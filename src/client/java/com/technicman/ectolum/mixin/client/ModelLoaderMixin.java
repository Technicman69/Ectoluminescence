package com.technicman.ectolum.mixin.client;

import com.google.common.collect.ImmutableMap;
import com.technicman.ectolum.Ectoluminescence;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.ModelLoader;
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

@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin {

//    @Mutable
//    @Final
//    @Shadow
//    private static Map<Identifier, StateManager<Block, BlockState>> STATIC_DEFINITIONS;
//
//    @Mutable
//    @Final
//    @Shadow
//    private static StateManager<Block, BlockState> ITEM_FRAME_STATE_FACTORY;
//
//    @Inject(method = "<clinit>", at = @At("TAIL"))
//    private static void injected(CallbackInfo ci) {
//        STATIC_DEFINITIONS = new ImmutableMap.Builder()
//                .putAll(STATIC_DEFINITIONS)
//                .put(new Identifier(Ectoluminescence.MOD_ID, "clear_item_frame"), ITEM_FRAME_STATE_FACTORY)
//                .build();
//    }
}
