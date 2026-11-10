package com.technicman.ectolum.recipe;

import com.technicman.ectolum.component.EchoingEffects;
import com.technicman.ectolum.util.EchoingKeyframe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleSmithingRecipe;
import org.jspecify.annotations.NonNull;

public class SmithingEchoingTwinkleRecipe extends SmithingToggleEchoingFlagRecipe {

    public SmithingEchoingTwinkleRecipe(Recipe.CommonInfo commonInfo, Ingredient template, Ingredient base, Ingredient addition) {
        super(commonInfo, template, base, addition);
    }

    @Override
    public EchoingEffects addEchoingLayer(EchoingEffects echoingEffects, EchoingKeyframe keyFrame) {
        return echoingEffects.twinkle(!keyFrame.glowing());
    }

    @Override
    public @NonNull RecipeSerializer<? extends SimpleSmithingRecipe> getSerializer() {
        return ModRecipes.SMITHING_ECHOING_TWINKLE_RECIPE;
    }
}

