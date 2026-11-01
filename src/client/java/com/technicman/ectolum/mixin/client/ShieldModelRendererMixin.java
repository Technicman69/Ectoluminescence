package com.technicman.ectolum.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.technicman.ectolum.component.BannerEffects;
import com.technicman.ectolum.component.ModComponents;
import com.technicman.ectolum.util.MixinGlobals;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.special.ShieldSpecialRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.util.Unit;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShieldSpecialRenderer.class)
public class ShieldModelRendererMixin {
    @WrapOperation(
            method = "submit(Lnet/minecraft/core/component/DataComponentMap;Lnet/minecraft/world/item/ItemDisplayContext;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IIZI)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/blockentity/BannerRenderer;submitPatterns(Lnet/minecraft/client/resources/model/MaterialSet;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IILnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/client/resources/model/Material;ZLnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/level/block/entity/BannerPatternLayers;ZLnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;I)V")
    )
    private void withEffects(MaterialSet materials, PoseStack matrices, SubmitNodeCollector queue, int light, int overlay, Model<Unit> model, Object state, Material spriteId, boolean useBannerLayer, DyeColor color, BannerPatternLayers patterns, boolean bl, ModelFeatureRenderer.CrumblingOverlay crumblingOverlayCommand, int i, Operation<Void> original, DataComponentMap components) {
        MixinGlobals.storedBannerEffects = components.getOrDefault(ModComponents.BANNER_EFFECTS, BannerEffects.EMPTY);
        if (MixinGlobals.storedBannerEffects.isBackgroundHidden()) spriteId = ModelBakery.NO_PATTERN_SHIELD;

        original.call(materials, matrices, queue, light, overlay, model, state, spriteId, useBannerLayer, color, patterns, bl, crumblingOverlayCommand, i);

        MixinGlobals.storedBannerEffects = BannerEffects.EMPTY;
    }
}
