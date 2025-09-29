package com.technicman.ectolum.mixin.client;

import com.technicman.ectolum.addon.EctolumSignInterface;
import net.minecraft.block.*;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.HangingSignBlockEntityRenderer;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SignBlockEntityRenderer.class)
public class SignBlockEntityRendererMixin {
    @Inject(
        method = "render(Lnet/minecraft/block/entity/SignBlockEntity;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/block/BlockState;Lnet/minecraft/block/AbstractSignBlock;Lnet/minecraft/block/WoodType;Lnet/minecraft/client/model/Model;)V",
        at = @At(target = "Lnet/minecraft/client/render/block/entity/SignBlockEntityRenderer;renderSign(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/block/WoodType;Lnet/minecraft/client/model/Model;)V", value = "INVOKE")
    )
    private void render(SignBlockEntity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BlockState state, AbstractSignBlock block, WoodType woodType, Model model, CallbackInfo ci) {
        if (model instanceof SignBlockEntityRenderer.SignModel signModel) {
            signModel.root.visible = true;
            if (((EctolumSignInterface) entity).ectolum$isBackgroundHidden()) {
                signModel.root.visible = false;
                if (entity.getCachedState().getBlock() instanceof WallSignBlock) {
                    matrices.translate(0f, 0f, -0.062f);
                    matrices.scale(1f, 1f, 0f);
                }
            }
        } else if (model instanceof HangingSignBlockEntityRenderer.HangingSignModel hangingSignModel) {
            hangingSignModel.root.visible = !((EctolumSignInterface) entity).ectolum$isBackgroundHidden();
        }
    }
}
