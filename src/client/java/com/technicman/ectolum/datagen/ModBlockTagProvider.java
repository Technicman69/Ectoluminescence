package com.technicman.ectolum.datagen;

import com.technicman.ectolum.util.BlockDyeMap;
import com.technicman.ectolum.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {

    public ModBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider registries) {
        valueLookupBuilder(ModTags.GLOWING_WOOL).addAll(BlockDyeMap.GLOWING_WOOL.values());
        valueLookupBuilder(ModTags.GLOWING_WOOL_CARPETS).addAll(BlockDyeMap.GLOWING_CARPET.values());
        valueLookupBuilder(ModTags.GLOWING_BEDS).addAll(BlockDyeMap.GLOWING_BED.values());
        valueLookupBuilder(ModTags.GLOWING_BANNERS).addAll(BlockDyeMap.GLOWING_BANNER.values()).addAll(BlockDyeMap.GLOWING_WALL_BANNER.values());
        valueLookupBuilder(ModTags.GLOWING_WALL_BANNERS).addAll(BlockDyeMap.GLOWING_WALL_BANNER.values());
    }
}
