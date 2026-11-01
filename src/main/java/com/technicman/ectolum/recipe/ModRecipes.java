package com.technicman.ectolum.recipe;

import com.technicman.ectolum.Ectoluminescence;
import net.fabricmc.fabric.api.recipe.v1.sync.RecipeSynchronization;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ModRecipes {
    public static RecipeSerializer<SmithingEchoingFadeRecipe> SMITHING_ECHOING_FADE_RECIPE = register("smithing_echoing_fade", SmithingEchoingRecipe.serializer(SmithingEchoingFadeRecipe::new));
    public static RecipeSerializer<SmithingEchoingPigmentRecipe> SMITHING_ECHOING_PIGMENT_RECIPE = register("smithing_echoing_pigment", SmithingEchoingRecipe.serializer(SmithingEchoingPigmentRecipe::new));
    public static RecipeSerializer<SmithingEchoingTwinkleRecipe> SMITHING_ECHOING_TWINKLE_RECIPE = register("smithing_echoing_twinkle", SmithingEchoingRecipe.serializer(SmithingEchoingTwinkleRecipe::new));
    public static RecipeSerializer<SmithingGlowingRecipe> SMITHING_GLOWING_RECIPE = register("smithing_glowing", SmithingEchoingRecipe.serializer(SmithingGlowingRecipe::new));

    static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String name, S serializer) {
        // JEI requires recipes to be synced with client
        if (FabricLoader.getInstance().isModLoaded("jei"))
            RecipeSynchronization.synchronizeRecipeSerializer(serializer);

        return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Ectoluminescence.id(name), serializer);
    }

    public static void onInitialize() {}
}
