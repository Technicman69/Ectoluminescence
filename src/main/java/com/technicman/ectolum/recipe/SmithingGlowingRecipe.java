package com.technicman.ectolum.recipe;

import com.technicman.ectolum.component.EchoingEffects;
import com.technicman.ectolum.component.ModComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;

public class SmithingGlowingRecipe extends SmithingEchoingRecipe {

    public SmithingGlowingRecipe(Recipe.CommonInfo commonInfo, Ingredient template, Ingredient base, Ingredient addition) {
        super(commonInfo, template, base, addition);
    }

    @Override
    public @NonNull ItemStack assemble(SmithingRecipeInput smithingRecipeInput) {
        ItemStack itemStack = smithingRecipeInput.base();
            EchoingEffects echoingEffects = new EchoingEffects(List.of(new EchoingEffects.EchoingLayer(
                    Optional.of(true),
                    Optional.empty(),
                    Optional.empty()
            )));
            ItemStack newStack = itemStack.copyWithCount(1);
            newStack.set(ModComponents.ECHOING_EFFECTS, echoingEffects);
            return newStack;
    }

    @Override
    public @NonNull RecipeSerializer<? extends SimpleSmithingRecipe> getSerializer() {
        return ModRecipes.SMITHING_GLOWING_RECIPE;
    }
}

