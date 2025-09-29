package com.technicman.ectolum.util;

import com.mojang.datafixers.util.Pair;
import com.technicman.ectolum.EctoluminescenceClient;
import net.fabricmc.fabric.impl.client.rendering.ArmorRendererRegistryImpl;
import net.fabricmc.fabric.mixin.client.rendering.ArmorFeatureRendererMixin;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.trim.ArmorTrimMaterials;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.DyeColor;

import java.util.Arrays;
import java.util.List;

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
        if (hideBackground) {
            canvas.visible = false;
            if (isWall) {
                matrices.translate(0.0F, 0.0F, 0.2F);
            }
        }
        canvas.render(matrices, baseSprite.getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid, glint), light, overlay);

        for(int i = 0; i < 17 && i < patterns.size(); ++i) {
            Pair<RegistryEntry<BannerPattern>, DyeColor> pair = patterns.get(i);
            ModelPart currectPart = i == 0 ? canvas : patternPart;
            float[] fs = pair.getSecond().getColorComponents();
            float[] nfs = fs.clone();
            if (glowing) {
                for(int j = 0; j < 3; j++) {
                    nfs[j] *= EctoluminescenceClient.COLOR_VALUE_MULTIPLIER;
                    if (nfs[j] > 1.0f) {
                        nfs[j] = 1.0f;
                    }
                }
            }
            int finalI = i;
            ((RegistryEntry)pair.getFirst()).getKey().map((key) -> {
                return isBanner ? TexturedRenderLayers.getBannerPatternTextureId((RegistryKey<BannerPattern>) key) : TexturedRenderLayers.getShieldPatternTextureId((RegistryKey<BannerPattern>) key);
            }).ifPresent((sprite) -> currectPart.render(matrices, ((SpriteIdentifier)sprite).getVertexConsumer(vertexConsumers, RenderLayer::getItemEntityTranslucentCull), glowing && finalI > 0 ? EctoluminescenceClient.GLOW_STRENGTH : light, overlay, nfs[0], nfs[1], nfs[2], 1.0F));
        }
    }
}
