package com.technicman.ectolum.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.technicman.ectolum.EctoluminescenceClient;
import com.technicman.ectolum.target_access.SheepAccess;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.SheepWoolLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SheepWoolLayer.class)
public class SheepWoolFeatureRendererMixin {
    @WrapOperation(
            method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/SheepRenderState;FF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/SheepWoolLayer;coloredCutoutModelCopyLayerRender(Lnet/minecraft/client/model/Model;Lnet/minecraft/resources/Identifier;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;II)V")
            )
    private void render(Model<?> model, Identifier identifier, PoseStack matrixStack, SubmitNodeCollector queue, int light, LivingEntityRenderState livingEntityRenderState, int color, int queueOrder, Operation<Void> original) {
        SheepAccess sheepRenderState = (SheepAccess) livingEntityRenderState;
        if (sheepRenderState.ectolum$isGlowing()) light = EctoluminescenceClient.GLOW_STRENGTH;
        // Changing rgb values, multiplying their values, making color x1.2 brighter
//        if (sheepRenderState.ectolum$isGlowing()) {
//            for (int c = 13; c <= 15; c++) {
//                float colorChannel = args.get(c);
//                colorChannel *= EctoluminescenceClient.COLOR_VALUE_MULTIPLIER;
//                if (colorChannel > 1.0f) {
//                    colorChannel = 1.0f;
//                }
//                args.set(c, colorChannel);
//            }
//        }
        original.call(model, identifier, matrixStack, queue, light, livingEntityRenderState, color, queueOrder);
    }
}
