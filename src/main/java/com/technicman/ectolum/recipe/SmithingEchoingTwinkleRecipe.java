package com.technicman.ectolum.recipe;

import com.technicman.ectolum.component.EchoingLayer;
import com.technicman.ectolum.util.EchoingKeyframe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SmithingRecipe;

import java.util.Optional;

public class SmithingEchoingTwinkleRecipe extends SmithingToggleEchoingFlagRecipe {

    public SmithingEchoingTwinkleRecipe(Ingredient template, Ingredient base, Ingredient addition) {
        super(template, base, addition);
    }

    @Override
    public EchoingLayer computeEchoingLayer(EchoingKeyframe keyFrame) {
        return new EchoingLayer(
                Optional.of(!keyFrame.glowing()),
                Optional.empty(),
                Optional.empty()
        );
    }
    @Override
    public RecipeSerializer<? extends SmithingRecipe> getSerializer() {
        return ModRecipes.SMITHING_ECHOING_TWINKLE_RECIPE;
    }
}

