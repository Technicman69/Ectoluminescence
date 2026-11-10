package com.technicman.ectolum.util;

import com.technicman.ectolum.Ectoluminescence;
import com.technicman.ectolum.component.BannerEffects;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

/**
 * For storing temporary values between mixins
 */
public class MixinGlobals {
    public static StateDefinition<Block, BlockState> CLEAR_ITEM_FRAME;
    public static final Identifier CLEAR_ITEM_FRAME_ID = Ectoluminescence.id("clear_item_frame");

    public static BannerEffects storedBannerEffects = BannerEffects.EMPTY;
    public static boolean storedClearFlag;
    public static boolean isTrimModel;
}
