package com.technicman.ectolum.recipe;

import com.technicman.ectolum.component.EchoingLayer;
import com.technicman.ectolum.component.ModComponents;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.jspecify.annotations.NonNull;

public class SmithingGlowingRecipe extends SmithingEchoingRecipe {

    public SmithingGlowingRecipe(Recipe.CommonInfo commonInfo, Ingredient template, Ingredient base, Ingredient addition) {
        super(commonInfo, template, base, addition);
    }

    @Override
    public @NonNull ItemStack assemble(SmithingRecipeInput smithingRecipeInput) {
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
    public @NonNull RecipeSerializer<? extends SimpleSmithingRecipe> getSerializer() {
        return ModRecipes.SMITHING_GLOWING_RECIPE;
    }
}

