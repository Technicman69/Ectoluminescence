package com.technicman.ectolum.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.technicman.ectolum.util.MixinGlobals;
import net.minecraft.client.resources.model.BlockStateDefinitions;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockStateDefinitions.class)
public class BlockStateDefinitionsMixin {
    @ModifyExpressionValue(
            method = "getItemFrameFakeState",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/resources/model/BlockStateDefinitions;ITEM_FRAME_FAKE_DEFINITION:Lnet/minecraft/world/level/block/state/StateDefinition;",
                    opcode = Opcodes.GETSTATIC))
    private static StateDefinition<Block, BlockState> swapWithClearItemFrame(StateDefinition<Block, BlockState> original) {
        return MixinGlobals.storedClearFlag ? MixinGlobals.CLEAR_ITEM_FRAME : original;
    }
}
