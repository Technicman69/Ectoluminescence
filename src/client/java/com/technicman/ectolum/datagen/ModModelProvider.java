package com.technicman.ectolum.datagen;

import com.technicman.ectolum.util.BlockDyeMap;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.client.data.*;
import net.minecraft.client.render.item.model.special.BedModelRenderer;
import net.minecraft.client.render.model.json.ModelVariant;
import net.minecraft.client.render.model.json.WeightedVariant;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.WeightedPool;
import org.jetbrains.annotations.NotNull;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(@NotNull BlockStateModelGenerator blockStateModelGenerator) {
        BlockDyeMap.GLOWING_WOOL.forEach((color, block) ->
                registerParentedWithItem(blockStateModelGenerator, BlockDyeMap.WOOL.get(color), block));
        BlockDyeMap.GLOWING_CARPET.forEach((color, block) ->
                registerParentedWithItem(blockStateModelGenerator, BlockDyeMap.CARPET.get(color), block));
        BlockDyeMap.GLOWING_BANNER.forEach((color, block) ->
                registerParentedWithItem(blockStateModelGenerator, BlockDyeMap.BANNER.get(color), block));
        BlockDyeMap.GLOWING_WALL_BANNER.forEach((color, block) ->
                registerParented(blockStateModelGenerator, BlockDyeMap.WALL_BANNER.get(color), block));

        BlockDyeMap.GLOWING_BED.forEach((color, block) -> {
            Block parent = BlockDyeMap.BED.get(color);
            registerParented(blockStateModelGenerator, parent, block);
            Item item = block.asItem();
            Identifier base = ModelIds.getItemModelId(parent.asItem());
            blockStateModelGenerator.itemModelOutput.accept(item, ItemModels.special(base, new BedModelRenderer.Unbaked(color)));
        });
    }

    public void registerParentedWithItem(@NotNull BlockStateModelGenerator blockStateModelGenerator, Block parent, Block block) {
        registerParented(blockStateModelGenerator, parent, block);
        blockStateModelGenerator.registerParentedItemModel(block, ModelIds.getItemModelId(parent.asItem()));
    }

    /**
     * Registers block_state same as provided parent block
     */
    public void registerParented(@NotNull BlockStateModelGenerator blockStateModelGenerator, Block parent, Block block) {
        blockStateModelGenerator.blockStateCollector.accept(
                VariantsBlockModelDefinitionCreator.of(
                        block,
                        new WeightedVariant(WeightedPool.of(new ModelVariant(ModelIds.getBlockModelId(parent))))
                )
        );
    }

    @Override
    public void generateItemModels(@NotNull ItemModelGenerator itemModelGenerator) {

    }
}
