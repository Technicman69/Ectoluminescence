package com.technicman.ectolum.datagen;

import com.technicman.ectolum.util.BlockDyeMap;
import com.technicman.ectolum.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.@NotNull WrapperLookup registries) {
        valueLookupBuilder(ModTags.GLOWING_WOOL).add(BlockDyeMap.GLOWING_WOOL.values());
        valueLookupBuilder(ModTags.GLOWING_WOOL_CARPETS).add(BlockDyeMap.GLOWING_CARPET.values());
    }
}
