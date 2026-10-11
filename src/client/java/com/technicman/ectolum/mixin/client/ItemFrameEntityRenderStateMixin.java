package com.technicman.ectolum.mixin.client;

import com.technicman.ectolum.target_access.ItemFrameAccess;
import net.minecraft.client.render.entity.state.ItemFrameEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemFrameEntityRenderState.class)
public class ItemFrameEntityRenderStateMixin implements ItemFrameAccess {
    @Unique
    public boolean ectolum$clear;

    @Override
    public boolean ectolum$isClear() {
        return ectolum$clear;
    }

    @Override
    public void ectolum$setClear(boolean clear) {
        ectolum$clear = clear;
    }
}
