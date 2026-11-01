package com.technicman.ectolum.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.technicman.ectolum.entity.ClearItemFrameEntity;
import com.technicman.ectolum.target_access.ItemFrameAccess;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.renderer.entity.state.ItemFrameRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.technicman.ectolum.util.MixinGlobals.CLEAR_ITEM_FRAME;

@Mixin(ItemFrameRenderer.class)
public abstract class ItemFrameEntityRendererMixin<T extends ItemFrame> extends EntityRenderer<T, ItemFrameRenderState> {
    protected ItemFrameEntityRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Inject(
            method = "extractRenderState(Lnet/minecraft/world/entity/decoration/ItemFrame;Lnet/minecraft/client/renderer/entity/state/ItemFrameRenderState;F)V",
            at = @At("TAIL")
    )
    private void updateRenderState(T itemFrameEntity, ItemFrameRenderState itemFrameEntityRenderState, float f, CallbackInfo ci) {
        ((ItemFrameAccess)itemFrameEntityRenderState).ectolum$setClear(itemFrameEntity instanceof ClearItemFrameEntity);
    }

    @WrapOperation(
            method = "submit(Lnet/minecraft/client/renderer/entity/state/ItemFrameRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/resources/model/BlockStateDefinitions;getItemFrameFakeState(ZZ)Lnet/minecraft/world/level/block/state/BlockState;")
    )
    private BlockState getStateForItemFrame(boolean hasGlow, boolean hasMap, Operation<BlockState> original, ItemFrameRenderState itemFrameEntityRenderState) {
        if (((ItemFrameAccess)itemFrameEntityRenderState).ectolum$isClear()) {
            return CLEAR_ITEM_FRAME.any().setValue(BlockStateProperties.MAP, hasMap);
        }
        return original.call(hasGlow, hasMap);
    }

    @ModifyArg(
            method = "submit(Lnet/minecraft/client/renderer/entity/state/ItemFrameRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitBlockStateModel(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;Lnet/minecraft/client/renderer/block/model/BlockStateModel;FFFIII)V"
            ),
            index = 1
    )
    private RenderType renderLayer(RenderType original) {
        return RenderTypes.itemEntityTranslucentCull(TextureAtlas.LOCATION_BLOCKS);
    }
}
