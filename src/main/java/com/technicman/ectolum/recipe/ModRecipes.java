package com.technicman.ectolum.recipe;

import com.technicman.ectolum.Ectoluminescence;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {
    public static SmithingModifyNbtRecipe.Serializer SMITHING_MERGE_NBT_RECIPE = register("smithing_merge_nbt", new SmithingModifyNbtRecipe.Serializer());
    public static SmithingEchoingMatterialRecipe.Serializer SMITHING_ECHOING_MATERIAL_RECIPE = register("smithing_echoing_material_recipe", new SmithingEchoingMatterialRecipe.Serializer());
    public static SmithingToggleEchoingFlagRecipe.Serializer SMITHING_TOGGLE_ECHOING_FLAG_RECIPE = register("smithing_toggle_echoing_flag_recipe", new SmithingToggleEchoingFlagRecipe.Serializer());

    static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String name, S serializer) {
        Identifier id = new Identifier(Ectoluminescence.MOD_ID, name);
        return Registry.register(Registries.RECIPE_SERIALIZER, id, serializer);
    }

    public static void onInitialize() {}
}
