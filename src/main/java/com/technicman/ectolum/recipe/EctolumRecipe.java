package com.technicman.ectolum.recipe;

import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.SmithingRecipe;

public interface EctolumRecipe extends SmithingRecipe {
    public Ingredient getTemplate();
    public Ingredient getBase();
    public Ingredient getAddition();
}
