package com.technicman.ectolum.datagen;

import com.technicman.ectolum.block.ModBlocks;
import com.technicman.ectolum.util.BlockDyeMap;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.BedBlock;
import net.minecraft.block.enums.BedPart;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.GLOW_INK_TORCH);

        BlockDyeMap.GLOWING_WOOL.forEach((color, block) -> addDrop(block));
        BlockDyeMap.GLOWING_CARPET.forEach((color, block) -> addDrop(block));
        BlockDyeMap.GLOWING_BED.forEach((color, block) -> addDrop(block, dropsWithProperty(block, BedBlock.PART, BedPart.HEAD)));
        BlockDyeMap.GLOWING_BANNER.forEach((color, block) -> addDrop(block, bannerDrops(block)));
    }
}
