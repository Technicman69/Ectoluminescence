package com.technicman.ectolum.recipe;

import com.technicman.ectolum.component.EchoingLayer;
import com.technicman.ectolum.util.EchoingKeyframe;
import java.util.Optional;

import net.minecraft.world.item.crafting.*;
import org.jspecify.annotations.NonNull;

public class SmithingEchoingTwinkleRecipe extends SmithingToggleEchoingFlagRecipe {

    public SmithingEchoingTwinkleRecipe(Recipe.CommonInfo commonInfo, Ingredient template, Ingredient base, Ingredient addition) {
        super(commonInfo, template, base, addition);
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
    public @NonNull RecipeSerializer<? extends SimpleSmithingRecipe> getSerializer() {
        return ModRecipes.SMITHING_ECHOING_TWINKLE_RECIPE;
    }
}

