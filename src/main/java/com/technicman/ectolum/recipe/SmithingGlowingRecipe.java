package com.technicman.ectolum.recipe;

import com.technicman.ectolum.component.EchoingLayer;
import com.technicman.ectolum.component.ModComponents;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.recipe.input.SmithingRecipeInput;
import net.minecraft.registry.RegistryWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SmithingGlowingRecipe extends SmithingEchoingRecipe {

    public SmithingGlowingRecipe(Ingredient template, Ingredient base, Ingredient addition) {
        super(template, base, addition);
    }

    public ItemStack craft(SmithingRecipeInput smithingRecipeInput, RegistryWrapper.WrapperLookup wrapperLookup) {
        ItemStack itemStack = smithingRecipeInput.base();
//        if (testBase(itemStack)) {
            List<EchoingLayer> echoingLayers = new ArrayList<>();
            echoingLayers.add(new EchoingLayer(
                    Optional.of(true),
                    Optional.empty(),
                    Optional.empty()
            ));
            ItemStack newStack = itemStack.copyWithCount(1);
            newStack.set(ModComponents.ECHOING_LAYERS, echoingLayers);
            return newStack;
//        }
//
//        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<? extends SmithingRecipe> getSerializer() {
        return ModRecipes.SMITHING_GLOWING_RECIPE;
    }
}

