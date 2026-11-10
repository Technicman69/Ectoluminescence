package com.technicman.ectolum.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.technicman.ectolum.util.MixinGlobals;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockModelRenderState.class)
public class BlockModelRenderStateMixin {
    @WrapOperation(
            method = "submitWithZOffset",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/rendertype/RenderTypes;entitySolidZOffsetForward(Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/rendertype/RenderType;")
    )
    private RenderType switchRenderLayer(Identifier texture, Operation<RenderType> original) {
        if (MixinGlobals.storedClearFlag) {
            // Just in case sth other than item frame uses this method
            MixinGlobals.storedClearFlag = false;

            return RenderTypes.armorTranslucent(texture);
        }
        return original.call(texture);
    }
}
