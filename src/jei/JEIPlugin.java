package com.technicman.ectolum.plugin.jei;

import com.technicman.ectolum.Ectoluminescence;
import com.technicman.ectolum.recipe.ModRecipes;
import com.technicman.ectolum.recipe.SmithingEchoingMatterialRecipe;
import com.technicman.ectolum.recipe.SmithingMergeNbtRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.extensions.vanilla.smithing.ISmithingCategoryExtension;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.data.server.recipe.SmithingTrimRecipeJsonBuilder;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    private static final Identifier ID = new Identifier(Ectoluminescence.MOD_ID, "jei_plugin");
//    private IRecipeManager recipes;
//
//    @Override
//    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
//        recipes = jeiRuntime.getRecipeManager();
//    }
//
//    private <T extends Recipe<Inventory>> List<T> recipesFor(RecipeType<?> recipeType, RecipeSerializer<T> recipeSerializer) {
//        return recipes.createRecipeLookup(recipeType).get().filter(recipe -> recipe instanceof T).toList();
//    }
    @Override
    public @NotNull Identifier getPluginUid() {
        return ID;
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        IModPlugin.super.registerVanillaCategoryExtensions(registration);
        registration.getSmithingCategory().addExtension(SmithingMergeNbtRecipe.class, new EctolumSmithingCategoryExtension<>());
        registration.getSmithingCategory().addExtension(SmithingEchoingMatterialRecipe.class, new EctolumSmithingCategoryExtension<>());
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        IModPlugin.super.registerRecipes(registration);
    }
}
