package com.technicman.ectolum.datagen;

import com.technicman.ectolum.util.BlockDyeMap;
import com.technicman.ectolum.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.@NotNull WrapperLookup registries) {
        valueLookupBuilder(ModTags.GLOWING_WOOL_ITEMS).add(BlockDyeMap.GLOWING_WOOL.values().stream().map(Block::asItem));
        valueLookupBuilder(ModTags.GLOWING_WOOL_CARPET_ITEMS).add(BlockDyeMap.GLOWING_CARPET.values().stream().map(Block::asItem));
        valueLookupBuilder(ModTags.GLOWING_BED_ITEMS).add(BlockDyeMap.GLOWING_BED.values().stream().map(Block::asItem));
        valueLookupBuilder(ModTags.GLOWING_BANNER_ITEMS).add(BlockDyeMap.GLOWING_BANNER.values().stream().map(Block::asItem));
    }
}
