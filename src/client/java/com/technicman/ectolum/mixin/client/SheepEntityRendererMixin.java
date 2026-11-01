package com.technicman.ectolum.mixin.client;

import com.technicman.ectolum.target_access.SheepAccess;
import net.minecraft.client.renderer.entity.SheepRenderer;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import net.minecraft.world.entity.animal.sheep.Sheep;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SheepRenderer.class)
public class SheepEntityRendererMixin {
    @Inject(
            method = "extractRenderState(Lnet/minecraft/world/entity/animal/sheep/Sheep;Lnet/minecraft/client/renderer/entity/state/SheepRenderState;F)V",
            at = @At("TAIL")
    )
    private void updateRenderState(Sheep sheepEntity, SheepRenderState sheepEntityRenderState, float f, CallbackInfo ci) {
        ((SheepAccess)sheepEntityRenderState).ectolum$setGlowing(((SheepAccess)sheepEntity).ectolum$isGlowing());
    }
}
