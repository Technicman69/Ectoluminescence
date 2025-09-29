package com.technicman.ectolum.mixin.client;

import com.technicman.ectolum.addon.EctolumSheepInterface;
import com.technicman.ectolum.EctoluminescenceClient;
import net.minecraft.client.render.entity.feature.SheepWoolFeatureRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(SheepWoolFeatureRenderer.class)
public class SheepWoolFeatureRendererMixin {
    @ModifyArgs(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/SheepEntity;FFFFFF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/feature/SheepWoolFeatureRenderer;render(Lnet/minecraft/client/render/entity/model/EntityModel;Lnet/minecraft/client/render/entity/model/EntityModel;Lnet/minecraft/util/Identifier;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFFFFF)V")
            )
    private void render(Args args) {
        int light = args.get(5);
        EctolumSheepInterface sheepEntity = args.get(6);
        args.set(5, sheepEntity.ectolum$isGlowing() ? EctoluminescenceClient.GLOW_STRENGTH : light);
        // Changing rgb values, multiplying their values, making color x1.2 brighter
//        if (sheepEntity.ectolum$isGlowing()) {
//            for (int c = 13; c <= 15; c++) {
//                float colorChannel = args.get(c);
//                colorChannel *= EctoluminescenceClient.COLOR_VALUE_MULTIPLIER;
//                if (colorChannel > 1.0f) {
//                    colorChannel = 1.0f;
//                }
//                args.set(c, colorChannel);
//            }
//        }
    }
}
