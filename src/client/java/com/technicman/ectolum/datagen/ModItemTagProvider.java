package com.technicman.ectolum.datagen;

import com.technicman.ectolum.util.BlockDyeMap;
import com.technicman.ectolum.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagsProvider.ItemTagsProvider {

    public ModItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider registries) {
        valueLookupBuilder(ModTags.GLOWING_WOOL_ITEMS).addAll(BlockDyeMap.GLOWING_WOOL.values().stream().map(Block::asItem));
        valueLookupBuilder(ModTags.GLOWING_WOOL_CARPET_ITEMS).addAll(BlockDyeMap.GLOWING_CARPET.values().stream().map(Block::asItem));
        valueLookupBuilder(ModTags.GLOWING_BED_ITEMS).addAll(BlockDyeMap.GLOWING_BED.values().stream().map(Block::asItem));
        valueLookupBuilder(ModTags.GLOWING_BANNER_ITEMS).addAll(BlockDyeMap.GLOWING_BANNER.values().stream().map(Block::asItem));
    }
}
