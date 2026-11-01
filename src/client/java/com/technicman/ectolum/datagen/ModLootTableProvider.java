package com.technicman.ectolum.datagen;

import com.technicman.ectolum.block.ModBlocks;
import com.technicman.ectolum.util.BlockDyeMap;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;
import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        dropSelf(ModBlocks.GLOW_INK_TORCH);

        BlockDyeMap.GLOWING_WOOL.values().forEach(this::addDrop);
        BlockDyeMap.GLOWING_CARPET.values().forEach(this::addDrop);
        BlockDyeMap.GLOWING_BED.values().forEach(block -> addDrop(block, dropsWithProperty(block, BedBlock.PART, BedPart.HEAD)));
        BlockDyeMap.GLOWING_BANNER.values().forEach(block -> addDrop(block, bannerDrops(block)));
    }
}
