package com.technicman.ectolum.util;

import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.ColorHelper;

import java.util.stream.IntStream;

public class CanvasRenderer {
    public static void renderCanvas(
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            int overlay,
            ModelPart canvas,
            ModelPart patternPart,
            SpriteIdentifier baseSprite,
            boolean isBanner,
            DyeColor dye,
            BannerPatternsComponent patterns,
            boolean glint,
            BannerEffects effects,
            boolean isWall
    ) {
        if (effects.isBackgroundHidden() && isBanner) {
            canvas.visible = false;
            if (isWall) {
                matrices.translate(0.0F, 0.0F, 0.2F);
            }
        }
        canvas.render(matrices, baseSprite.getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid, glint), light, overlay, dye.getEntityColor());
//        renderLayer(matrices, vertexConsumers, light, overlay, canvas, isBanner ? TexturedRenderLayers.BANNER_BASE : TexturedRenderLayers.SHIELD_BASE, dye);

//        for(int i = 0; i < 16 && i < patterns.layers().size(); ++i) {
//            BannerPatternsComponent.Layer layer = (BannerPatternsComponent.Layer)patterns.layers().get(i);
//            SpriteIdentifier spriteIdentifier = isBanner ? TexturedRenderLayers.getBannerPatternTextureId(layer.pattern()) : TexturedRenderLayers.getShieldPatternTextureId(layer.pattern());
//            renderLayer(matrices, vertexConsumers, light, overlay, canvas, spriteIdentifier, layer.color());
//        }

        for(int i = 0; i < 16 && i < patterns.layers().size(); ++i) {
            BannerPatternsComponent.Layer layer = patterns.layers().get(i);

            int color = layer.color().getEntityColor();
            if (effects.isGlowing()) {
                float[] ch = new float[]{ColorHelper.Argb.getRed(color) / 256f, ColorHelper.Argb.getGreen(color) / 256f, ColorHelper.Argb.getBlue(color) / 256f};
                IntStream.range(0, 3).forEach(j -> {
                    ch[j] *= GlobalVariables.COLOR_VALUE_MULTIPLIER;
                    if (ch[j] > 1.0f) {
                        ch[j] = 1.0f;
                    }
                });
                color = ColorHelper.Argb.fromFloats(1.0f, ch[0], ch[1], ch[2]);
            }

            SpriteIdentifier spriteIdentifier = isBanner ? TexturedRenderLayers.getBannerPatternTextureId(layer.pattern()) : TexturedRenderLayers.getShieldPatternTextureId(layer.pattern());
            patternPart.render(matrices, spriteIdentifier.getVertexConsumer(vertexConsumers, RenderLayer::getItemEntityTranslucentCull), effects.isGlowing() ? GlobalVariables.GLOW_STRENGTH : light, overlay, color);
//            ((RegistryEntry) layer.pattern()).getKey().map((key) ->
//                isBanner ? TexturedRenderLayers.BANNER_BASE
//                        : TexturedRenderLayers.SHIELD_BASE
//            ).ifPresent((sprite) -> currectPart.render(matrices, ((SpriteIdentifier)sprite).getVertexConsumer(vertexConsumers, RenderLayer::getItemEntityTranslucentCull), effects.isGlowing() && finalI > 0 ? GlobalVariables.GLOW_STRENGTH : light, overlay, finalColor));
        }
    }
}
