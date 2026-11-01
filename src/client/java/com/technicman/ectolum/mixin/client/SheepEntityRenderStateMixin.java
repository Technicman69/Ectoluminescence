package com.technicman.ectolum.mixin.client;

import com.technicman.ectolum.target_access.SheepAccess;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SheepRenderState.class)
public class SheepEntityRenderStateMixin implements SheepAccess {
    @Unique
    public boolean ectolum$glowing = false;

    @Override
    public boolean ectolum$isGlowing() {
        return ectolum$glowing;
    }

    @Override
    public void ectolum$setGlowing(boolean glowing) {
        ectolum$glowing = glowing;
    }
}
