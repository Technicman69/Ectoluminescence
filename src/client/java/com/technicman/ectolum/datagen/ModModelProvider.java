package com.technicman.ectolum.datagen;

import com.technicman.ectolum.util.BlockDyeMap;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.*;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.resources.Identifier;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(@NotNull BlockModelGenerators blockStateModelGenerator) {
        BlockDyeMap.GLOWING_WOOL.forEach((color, block) ->
                registerParentedWithItem(blockStateModelGenerator, BlockDyeMap.WOOL.get(color), block));
        BlockDyeMap.GLOWING_CARPET.forEach((color, block) ->
                registerParentedWithItem(blockStateModelGenerator, BlockDyeMap.CARPET.get(color), block));
        BlockDyeMap.GLOWING_BANNER.forEach((color, block) -> {
            registerParented(blockStateModelGenerator, Identifier.of("block/banner"), block);
            Item item = block.asItem();
            Identifier base = Identifier.of("item/template_banner");
            blockStateModelGenerator.itemModelOutput.accept(item, ItemModels.special(base, new BannerModelRenderer.Unbaked(color)));
        });
        BlockDyeMap.GLOWING_WALL_BANNER.forEach((color, block) -> {
            registerParented(blockStateModelGenerator, Identifier.of("block/banner"), block);
        });

        BlockDyeMap.GLOWING_BED.forEach((color, block) -> {
            Block parent = BlockDyeMap.BED.get(color);
            registerParented(blockStateModelGenerator, Identifier.of("block/bed"), block);
            Item item = block.asItem();
            Identifier base = ModelIds.getItemModelId(parent.asItem());
            blockStateModelGenerator.itemModelOutput.accept(item, ItemModels.special(base, new BedModelRenderer.Unbaked(color)));
        });
    }

    public void registerParentedWithItem(@NotNull BlockModelGenerators blockStateModelGenerator, Block parent, Block block) {
        registerParented(blockStateModelGenerator, ModelLocationUtils.getModelLocation(parent), block);
        blockStateModelGenerator.registerSimpleItemModel(block, ModelLocationUtils.getModelLocation(parent));
    }

    /**
     * Registers block_state same as provided parent block
     */
    public void registerParented(@NotNull BlockModelGenerators blockStateModelGenerator, Identifier parent, Block block) {
        blockStateModelGenerator.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(
                        block,
                        new MultiVariant(WeightedList.of(new Variant(parent)))
                )
        );
    }

    @Override
    public void generateItemModels(@NotNull ItemModelGenerators itemModelGenerator) {

    }
}
