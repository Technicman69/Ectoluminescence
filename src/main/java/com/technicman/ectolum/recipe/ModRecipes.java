package com.technicman.ectolum.recipe;

import com.technicman.ectolum.Ectoluminescence;
import net.fabricmc.fabric.api.recipe.v1.sync.RecipeSynchronization;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModRecipes {
    public static RecipeSerializer<SmithingEchoingFadeRecipe> SMITHING_ECHOING_FADE_RECIPE = register("smithing_echoing_fade", new SmithingEchoingRecipe.Serializer<>(SmithingEchoingFadeRecipe::new));
    public static RecipeSerializer<SmithingEchoingPigmentRecipe> SMITHING_ECHOING_PIGMENT_RECIPE = register("smithing_echoing_pigment", new SmithingEchoingRecipe.Serializer<>(SmithingEchoingPigmentRecipe::new));
    public static RecipeSerializer<SmithingEchoingTwinkleRecipe> SMITHING_ECHOING_TWINKLE_RECIPE = register("smithing_echoing_twinkle", new SmithingEchoingRecipe.Serializer<>(SmithingEchoingTwinkleRecipe::new));
    public static RecipeSerializer<SmithingGlowingRecipe> SMITHING_GLOWING_RECIPE = register("smithing_glowing", new SmithingEchoingRecipe.Serializer<>(SmithingGlowingRecipe::new));

    static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String name, S serializer) {
        // JEI requires recipes to be synced with client
        if (FabricLoader.getInstance().isModLoaded("jei"))
            RecipeSynchronization.synchronizeRecipeSerializer(serializer);

        return Registry.register(Registries.RECIPE_SERIALIZER, Ectoluminescence.id(name), serializer);
    }

    public static void onInitialize() {}
}
