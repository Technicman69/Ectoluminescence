package com.technicman.ectolum.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.technicman.ectolum.component.BannerEffects;
import com.technicman.ectolum.component.ModComponents;
import com.technicman.ectolum.util.MixinGlobals;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.special.ShieldSpecialRenderer;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.util.Unit;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShieldSpecialRenderer.class)
public class ShieldModelRendererMixin {
    @WrapOperation(
            method = "submit(Lnet/minecraft/core/component/DataComponentMap;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IIZI)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;IIILnet/minecraft/client/resources/model/sprite/SpriteId;Lnet/minecraft/client/resources/model/sprite/SpriteGetter;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V",
                    ordinal = 0
            )
    )
    private void withEffects(SubmitNodeCollector instance, Model<Unit> model, Object o, PoseStack poseStack, int i, int j, int k, SpriteId spriteId, SpriteGetter spriteGetter, int i1, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, Operation<Void> original, final @Nullable DataComponentMap components) {
        MixinGlobals.storedBannerEffects = components != null ? components.getOrDefault(ModComponents.BANNER_EFFECTS, BannerEffects.EMPTY) : BannerEffects.EMPTY;
        if (MixinGlobals.storedBannerEffects.isBackgroundHidden()) spriteId = Sheets.SHIELD_BASE_NO_PATTERN;

        original.call(instance, model, o, poseStack, i, j, k, spriteId, spriteGetter, i1, crumblingOverlay);

        MixinGlobals.storedBannerEffects = BannerEffects.EMPTY;
    }
}
