package com.technicman.integration.jei;

import com.technicman.ectolum.recipe.SmithingEchoingRecipe;
import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.smithing.ISmithingCategoryExtension;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.*;
import net.minecraft.recipe.input.SmithingRecipeInput;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SmithingEchoingExtension<R extends SmithingEchoingRecipe> implements ISmithingCategoryExtension<R> {
    private static final RegistryWrapper.WrapperLookup WRAPPER_LOOKUP;
    private static final Random RANDOM = new Random();


    public static final List<RegistryEntry<ArmorTrimMaterial>> MATERIALS;
    public static final List<RegistryEntry<ArmorTrimPattern>> PATTERNS;

    static {
        MinecraftClient client = MinecraftClient.getInstance();
        assert client.world != null;

        WRAPPER_LOOKUP = client.world.getRegistryManager();
        MATERIALS = WRAPPER_LOOKUP.getWrapperOrThrow(RegistryKeys.TRIM_MATERIAL).streamEntries().collect(Collectors.toUnmodifiableList());
        PATTERNS = WRAPPER_LOOKUP.getWrapperOrThrow(RegistryKeys.TRIM_PATTERN).streamEntries().collect(Collectors.toUnmodifiableList());
    }

    private ArmorTrim getRandomTrim() {
        RegistryEntry<ArmorTrimMaterial> material = MATERIALS.get(RANDOM.nextInt(MATERIALS.size()));
        RegistryEntry<ArmorTrimPattern> pattern = PATTERNS.get(RANDOM.nextInt(MATERIALS.size()));
        return new ArmorTrim(material, pattern);
    }

    @Override
    public <T extends IIngredientAcceptor<T>> void setTemplate(R recipe, T ingredientAcceptor) {
        ingredientAcceptor.addIngredients(recipe.getTemplate());
    }

    @Override
    public <T extends IIngredientAcceptor<T>> void setBase(R recipe, T ingredientAcceptor) {
        ingredientAcceptor.addItemStacks(Arrays.stream(recipe.getBase().getMatchingStacks())
                .peek((stack) -> {
                    stack.set(DataComponentTypes.TRIM, getRandomTrim());

                }).collect(Collectors.toList())
        );
    }

    @Override
    public <T extends IIngredientAcceptor<T>> void setAddition(R recipe, T ingredientAcceptor) {
        ingredientAcceptor.addIngredients(recipe.getAddition());
    }

    @Override
    public void onDisplayedIngredientsUpdate(R recipe, IRecipeSlotDrawable templateSlot, IRecipeSlotDrawable baseSlot, IRecipeSlotDrawable additionSlot, IRecipeSlotDrawable outputSlot, IFocusGroup focuses) {
        templateSlot.getDisplayedIngredient().ifPresent(typedTemplate ->
                typedTemplate.getItemStack().flatMap(template -> baseSlot.getDisplayedItemStack()).ifPresent(base ->
                        additionSlot.getDisplayedItemStack().ifPresent(addition -> {

                            SmithingRecipeInput input = new SmithingRecipeInput(typedTemplate.getItemStack().get(), base, addition);
                            ItemStack outputStack = recipe.craft(input, WRAPPER_LOOKUP);

                            outputSlot.createDisplayOverrides().addItemStack(outputStack);
                        })));
    }
}
