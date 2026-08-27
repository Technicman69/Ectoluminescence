package com.technicman.ectolum.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.datafixers.util.Pair;
import com.technicman.ectolum.accessor.EctolumArmorTrimInterface;
import com.technicman.ectolum.util.GlobalVariables;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @Unique
    public void ectolum$applyTrim(DynamicRegistryManager registryManager, ItemStack stack, ArmorTrim trim) {
        if (!((EctolumArmorTrimInterface) trim).ectolum$hidden()) {
            ArmorTrim.apply(registryManager, stack, trim);
        } else {
            stack.removeSubNbt("Trim");
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
        DynamicRegistryManager manager = world.getRegistryManager();
        ArmorTrim.getTrim(manager, stack).ifPresentOrElse((trim) -> {
            EctolumArmorTrimInterface ectolumTrim = (EctolumArmorTrimInterface) trim;
            if (ectolumTrim.ectolum$hasEchoingLayers()) {
                long time = world.getTime();
                float t = Math.floorMod(time, 10L) * 0.1f;
                float w = -(MathHelper.cos(MathHelper.PI * t) + 1) * 0.5f;
                Pair<ArmorTrim, ArmorTrim> pair = ectolumTrim.ectolum$calculateTrimsAtInterval(Math.floorDiv(time, 10L));


                //var l = ectolumTrim.ectolum$getEchoingLayers().length;
                //System.out.println("Frame " + Math.floorDiv(time, 10L) % l + "/" + ectolumTrim.ectolum$getEchoingLayers().length + " : " + w);
                boolean firstHidden = ((EctolumArmorTrimInterface) pair.getFirst()).ectolum$hidden();
                boolean secondHidden = ((EctolumArmorTrimInterface) pair.getSecond()).ectolum$hidden();
                boolean isConstant = (pair.getFirst().equals(pair.getSecond()) || firstHidden && secondHidden) && !(
                        (!firstHidden && secondHidden) || (firstHidden && !secondHidden)
                        );

                ectolum$applyTrim(manager, stack, pair.getFirst());
                BakedModel model1 = instance.getModel(stack, world, null, 0);
                original.call(instance, model1, stack, light, overlay, matrices, vertices);
                if (!isConstant) {
                    GlobalVariables.nextAlpha = w;

                    ectolum$applyTrim(manager, stack, pair.getSecond());
                    BakedModel model2 = instance.getModel(stack, world, null, 0);
                    original.call(instance, model2, stack, light, overlay, matrices, vertices);

                    GlobalVariables.nextAlpha = 1f;
                }

                ArmorTrim.apply(manager, stack, trim);
            } else {
                original.call(instance, model, stack, light, overlay, matrices, vertices);
            }
        }, () -> original.call(instance, model, stack, light, overlay, matrices, vertices));
    }
}
