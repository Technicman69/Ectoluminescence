package com.technicman.ectolum.mixin.client;

import com.technicman.ectolum.component.BannerEffects;
import com.technicman.ectolum.target_access.BannerAccess;
import net.minecraft.client.renderer.blockentity.state.BannerRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BannerRenderState.class)
public class BannerBlockEntityRenderStateMixin implements BannerAccess {
    @Unique
    public BannerEffects ectolum$effects;

    @Override
    public BannerEffects ectolum$getBannerEffects() {
        return ectolum$effects;
    }

    @Override
    public void ectolum$setBannerEffects(BannerEffects effects) {
        ectolum$effects = effects;
    }
}
