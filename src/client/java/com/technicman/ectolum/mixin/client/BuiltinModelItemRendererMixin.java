package com.technicman.ectolum.mixin.client;

import com.mojang.datafixers.util.Pair;
import com.technicman.ectolum.util.CanvasRenderer;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(BuiltinModelItemRenderer.class)
public class BuiltinModelItemRendererMixin {
    @Redirect(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/entity/BannerBlockEntityRenderer;renderCanvas(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/util/SpriteIdentifier;ZLjava/util/List;Z)V")
    )
    private void renderCanvas(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, ModelPart canvas, SpriteIdentifier baseSprite, boolean isBanner, List<Pair<RegistryEntry<BannerPattern>, DyeColor>> patterns, boolean glint, ItemStack stack) {
        // Render banner on shield
        boolean glowing = false;
        boolean hideBackground = false;
        NbtCompound entityTag = BlockItem.getBlockEntityNbt(stack);
        if (entityTag != null) {
            glowing = entityTag.getBoolean("ectolum.glowing");
            hideBackground = entityTag.getBoolean("ectolum.hide_background");
            if (hideBackground) {
                baseSprite = ModelLoader.SHIELD_BASE_NO_PATTERN;
            }
        }
        CanvasRenderer.renderCanvas(matrices, vertexConsumers, light, overlay, canvas, canvas, baseSprite, isBanner, patterns, glint, glowing, hideBackground, false);
    }
}
