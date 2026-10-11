package com.technicman.ectolum.mixin.client;

import com.technicman.ectolum.target_access.SignAccess;
import net.minecraft.client.render.block.entity.state.SignBlockEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SignBlockEntityRenderState.class)
public class SignBlockEntityRenderStateMixin implements SignAccess {
    @Unique
    private boolean ectolum$hideBackground = false;

    @Override
    public boolean ectolum$isBackgroundHidden() {
        return ectolum$hideBackground;
    }

    @Override
    public void ectolum$setHideBackground(boolean hideBackground) {
        ectolum$hideBackground = hideBackground;
    }
}
