package com.technicman.ectolum.plugin.jei;

import com.technicman.ectolum.recipe.EctolumRecipe;
import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.recipe.advanced.IRecipeManagerPluginHelper;
import mezz.jei.api.recipe.category.extensions.vanilla.smithing.ISmithingCategoryExtension;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.SmithingRecipe;

public class EctolumSmithingCategoryExtension<R extends EctolumRecipe> implements ISmithingCategoryExtension<R> {

    @Override
    public <T extends IIngredientAcceptor<T>> void setBase(R recipe, T ingredientAcceptor) {
        Ingredient ingredient = recipe.getBase();
        ingredientAcceptor.addIngredients(ingredient);
    }

    @Override
    public <T extends IIngredientAcceptor<T>> void setAddition(R recipe, T ingredientAcceptor) {
        Ingredient ingredient = recipe.getAddition();
        ingredientAcceptor.addIngredients(ingredient);
    }

    @Override
    public <T extends IIngredientAcceptor<T>> void setTemplate(R recipe, T ingredientAcceptor) {
        Ingredient ingredient = recipe.getTemplate();
        ingredientAcceptor.addIngredients(ingredient);
    }
}