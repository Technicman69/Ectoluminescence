package com.technicman.ectolum.integration.jei;

import com.technicman.ectolum.Ectoluminescence;
import com.technicman.ectolum.recipe.SmithingEchoingMatterialRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class Plugin implements IModPlugin {
    private static final Identifier ID = new Identifier(Ectoluminescence.MOD_ID, "jei_plugin");

    public Plugin() {
        System.out.println("Initialising JEI recipes");
    }

//    private List<SmithingRecipe> getModRecipes() {
//        return recipeManager.createRecipeLookup(RecipeTypes.SMITHING).get().filter(recipe -> recipe instanceof EctolumRecipe).toList();
//    }
    @Override
    public @NotNull Identifier getPluginUid() {
        return ID;
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        registration.getSmithingCategory().addExtension(SmithingEchoingMatterialRecipe.class, new SmithingEchoingMaterialRecipeExtension());
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
//        System.out.println(getModRecipes());
//        var a =
//                MinecraftClient.getInstance().player.getInventory();
//        MinecraftClient.getInstance().world.getRecipeManager().listAllOfType(RecipeType.SMITHING).stream().filter(recipe -> recipe instanceof EctolumRecipe).toList();
//        var rs = MinecraftClient.getInstance().world.getRecipeManager().listAllOfType(RecipeType.SMITHING).stream().filter(recipe -> recipe instanceof EctolumRecipe).toList();
//        registration.addRecipes(RecipeTypes.SMITHING, rs);

    }
}
