package com.technicman.ectolum.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.DyeColor;

import java.util.List;
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
            List<Pair<RegistryEntry<BannerPattern>, DyeColor>> patterns,
            boolean glint,
            boolean glowing,
            boolean hideBackground,
            boolean isWall
    ) {
        if (hideBackground && isBanner) {
            canvas.visible = false;
            if (isWall) {
                matrices.translate(0.0F, 0.0F, 0.2F);
            }
        }
        //if (isBanner) {
            canvas.render(matrices, baseSprite.getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid, glint), light, overlay);
        //}

        for(int i = (!isBanner && hideBackground) ? 1 : 0; i < 17 && i < patterns.size(); ++i) {
            Pair<RegistryEntry<BannerPattern>, DyeColor> pair = patterns.get(i);
            ModelPart currectPart = i == 0 ? canvas : patternPart;
            float[] fs = pair.getSecond().getColorComponents();
            float[] nfs = fs.clone();
            if (glowing) {
                IntStream.range(0, 3).forEach(j -> {
                    nfs[j] *= GlobalVariables.COLOR_VALUE_MULTIPLIER;
                    if (nfs[j] > 1.0f) {
                        nfs[j] = 1.0f;
                    }
                });
            }
            int finalI = i;
            ((RegistryEntry)pair.getFirst()).getKey().map((key) ->
                isBanner ? TexturedRenderLayers.getBannerPatternTextureId((RegistryKey<BannerPattern>) key)
                        : TexturedRenderLayers.getShieldPatternTextureId((RegistryKey<BannerPattern>) key)
            ).ifPresent((sprite) -> currectPart.render(matrices, ((SpriteIdentifier)sprite).getVertexConsumer(vertexConsumers, RenderLayer::getItemEntityTranslucentCull), glowing && finalI > 0 ? GlobalVariables.GLOW_STRENGTH : light, overlay, nfs[0], nfs[1], nfs[2], 1.0F));
        }
    }
}
