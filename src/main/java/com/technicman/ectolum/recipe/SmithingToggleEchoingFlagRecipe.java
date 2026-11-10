package com.technicman.ectolum.recipe;

import com.technicman.ectolum.component.EchoingEffects;
import com.technicman.ectolum.component.ModComponents;
import com.technicman.ectolum.util.EchoingKeyframe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import org.jspecify.annotations.NonNull;

public abstract class SmithingToggleEchoingFlagRecipe extends SmithingEchoingRecipe {

    public SmithingToggleEchoingFlagRecipe(Recipe.CommonInfo commonInfo, Ingredient template, Ingredient base, Ingredient addition) {
        super(commonInfo, template, base, addition);
    }

    public abstract EchoingEffects addEchoingLayer(EchoingEffects echoingEffects, EchoingKeyframe keyFrame);

    @Override
    public @NonNull ItemStack assemble(SmithingRecipeInput smithingRecipeInput) {
        ItemStack itemStack = smithingRecipeInput.base();
        EchoingKeyframe last = EchoingKeyframe.buildLast(itemStack);

        EchoingEffects echoingEffects;
        try {
            echoingEffects = addEchoingLayer(
                    itemStack.getOrDefault(ModComponents.ECHOING_EFFECTS, EchoingEffects.empty()),
                    last);
        } catch (ArrayIndexOutOfBoundsException e) {
            // Echoing layer limit exceeded
            return ItemStack.EMPTY;
        }

        ItemStack newStack = itemStack.copyWithCount(1);
        newStack.set(ModComponents.ECHOING_EFFECTS, echoingEffects);
        return newStack;
    }
}

