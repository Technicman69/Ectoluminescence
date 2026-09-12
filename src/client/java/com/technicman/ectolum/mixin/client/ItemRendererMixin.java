package com.technicman.ectolum.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.technicman.ectolum.util.EchoingKeyframe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Objects;

import static com.technicman.ectolum.component.ModComponents.ECHOING_LAYERS;
import static net.minecraft.component.DataComponentTypes.TRIM;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @Unique
    private static float ectolum$nextAlpha = 1.0f;

    @Unique
    public void ectolum$applyEchoingKeyFrame(ItemStack stack, EchoingKeyframe keyframe) {
        if (!keyframe.hidden()) {
            stack.set(TRIM, keyframe.toTrim(Objects.requireNonNull(stack.get(TRIM))));
        } else {
            stack.remove(TRIM);
        }
    }
    @WrapOperation(
            method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/ItemRenderer;renderBakedItemModel(Lnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;)V"
            )
    )
    private void renderEchoingLayers(ItemRenderer instance, BakedModel model, ItemStack stack, int light, int overlay, MatrixStack matrices, VertexConsumer vertices, Operation<Void> original) {
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world == null) {
            original.call(instance, model, stack, light, overlay, matrices, vertices);
            return;
        }
        if (stack.contains(TRIM) && stack.contains(ECHOING_LAYERS)) {
            ArmorTrim savedTrim = stack.get(TRIM);

            long worldTime = world.getTime();
            float t = Math.floorMod(worldTime, 10L) * 0.1f;
            long time = Math.floorDiv(worldTime, 10L);
            Pair<EchoingKeyframe, EchoingKeyframe> pair = EchoingKeyframe.buildKeyFrames(stack, time);

            boolean leftHidden = pair.getLeft().hidden();
            boolean rightHidden = pair.getRight().hidden();
            boolean isConstant = (pair.getLeft().equals(pair.getRight()) || leftHidden && rightHidden) && !(
                    (!leftHidden && rightHidden) || (leftHidden && !rightHidden)
                    );

            ectolum$applyEchoingKeyFrame(stack, pair.getLeft());
            BakedModel model1 = instance.getModel(stack, world, null, 0);
            original.call(instance, model1, stack, light, overlay, matrices, vertices);
            if (!isConstant) {
                ectolum$nextAlpha = -(MathHelper.cos(MathHelper.PI * t) + 1) * 0.5f;

                ectolum$applyEchoingKeyFrame(stack, pair.getRight());
                BakedModel model2 = instance.getModel(stack, world, null, 0);
                original.call(instance, model2, stack, light, overlay, matrices, vertices);

                ectolum$nextAlpha = 1f;
            }

            stack.set(TRIM, savedTrim);
        } else {
            original.call(instance, model, stack, light, overlay, matrices, vertices);
        }
    }

    @ModifyArg(
            method = "renderBakedItemQuads",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/VertexConsumer;quad(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/model/BakedQuad;FFFFII)V"
            ),
            index = 5
    )
    private float applyAlpha(float alpha) {

        return alpha * ectolum$nextAlpha;
    }
}
