package com.technicman.ectolum.datagen;

import com.technicman.ectolum.Ectoluminescence;
import com.technicman.ectolum.item.ModItems;
import com.technicman.ectolum.util.BlockDyeMap;
import com.technicman.ectolum.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Block;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.DyeColor;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected @NotNull RecipeGenerator getRecipeGenerator(RegistryWrapper.@NotNull WrapperLookup registries, @NotNull RecipeExporter exporter) {
        return new RecipeGenerator(registries, exporter) {
            @Override
            public void generate() {
                createShaped(RecipeCategory.MISC, ModItems.GLOW_INK_TORCH_ITEM, 4)
                        .criterion(hasItem(Items.GLOW_INK_SAC), conditionsFromItem(Items.GLOW_INK_SAC))
                        .input('g', Items.GLOW_INK_SAC)
                        .input('/', Items.STICK)
                        .pattern("g")
                        .pattern("/")
                        .offerTo(exporter);
                createShapeless(RecipeCategory.DECORATIONS, ModItems.CLEAR_ITEM_FRAME)
                        .criterion(hasItem(Items.PHANTOM_MEMBRANE), conditionsFromItem(Items.PHANTOM_MEMBRANE))
                        .input(Items.ITEM_FRAME)
                        .input(Items.PHANTOM_MEMBRANE)
                        .offerTo(exporter);

                // Regular wool from dying
                createDyeRecipe(BlockDyeMap.WOOL, ModTags.GLOWING_WOOL_ITEMS, RecipeCategory.BUILDING_BLOCKS, "dye_glowing_wool");
                // Glowing wool from regular ones
                createLightRecipe(BlockDyeMap.GLOWING_WOOL, BlockDyeMap.WOOL, RecipeCategory.BUILDING_BLOCKS, "light_wool");


                // Crafting glowing carpets
                BlockDyeMap.GLOWING_CARPET.forEach(((color, block) -> {
                    ItemConvertible wool = BlockDyeMap.GLOWING_WOOL.get(color);
                    createShaped(RecipeCategory.DECORATIONS, block, 3)
                            .criterion(hasItem(wool), conditionsFromItem(wool))
                            .group("glowing_carpet")
                            .input('w', wool)
                            .pattern("ww")
                            .offerTo(exporter);
                }));
                // Regular carpet from dying
                createDyeRecipe(BlockDyeMap.CARPET, ModTags.GLOWING_WOOL_CARPET_ITEMS, RecipeCategory.MISC, "dye_glowing_wool_carpet");
                // Glowing carpets from regular ones
                createLightRecipe(BlockDyeMap.GLOWING_CARPET, BlockDyeMap.CARPET, RecipeCategory.MISC, "light_wool_carpet");


                // Crafting glowing beds
                BlockDyeMap.GLOWING_BED.forEach(((color, block) -> {
                    ItemConvertible wool = BlockDyeMap.GLOWING_WOOL.get(color);
                    createShaped(RecipeCategory.DECORATIONS, block)
                            .criterion(hasItem(wool), conditionsFromItem(wool))
                            .group("glowing_bed")
                            .input('p', ItemTags.PLANKS)
                            .input('w', wool)
                            .pattern("www")
                            .pattern("ppp")
                            .offerTo(exporter);
                }));
                // Regular bed from dying
                createDyeRecipe(BlockDyeMap.BED, ModTags.GLOWING_BED_ITEMS, RecipeCategory.MISC, "dye_glowing_bed");
                // Glowing beds from regular ones
                createLightRecipe(BlockDyeMap.GLOWING_BED, BlockDyeMap.BED, RecipeCategory.MISC, "light_bed");


                // Crafting glowing banners
                BlockDyeMap.GLOWING_BANNER.forEach(((color, block) -> {
                    ItemConvertible wool = BlockDyeMap.GLOWING_WOOL.get(color);
                    createShaped(RecipeCategory.DECORATIONS, block)
                            .criterion(hasItem(wool), conditionsFromItem(wool))
                            .group("glowing_banner")
                            .input('/', Items.STICK)
                            .input('w', wool)
                            .pattern("www")
                            .pattern("www")
                            .pattern(" / ")
                            .offerTo(exporter);
                }));
            }

            private void createDyeRecipe(EnumMap<DyeColor, Block> blockDyeMap, TagKey<Item> glowingBlocks, RecipeCategory category, String group) {
                blockDyeMap.forEach((color, block) -> {
                    createShapeless(category, block)
                            .criterion("has_needed_dye", conditionsFromItem(DyeItem.byColor(color)))
                            .group(group)
                            .input(DyeItem.byColor(color))
                            .input(glowingBlocks)
                            .offerTo(exporter, prefixedKey(block, "dye_glowing_"));
                });
            }

            private void createLightRecipe(EnumMap<DyeColor, Block> glowingBlockDyeMap, EnumMap<DyeColor, Block> regularBlockDyeMap, RecipeCategory category, String group) {
                glowingBlockDyeMap.forEach((color, block) -> {
                    Block regular = regularBlockDyeMap.get(color);
                    createShapeless(category, block)
                            .criterion("has_glow_ink_sac", conditionsFromItem(Items.GLOW_INK_SAC))
                            .group(group)
                            .input(Items.GLOW_INK_SAC)
                            .input(regular)
                            .offerTo(exporter, prefixedKey(regular, "light_"));
                });
            }
        };
    }

    private static RegistryKey<Recipe<?>> prefixedKey(Block block, String prefix) {
        return RegistryKey.of(RegistryKeys.RECIPE, Ectoluminescence.id(Registries.BLOCK.getId(block).getPath()).withPrefixedPath(prefix));
    }

    @Override
    public String getName() {
        return "Ectoluminescence recipes";
    }
}
