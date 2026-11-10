package com.technicman.ectolum.recipe;

import com.technicman.ectolum.component.EchoingEffects;
import com.technicman.ectolum.component.ModComponents;
import com.technicman.ectolum.util.EchoingKeyframe;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import org.jspecify.annotations.NonNull;

public class SmithingEchoingPigmentRecipe extends SmithingEchoingRecipe {

    public SmithingEchoingPigmentRecipe(Recipe.CommonInfo commonInfo, Ingredient template, Ingredient base, Ingredient addition) {
        super(commonInfo, template, base, addition);
    }

    @Override
    public @NonNull ItemStack assemble(SmithingRecipeInput smithingRecipeInput) {
        ItemStack itemStack = smithingRecipeInput.base();
            Holder<TrimMaterial> material = smithingRecipeInput.addition().get(DataComponents.PROVIDES_TRIM_MATERIAL);
            if (material != null) {

                EchoingKeyframe last = EchoingKeyframe.buildLast(itemStack);
                if (last.material().equals(material)) {
                    return ItemStack.EMPTY;
                }

                EchoingEffects echoingEffects;
                try {
                    echoingEffects = itemStack.getOrDefault(ModComponents.ECHOING_EFFECTS, EchoingEffects.empty())
                            .pigment(material);
                } catch (ArrayIndexOutOfBoundsException e) {
                    // Echoing layer limit exceeded
                    return ItemStack.EMPTY;
                }

                ItemStack newStack = itemStack.copyWithCount(1);
                newStack.set(ModComponents.ECHOING_EFFECTS, echoingEffects);
                return newStack;
            }
        return ItemStack.EMPTY;
    }

    @Override
    public @NonNull RecipeSerializer<? extends SimpleSmithingRecipe> getSerializer() {
        return ModRecipes.SMITHING_ECHOING_PIGMENT_RECIPE;
    }
}

