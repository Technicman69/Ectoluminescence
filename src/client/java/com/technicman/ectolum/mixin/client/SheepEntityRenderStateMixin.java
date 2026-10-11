package com.technicman.ectolum.mixin.client;

import com.technicman.ectolum.target_access.SheepAccess;
import net.minecraft.client.render.entity.state.SheepEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SheepEntityRenderState.class)
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
