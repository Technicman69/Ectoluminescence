package com.technicman.ectolum.mixin.client;

import com.technicman.ectolum.target_access.DecoratedPotAccess;
import net.minecraft.client.render.block.entity.state.DecoratedPotBlockEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.BitSet;

@Mixin(DecoratedPotBlockEntityRenderState.class)
public class DecoratedPotBlockEntityRenderStateMixin implements DecoratedPotAccess {
    @Unique
    public BitSet ectolum$sherdGlow = new BitSet();

    @Override
    public boolean ectolum$getSherdGlow(int index) {
        return ectolum$sherdGlow.get(index);
    }

    @Override
    public void ectolum$setSherdGlow(int index, boolean glowing) {
        ectolum$sherdGlow.set(index, glowing);
    }
}
