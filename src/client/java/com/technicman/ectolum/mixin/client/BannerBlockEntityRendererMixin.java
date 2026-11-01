package com.technicman.ectolum.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.technicman.ectolum.EctoluminescenceClient;
import com.technicman.ectolum.component.BannerEffects;
import com.technicman.ectolum.target_access.BannerAccess;
import com.technicman.ectolum.util.MixinGlobals;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.state.BannerRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(BannerRenderer.class)
public class BannerBlockEntityRendererMixin {

    @Inject(
            method = "extractRenderState(Lnet/minecraft/world/level/block/entity/BannerBlockEntity;Lnet/minecraft/client/renderer/blockentity/state/BannerRenderState;FLnet/minecraft/world/phys/Vec3;Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V",
            at = @At("TAIL")
    )
    private void updateRenderState(BannerBlockEntity bannerBlockEntity, BannerRenderState bannerBlockEntityRenderState, float f, Vec3 vec3d, ModelFeatureRenderer.CrumblingOverlay crumblingOverlayCommand, CallbackInfo ci) {
        ((BannerAccess)bannerBlockEntityRenderState).ectolum$setBannerEffects(((BannerAccess)bannerBlockEntity).ectolum$getBannerEffects());
    }

    @Inject(
            method = "submit(Lnet/minecraft/client/renderer/blockentity/state/BannerRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V",
            at = @At("HEAD")
    )
    private void readEffects(BannerRenderState bannerBlockEntityRenderState, PoseStack matrixStack, SubmitNodeCollector orderedRenderCommandQueue, CameraRenderState cameraRenderState, CallbackInfo ci) {
        MixinGlobals.storedBannerEffects = ((BannerAccess)bannerBlockEntityRenderState).ectolum$getBannerEffects();
    }

    @Inject(
            method = "submit(Lnet/minecraft/client/renderer/blockentity/state/BannerRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V",
            at = @At("TAIL")
    )
    private void clearEffects(CallbackInfo ci) {
        // Clear effects to prevent banner items having them
        MixinGlobals.storedBannerEffects = BannerEffects.EMPTY;
        // This prevents items being invisible
    }

    @WrapOperation(
            method = "submitBanner(Lnet/minecraft/client/resources/model/MaterialSet;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IIFLnet/minecraft/client/model/object/banner/BannerModel;Lnet/minecraft/client/model/object/banner/BannerFlagModel;FLnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/level/block/entity/BannerPatternLayers;Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IIILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V")
    )
    private static void renderBaseBlock(SubmitNodeCollector instance, Model<Unit> model, Object state, PoseStack matrices, RenderType renderLayer, int light, int overlay, int tintedColor, TextureAtlasSprite sprite, int outlineColor, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, Operation<Void> original) {
        if (!MixinGlobals.storedBannerEffects.isBackgroundHidden()) {
            // If not, just render banner base like normal
            original.call(instance, model, state, matrices, renderLayer, light, overlay, tintedColor, sprite, outlineColor, crumblingOverlay);
        }
    }

    @WrapOperation(
            method = "submitPatterns",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IIILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V",
                    ordinal = 0
            )
    )
    private static void renderBaseFlag(SubmitNodeCollector instance, Model<Unit> model, Object o, PoseStack matrixStack, RenderType renderLayer, int light, int overlay, int tintedColor, TextureAtlasSprite sprite, int outlineColor, ModelFeatureRenderer.CrumblingOverlay crumblingOverlayCommand, Operation<Void> original, MaterialSet s, PoseStack m, SubmitNodeCollector q, int l, int ov, Model<Unit> mo, Object state, Material sp, boolean useBannerLayer) {
        if (!(useBannerLayer && MixinGlobals.storedBannerEffects.isBackgroundHidden())) {
            original.call(instance, model, o, matrixStack, renderLayer, light, overlay, tintedColor, sprite, outlineColor, crumblingOverlayCommand);
        }
    }

    @WrapOperation(
            method = "submitPatterns",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/blockentity/BannerRenderer;submitPatternLayer(Lnet/minecraft/client/resources/model/MaterialSet;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IILnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/client/resources/model/Material;Lnet/minecraft/world/item/DyeColor;Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V",
                    ordinal = 0
            )
    )
    private static void renderBaseFlagPattern(MaterialSet materials, PoseStack matrices, SubmitNodeCollector queue, int light, int overlay, Model<Float> model, Object state, Material spriteId, DyeColor color, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, Operation<Void> original) {
        if (!MixinGlobals.storedBannerEffects.isBackgroundHidden()) {
            original.call(materials, matrices, queue, light, overlay, model, state, spriteId, color, crumblingOverlay);
        }
    }

    @WrapOperation(
            method = "submitPatterns",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/blockentity/BannerRenderer;submitPatternLayer(Lnet/minecraft/client/resources/model/MaterialSet;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IILnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/client/resources/model/Material;Lnet/minecraft/world/item/DyeColor;Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V",
                    ordinal = 1
            )
    )
    private static void renderBasePattern(MaterialSet materials, PoseStack matrices, SubmitNodeCollector queue, int light, int overlay, Model<Float> model, Object state, Material spriteId, DyeColor color, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, Operation<Void> original) {
        light = MixinGlobals.storedBannerEffects.isGlowing() ? EctoluminescenceClient.GLOW_STRENGTH : light;
        original.call(materials, matrices, queue, light, overlay, model, state, spriteId, color, crumblingOverlay);
    }

    @ModifyArg(
            method = "submitPatternLayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/resources/model/Material;renderType(Ljava/util/function/Function;)Lnet/minecraft/client/renderer/rendertype/RenderType;"
            )
    )
    private static Function<Identifier, RenderType> changeRenderLayer(Function<Identifier, RenderType> layerFactory) {
        // To prevent pattern from displaying clouds behind it etc
        return RenderTypes::itemEntityTranslucentCull;
    }
}