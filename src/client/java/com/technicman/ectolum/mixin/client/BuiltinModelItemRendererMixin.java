package com.technicman.ectolum.mixin.client;

import com.technicman.ectolum.component.ModComponents;
import com.technicman.ectolum.util.BannerEffects;
import com.technicman.ectolum.util.CanvasRenderer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BuiltinModelItemRenderer.class)
public class BuiltinModelItemRendererMixin {
    @Redirect(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/entity/BannerBlockEntityRenderer;renderCanvas(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/util/SpriteIdentifier;ZLnet/minecraft/util/DyeColor;Lnet/minecraft/component/type/BannerPatternsComponent;Z)V")
    )
    private void renderCanvas(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, ModelPart canvas, SpriteIdentifier baseSprite, boolean isBanner, DyeColor color, BannerPatternsComponent patterns, boolean glint, ItemStack stack) {
        // Render banner on shield
        BannerEffects effects = stack.getOrDefault(ModComponents.BANNER_EFFECTS, BannerEffects.EMPTY);
        if (effects.isBackgroundHidden()) {
            baseSprite = ModelLoader.SHIELD_BASE_NO_PATTERN;
        }
        CanvasRenderer.renderCanvas(matrices, vertexConsumers, light, overlay, canvas, canvas, baseSprite, isBanner, color, patterns, glint, effects, false);
    }
}
