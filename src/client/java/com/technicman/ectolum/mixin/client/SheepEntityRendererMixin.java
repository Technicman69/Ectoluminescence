package com.technicman.ectolum.mixin.client;

import com.technicman.ectolum.target_access.SheepAccess;
import net.minecraft.client.render.entity.SheepEntityRenderer;
import net.minecraft.client.render.entity.state.SheepEntityRenderState;
import net.minecraft.entity.passive.SheepEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SheepEntityRenderer.class)
public class SheepEntityRendererMixin {
    @Inject(
            method = "updateRenderState(Lnet/minecraft/entity/passive/SheepEntity;Lnet/minecraft/client/render/entity/state/SheepEntityRenderState;F)V",
            at = @At("TAIL")
    )
    private void updateRenderState(SheepEntity sheepEntity, SheepEntityRenderState sheepEntityRenderState, float f, CallbackInfo ci) {
        ((SheepAccess)sheepEntityRenderState).ectolum$setGlowing(((SheepAccess)sheepEntity).ectolum$isGlowing());
    }
}
