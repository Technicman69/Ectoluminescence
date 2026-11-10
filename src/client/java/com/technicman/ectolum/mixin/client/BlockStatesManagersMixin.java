package com.technicman.ectolum.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.technicman.ectolum.util.MixinGlobals;
import net.minecraft.client.resources.model.BlockStateDefinitions;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.function.Function;

@Mixin(BlockStateDefinitions.class)
public abstract class BlockStatesManagersMixin {

    @Shadow
    private static StateDefinition<Block, BlockState> createItemFrameFakeState() {
        return null;
    }

    static {
        MixinGlobals.CLEAR_ITEM_FRAME = BlockStatesManagersMixin.createItemFrameFakeState();
    }

    @Inject(method = "definitionLocationToBlockStateMapper", at = @At("TAIL"))
    private static void createIdToManagerMapper(CallbackInfoReturnable<Function<Identifier, StateDefinition<Block, BlockState>>> cir, @Local(name = "result") Map<Identifier, StateDefinition<Block, BlockState>> map) {
        map.put(MixinGlobals.CLEAR_ITEM_FRAME_ID, MixinGlobals.CLEAR_ITEM_FRAME);
    }
}
