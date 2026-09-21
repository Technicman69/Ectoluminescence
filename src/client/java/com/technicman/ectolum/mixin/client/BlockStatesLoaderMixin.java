package com.technicman.ectolum.mixin.client;

import com.google.common.collect.ImmutableMap;
import com.technicman.ectolum.Ectoluminescence;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BlockStatesLoader;
import net.minecraft.state.StateManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(BlockStatesLoader.class)
public abstract class BlockStatesLoaderMixin {

    @Mutable
    @Final
    @Shadow
    private static Map<Identifier, StateManager<Block, BlockState>> STATIC_DEFINITIONS;

    @Mutable
    @Final
    @Shadow
    private static StateManager<Block, BlockState> ITEM_FRAME_STATE_MANAGER;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void injected(CallbackInfo ci) {
        STATIC_DEFINITIONS = new ImmutableMap.Builder<Identifier, StateManager<Block, BlockState>>()
                .putAll(STATIC_DEFINITIONS)
                .put(Ectoluminescence.identifier("clear_item_frame"), ITEM_FRAME_STATE_MANAGER)
                .build();
    }
}
