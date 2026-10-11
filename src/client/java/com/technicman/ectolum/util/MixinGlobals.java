package com.technicman.ectolum.util;

import com.technicman.ectolum.Ectoluminescence;
import com.technicman.ectolum.component.BannerEffects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Unique;

/**
 * For storing temporary values between mixins
 */
public class MixinGlobals {
    public static StateManager<Block, BlockState> CLEAR_ITEM_FRAME;
    public static final Identifier CLEAR_ITEM_FRAME_ID = Ectoluminescence.id("clear_item_frame");
    @Unique
    public static BannerEffects storedBannerEffects = BannerEffects.EMPTY;
}
