package com.technicman.ectolum.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.technicman.ectolum.entity.ClearItemFrameEntity;
import com.technicman.ectolum.target_access.ItemFrameAccess;
import com.technicman.ectolum.util.MixinGlobals;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.renderer.entity.state.ItemFrameRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.decoration.ItemFrame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemFrameRenderer.class)
public abstract class ItemFrameEntityRendererMixin<T extends ItemFrame> extends EntityRenderer<T, ItemFrameRenderState> {
    protected ItemFrameEntityRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Inject(
            method = "extractRenderState(Lnet/minecraft/world/entity/decoration/ItemFrame;Lnet/minecraft/client/renderer/entity/state/ItemFrameRenderState;F)V",
            at = @At("HEAD")
    )
    private void updateRenderState(T entity, ItemFrameRenderState state, float partialTicks, CallbackInfo ci) {
        boolean clear = entity instanceof ClearItemFrameEntity;
        ((ItemFrameAccess) state).ectolum$setClear(clear);
        MixinGlobals.storedClearFlag = clear;
        // Value stored for BlockStateDefinitions to pick the right model
    }

    @Inject(
            method = "submit(Lnet/minecraft/client/renderer/entity/state/ItemFrameRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
            at = @At("HEAD")
    )
    private void loadRenderState(ItemFrameRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
        MixinGlobals.storedClearFlag = ((ItemFrameAccess) state).ectolum$isClear();
        // Value stored for BlockModelRenderState to pick right render type
    }
}
