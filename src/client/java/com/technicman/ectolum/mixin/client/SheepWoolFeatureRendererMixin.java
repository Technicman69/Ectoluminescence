package com.technicman.ectolum.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.technicman.ectolum.EctoluminescenceClient;
import com.technicman.ectolum.target_access.SheepAccess;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.SheepWoolFeatureRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SheepWoolFeatureRenderer.class)
public class SheepWoolFeatureRendererMixin {
    @WrapOperation(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/client/render/entity/state/SheepEntityRenderState;FF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/feature/SheepWoolFeatureRenderer;render(Lnet/minecraft/client/model/Model;Lnet/minecraft/util/Identifier;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/client/render/entity/state/LivingEntityRenderState;II)V")
            )
    private void render(Model<?> model, Identifier identifier, MatrixStack matrixStack, OrderedRenderCommandQueue queue, int light, LivingEntityRenderState livingEntityRenderState, int color, int queueOrder, Operation<Void> original) {
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
