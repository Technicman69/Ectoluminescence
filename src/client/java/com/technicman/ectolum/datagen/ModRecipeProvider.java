package com.technicman.ectolum.datagen;

import com.technicman.ectolum.Ectoluminescence;
import com.technicman.ectolum.item.ModItems;
import com.technicman.ectolum.util.BlockDyeMap;
import com.technicman.ectolum.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.@NotNull Provider registries, @NotNull RecipeOutput exporter) {
        return new RecipeProvider(registries, exporter) {
            @Override
            public void buildRecipes() {
                shaped(RecipeCategory.MISC, ModItems.GLOW_INK_TORCH_ITEM, 4)
                        .criterion(getHasName(Items.GLOW_INK_SAC), has(Items.GLOW_INK_SAC))
                        .input('g', Items.GLOW_INK_SAC)
                        .input('/', Items.STICK)
                        .pattern("g")
                        .pattern("/")
                        .offerTo(output);
                shapeless(RecipeCategory.DECORATIONS, ModItems.CLEAR_ITEM_FRAME)
                        .criterion(getHasName(Items.PHANTOM_MEMBRANE), has(Items.PHANTOM_MEMBRANE))
                        .input(Items.ITEM_FRAME)
                        .input(Items.PHANTOM_MEMBRANE)
                        .offerTo(output);

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
                    shapeless(category, block)
                            .unlockedBy("has_needed_dye", has(DyeItem.byColor(color)))
                            .group(group)
                            .requires(DyeItem.byColor(color))
                            .requires(glowingBlocks)
                            .save(output, prefixedKey(block, "dye_glowing_"));
                });
            }

            private void createLightRecipe(EnumMap<DyeColor, Block> glowingBlockDyeMap, EnumMap<DyeColor, Block> regularBlockDyeMap, RecipeCategory category, String group) {
                glowingBlockDyeMap.forEach((color, block) -> {
                    Block regular = regularBlockDyeMap.get(color);
                    shapeless(category, block)
                            .unlockedBy("has_glow_ink_sac", has(Items.GLOW_INK_SAC))
                            .group(group)
                            .requires(Items.GLOW_INK_SAC)
                            .requires(regular)
                            .save(output, prefixedKey(regular, "light_"));
                });
            }
        };
    }

    private static ResourceKey<Recipe<?>> prefixedKey(Block block, String prefix) {
        return ResourceKey.create(Registries.RECIPE, Ectoluminescence.id(BuiltInRegistries.BLOCK.getKey(block).getPath()).withPrefixedPath(prefix));
    }

    @Override
    public String getName() {
        return "Ectoluminescence recipes";
    }
}
