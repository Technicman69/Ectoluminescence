package com.technicman.ectolum.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.technicman.ectolum.EctoluminescenceClient;
import com.technicman.ectolum.target_access.DecoratedPotAccess;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.DecoratedPotRenderer;
import net.minecraft.client.renderer.blockentity.state.DecoratedPotRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
import net.minecraft.world.level.block.entity.DecoratedPotPatterns;
import net.minecraft.world.level.block.entity.PotDecorations;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Optional;

@Mixin(DecoratedPotRenderer.class)
public abstract class DecoratedPotBlockEntityRendererMixin {
    @Mutable
    @Final
    @Shadow
    private final ModelPart frontSide;
    @Mutable
    @Final
    @Shadow
    private final ModelPart backSide;
    @Mutable
    @Final
    @Shadow
    private final ModelPart leftSide;
    @Mutable
    @Final
    @Shadow
    private final ModelPart rightSide;
    @Mutable
    @Final
    @Shadow
    private final SpriteGetter sprites;

    @Unique
    private DecoratedPotAccess ectolum$storedPot;

    protected DecoratedPotBlockEntityRendererMixin(ModelPart front, ModelPart back, ModelPart left, ModelPart right, SpriteGetter sprites) {
        this.frontSide = front;
        this.backSide = back;
        this.leftSide = left;
        this.rightSide = right;
        this.sprites = sprites;
    }

    @Inject(
            method = "extractRenderState(Lnet/minecraft/world/level/block/entity/DecoratedPotBlockEntity;Lnet/minecraft/client/renderer/blockentity/state/DecoratedPotRenderState;FLnet/minecraft/world/phys/Vec3;Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V",
            at = @At("TAIL")
    )
    private void updateRenderState(DecoratedPotBlockEntity blockEntity, DecoratedPotRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress, CallbackInfo ci) {
        DecoratedPotAccess potEntity = (DecoratedPotAccess) blockEntity;
        DecoratedPotAccess potRenderState = (DecoratedPotAccess) state;
        for (int i = 0; i < 4; i++) {
            potRenderState.ectolum$setSherdGlow(i, potEntity.ectolum$getSherdGlow(i));
        }
    }


    @WrapOperation(
            method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IILnet/minecraft/world/level/block/entity/PotDecorations;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModelPart(Lnet/minecraft/client/model/geom/ModelPart;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ZZILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;I)V"
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/world/level/block/entity/PotDecorations;front()Ljava/util/Optional;"
                    )
            )
    )
    private void renderSherd(SubmitNodeCollector instance, ModelPart part, PoseStack matrices, RenderType renderLayer, int light, int overlay, @org.jspecify.annotations.Nullable TextureAtlasSprite sprite, boolean sheeted, boolean hasGlint, int tintedColor, ModelFeatureRenderer.@org.jspecify.annotations.Nullable CrumblingOverlay crumblingOverlay, int i, Operation<Void> original, PoseStack m, SubmitNodeCollector queue, int l, int o, PotDecorations sherds) {
        final Map<ModelPart, Tuple<Integer, Optional<Item>>> partToSherd = Map.of(
                this.frontSide, new Tuple<>(DecoratedPotAccess.FRONT, sherds.front()),
                this.backSide, new Tuple<>(DecoratedPotAccess.BACK, sherds.back()),
                this.leftSide, new Tuple<>(DecoratedPotAccess.LEFT, sherds.left()),
                this.rightSide, new Tuple<>(DecoratedPotAccess.RIGHT, sherds.right())
        );

        // Replace sherd if it's glowing
        Tuple<Integer, Optional<Item>> pair = partToSherd.get(part);
        if (pair != null) {
            if (ectolum$storedPot != null && ectolum$storedPot.ectolum$getSherdGlow(pair.getA())) {
                // If glowing, swap textures nad set light
                light = EctoluminescenceClient.GLOW_STRENGTH;
                SpriteId spriteIdentifier = pair.getB().map(DecoratedPotBlockEntityRendererMixin::ectolum$glowingPatternTextureId).orElse(null);
                if (spriteIdentifier != null) {
                    renderLayer = spriteIdentifier.renderType(RenderTypes::entitySolid);
                    sprite = sprites.get(spriteIdentifier);
                }
            }
        }
        original.call(instance, part, matrices, renderLayer, light, overlay, sprite, sheeted, hasGlint, tintedColor, crumblingOverlay, i);
    }

    @Unique
    private static @Nullable SpriteId ectolum$glowingPatternTextureId(Item sherd) {
        ResourceKey<DecoratedPotPattern> baseKey = DecoratedPotPatterns.getPatternFromItem(sherd);
        if (baseKey != null) {
            ResourceKey<DecoratedPotPattern> glowingKey = ResourceKey.create(Registries.DECORATED_POT_PATTERN, baseKey.identifier().withSuffix("_glowing"));
            return Sheets.getDecoratedPotSprite(glowingKey);
        }
        return null;
    }

    @Inject(
            method = "submit(Lnet/minecraft/client/renderer/blockentity/state/DecoratedPotRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
            at = @At("HEAD")
    )
    private void injected(DecoratedPotRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
        ectolum$storedPot = (DecoratedPotAccess) state;
    }
}
