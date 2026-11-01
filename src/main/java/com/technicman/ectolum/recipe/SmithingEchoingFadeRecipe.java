package com.technicman.ectolum.recipe;

import com.technicman.ectolum.component.EchoingLayer;
import com.technicman.ectolum.util.EchoingKeyframe;
import java.util.Optional;

import net.minecraft.world.item.crafting.*;
import org.jspecify.annotations.NonNull;

public class SmithingEchoingFadeRecipe extends SmithingToggleEchoingFlagRecipe {

    public SmithingEchoingFadeRecipe(Recipe.CommonInfo commonInfo, Ingredient template, Ingredient base, Ingredient addition) {
        super(commonInfo, template, base, addition);
    }

    @Override
    public EchoingLayer computeEchoingLayer(EchoingKeyframe keyFrame) {
        return new EchoingLayer(
                Optional.empty(),
                Optional.of(!keyFrame.hidden()),
                Optional.empty()
        );
    }
    @Override
    public @NonNull RecipeSerializer<? extends SimpleSmithingRecipe> getSerializer() {
        return ModRecipes.SMITHING_ECHOING_FADE_RECIPE;
    }
}

