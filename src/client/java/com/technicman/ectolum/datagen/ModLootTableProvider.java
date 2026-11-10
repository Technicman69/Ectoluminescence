package com.technicman.ectolum.datagen;

import com.technicman.ectolum.block.ModBlocks;
import com.technicman.ectolum.util.BlockDyeMap;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.properties.BedPart;

import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricBlockLootSubProvider {

    public ModLootTableProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(packOutput, registriesFuture);
    }

    @Override
    public void generate() {
        dropSelf(ModBlocks.GLOW_INK_TORCH);

        BlockDyeMap.GLOWING_WOOL.values().forEach(this::dropSelf);
        BlockDyeMap.GLOWING_CARPET.values().forEach(this::dropSelf);
        BlockDyeMap.GLOWING_BED.values().forEach(block -> createSinglePropConditionTable(block, BedBlock.PART, BedPart.HEAD));
        BlockDyeMap.GLOWING_BANNER.values().forEach(block -> add(block, createBannerDrop(block)));
    }
}
