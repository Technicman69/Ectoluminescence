package com.technicman.ectolum.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.technicman.ectolum.target_access.SignAccess;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.AbstractSignRenderer;
import net.minecraft.client.renderer.blockentity.state.SignRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSignRenderer.class)
public class AbstractSignBlockEntityRendererMixin {
    @Inject(
            method = "extractRenderState(Lnet/minecraft/world/level/block/entity/SignBlockEntity;Lnet/minecraft/client/renderer/blockentity/state/SignRenderState;FLnet/minecraft/world/phys/Vec3;Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V",
            at = @At("TAIL")
    )
    private void updateRenderState(SignBlockEntity signBlockEntity, SignRenderState signBlockEntityRenderState, float f, Vec3 vec3d, ModelFeatureRenderer.CrumblingOverlay crumblingOverlayCommand, CallbackInfo ci) {
        ((SignAccess) signBlockEntityRenderState).ectolum$setHideBackground(((SignAccess) signBlockEntity).ectolum$isBackgroundHidden());
    }

    @WrapOperation(
        method = "submitSignWithText(Lnet/minecraft/client/renderer/blockentity/state/SignRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/SignBlock;Lnet/minecraft/world/level/block/state/properties/WoodType;Lnet/minecraft/client/model/Model$Simple;Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;Lnet/minecraft/client/renderer/SubmitNodeCollector;)V",
                at = @At(target = "Lnet/minecraft/client/renderer/blockentity/AbstractSignRenderer;submitSign(Lcom/mojang/blaze3d/vertex/PoseStack;ILnet/minecraft/world/level/block/state/properties/WoodType;Lnet/minecraft/client/model/Model$Simple;Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;Lnet/minecraft/client/renderer/SubmitNodeCollector;)V", value = "INVOKE")
    )
    private void render(AbstractSignRenderer instance, PoseStack matrices, int lightmapCoords, WoodType woodType, Model.Simple model, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, SubmitNodeCollector queue, Operation<Void> original, SignRenderState renderState, PoseStack m, BlockState blockState, SignBlock block) {
        if (((SignAccess) renderState).ectolum$isBackgroundHidden()) {
            if (block instanceof WallSignBlock) {
                matrices.translate(0f, 0f, -0.062f);
                matrices.scale(1f, 1f, 0f);
            }
        } else {
            original.call(instance, matrices, lightmapCoords, woodType, model, crumblingOverlay, queue);
        }
    }
}
