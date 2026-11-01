package com.technicman.integration.jei;

import com.technicman.ectolum.recipe.SmithingEchoingRecipe;
import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.smithing.ISmithingCategoryExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SmithingEchoingExtension<R extends SmithingEchoingRecipe> implements ISmithingCategoryExtension<@NotNull R> {
    private static final Random RANDOM = new Random();

    private static List<Holder<TrimMaterial>> MATERIALS;
    private static List<Holder<TrimPattern>> PATTERNS;

    public static void reload() {
        Minecraft client = Minecraft.getInstance();
        assert client.level != null;

        HolderLookup.Provider WRAPPER_LOOKUP = client.level.registryAccess();
        MATERIALS = WRAPPER_LOOKUP.lookupOrThrow(Registries.TRIM_MATERIAL).listElements().collect(Collectors.toUnmodifiableList());
        PATTERNS = WRAPPER_LOOKUP.lookupOrThrow(Registries.TRIM_PATTERN).listElements().collect(Collectors.toUnmodifiableList());
    }

    private ArmorTrim getRandomTrim() {
        Holder<TrimMaterial> material = MATERIALS.get(RANDOM.nextInt(MATERIALS.size()));
        Holder<TrimPattern> pattern = PATTERNS.get(RANDOM.nextInt(MATERIALS.size()));
        return new ArmorTrim(material, pattern);
    }

    @Override
    public <T extends IIngredientAcceptor<@NotNull T>> void setTemplate(R recipe, T ingredientAcceptor) {
        recipe.template().ifPresent(ingredientAcceptor::add);
    }

    @Override
    public <T extends IIngredientAcceptor<@NotNull T>> void setBase(R recipe, T ingredientAcceptor) {
        ingredientAcceptor.addItemStacks(recipe.base().getMatchingItems()
                .map(ItemStack::new)
                .peek(stack -> stack.set(DataComponentTypes.TRIM, getRandomTrim()))
                .collect(Collectors.toList())
        );
    }

    @Override
    public <T extends IIngredientAcceptor<@NotNull T>> void setAddition(R recipe, T ingredientAcceptor) {
        recipe.addition().ifPresent(ingredientAcceptor::add);
    }

    @Override
    public void onDisplayedIngredientsUpdate(R recipe, IRecipeSlotDrawable templateSlot, @NotNull IRecipeSlotDrawable baseSlot, @NotNull IRecipeSlotDrawable additionSlot, @NotNull IRecipeSlotDrawable outputSlot, @NotNull IFocusGroup focuses) {
        templateSlot.getDisplayedIngredient().ifPresent(typedTemplate ->
                typedTemplate.getItemStack().flatMap(template -> baseSlot.getDisplayedItemStack()).ifPresent(base ->
                        additionSlot.getDisplayedItemStack().ifPresent(addition -> {

                            SmithingRecipeInput input = new SmithingRecipeInput(typedTemplate.getItemStack().get(), base, addition);
                            ItemStack outputStack = recipe.assemble(input);

                            outputSlot.createDisplayOverrides().add(outputStack);
                        })
                )
        );
    }
}
