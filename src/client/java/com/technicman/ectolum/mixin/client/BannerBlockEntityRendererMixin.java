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
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
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
    private void updateRenderState(BannerBlockEntity blockEntity, BannerRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress, CallbackInfo ci) {
        ((BannerAccess) state).ectolum$setBannerEffects(((BannerAccess) blockEntity).ectolum$getBannerEffects());
    }

    @Inject(
            method = "submit(Lnet/minecraft/client/renderer/blockentity/state/BannerRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
            at = @At("HEAD")
    )
    private void readEffects(BannerRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
        MixinGlobals.storedBannerEffects = ((BannerAccess) state).ectolum$getBannerEffects();
    }

    @Inject(
            method = "submit(Lnet/minecraft/client/renderer/blockentity/state/BannerRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
            at = @At("TAIL")
    )
    private void clearEffects(CallbackInfo ci) {
        // Clear effects to prevent banner items having them
        MixinGlobals.storedBannerEffects = BannerEffects.EMPTY;
        // This prevents items being invisible
    }

    @WrapOperation(
            method = "submitBanner",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;IIILnet/minecraft/client/resources/model/sprite/SpriteId;Lnet/minecraft/client/resources/model/sprite/SpriteGetter;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"
            )
    )
    private static void renderBaseBlock(SubmitNodeCollector instance, Model<Unit> model, Object o, PoseStack poseStack, int i, int j, int k, SpriteId spriteId, SpriteGetter spriteGetter, int i1, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, Operation<Void> original) {
        if (!MixinGlobals.storedBannerEffects.isBackgroundHidden()) {
            // If not, just render banner base like normal
            original.call(instance, model, o, poseStack, i, j, k, spriteId, spriteGetter, i1, crumblingOverlay);
        }
    }

    @WrapOperation(
            method = "submitPatterns",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/blockentity/BannerRenderer;submitPatternLayer(Lnet/minecraft/client/resources/model/sprite/SpriteGetter;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IILnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/client/resources/model/sprite/SpriteId;Lnet/minecraft/world/item/DyeColor;Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V",
                    ordinal = 0
            )
    )
    private static void renderBaseFlagPattern(SpriteGetter sprites, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, Model<Unit> model, Object state, SpriteId sprite, DyeColor color, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress, Operation<Void> original) {
        if (!MixinGlobals.storedBannerEffects.isBackgroundHidden()) {
            original.call(sprites, poseStack, submitNodeCollector, lightCoords, overlayCoords, model, state, sprite, color, breakProgress);
        }
    }

    @WrapOperation(
            method = "submitPatterns",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/blockentity/BannerRenderer;submitPatternLayer(Lnet/minecraft/client/resources/model/sprite/SpriteGetter;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IILnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/client/resources/model/sprite/SpriteId;Lnet/minecraft/world/item/DyeColor;Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V",
                    ordinal = 1
            )
    )
    private static void renderBasePattern(SpriteGetter sprites, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, Model<Unit> model, Object state, SpriteId sprite, DyeColor color, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress, Operation<Void> original) {
        lightCoords = MixinGlobals.storedBannerEffects.isGlowing() ? EctoluminescenceClient.GLOW_STRENGTH : lightCoords;
        original.call(sprites, poseStack, submitNodeCollector, lightCoords, overlayCoords, model, state, sprite, color, breakProgress);
    }

    @ModifyArg(
            method = "submitPatternLayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/resources/model/sprite/SpriteId;renderType(Ljava/util/function/Function;)Lnet/minecraft/client/renderer/rendertype/RenderType;"
            )
    )
    private static Function<Identifier, RenderType> changeRenderLayer(Function<Identifier, RenderType> layerFactory) {
        // To prevent pattern from displaying clouds behind it etc
        return RenderTypes::entityTranslucentCullItemTarget;
    }
}