package com.technicman.ectolum.datagen;

import com.mojang.math.Transformation;
import com.technicman.ectolum.util.BlockDyeMap;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BedRenderer;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.special.BannerSpecialRenderer;
import net.minecraft.client.renderer.special.BedSpecialRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BedPart;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class ModModelProvider extends FabricModelProvider {

    public ModModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(@NotNull BlockModelGenerators blockStateModelGenerator) {
        BlockDyeMap.GLOWING_WOOL.forEach((color, block) ->
                registerParentedWithItem(blockStateModelGenerator, BlockDyeMap.WOOL.get(color), block));
        BlockDyeMap.GLOWING_CARPET.forEach((color, block) ->
                registerParentedWithItem(blockStateModelGenerator, BlockDyeMap.CARPET.get(color), block));
        BlockDyeMap.GLOWING_BANNER.forEach((color, block) -> {
            registerParented(blockStateModelGenerator, ModelLocationUtils.decorateBlockModelLocation("bed"), block);
            Item item = block.asItem();
            Identifier itemModel = ModelLocationUtils.decorateItemModelLocation("template_banner");
            blockStateModelGenerator.itemModelOutput.accept(item, ItemModelUtils.specialModel(itemModel, BannerRenderer.TRANSFORMATIONS.freeTransformations(0), new BannerSpecialRenderer.Unbaked(color, BannerBlock.AttachmentType.GROUND)));
        });
        BlockDyeMap.GLOWING_WALL_BANNER.forEach((color, block) -> {
            registerParented(blockStateModelGenerator, ModelLocationUtils.decorateBlockModelLocation("bed"), block);
        });

        BlockDyeMap.GLOWING_BED.forEach((color, block) -> {
            registerParented(blockStateModelGenerator, ModelLocationUtils.decorateBlockModelLocation("bed"), block);
            Item item = block.asItem();
            Item parent = BlockDyeMap.BED.get(color).asItem();
            Identifier parentModel = ModelLocationUtils.getModelLocation(parent);
            Transformation headTransformation = BedRenderer.modelTransform(Direction.SOUTH);
            ItemModel.Unbaked headPart = ItemModelUtils.specialModel(parentModel, headTransformation, new BedSpecialRenderer.Unbaked(color, BedPart.HEAD));
            Transformation footTransformation = (new Transformation(new Vector3f(0.0F, 0.0F, -1.0F), null, null, null)).compose(headTransformation);
            ItemModel.Unbaked footPart = ItemModelUtils.specialModel(parentModel, footTransformation, new BedSpecialRenderer.Unbaked(color, BedPart.FOOT));
            blockStateModelGenerator.itemModelOutput.accept(item, ItemModelUtils.composite(headPart, footPart));
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
