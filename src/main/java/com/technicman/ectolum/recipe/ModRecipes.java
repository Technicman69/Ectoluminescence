package com.technicman.ectolum.recipe;

import com.technicman.ectolum.Ectoluminescence;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {
    public static SmithingEchoingFadeRecipe.Serializer SMITHING_ECHOING_FADE_RECIPE = register("smithing_echoing_fade", new SmithingEchoingFadeRecipe.Serializer());
    public static SmithingEchoingPigmentRecipe.Serializer SMITHING_ECHOING_PIGMENT_RECIPE = register("smithing_echoing_pigment", new SmithingEchoingPigmentRecipe.Serializer());
    public static SmithingEchoingTwinkleRecipe.Serializer SMITHING_ECHOING_TWINKLE_RECIPE = register("smithing_echoing_twinkle", new SmithingEchoingTwinkleRecipe.Serializer());
    public static SmithingGlowingRecipe.Serializer SMITHING_GLOWING_RECIPE = register("smithing_glowing", new SmithingGlowingRecipe.Serializer());

    static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String name, S serializer) {
        Identifier id = Ectoluminescence.identifier(name);
        return Registry.register(Registries.RECIPE_SERIALIZER, id, serializer);
    }

    public static void onInitialize() {}
}
