package com.technicman.ectolum.recipe;

import com.technicman.ectolum.component.ModComponents;
import com.technicman.ectolum.util.EchoingKeyframe;
import com.technicman.ectolum.util.EchoingLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.input.SmithingRecipeInput;
import net.minecraft.registry.RegistryWrapper;

import java.util.List;

public abstract class SmithingToggleEchoingFlagRecipe extends SmithingEchoingRecipe {

    public SmithingToggleEchoingFlagRecipe(Ingredient template, Ingredient base, Ingredient addition) {
        super(template, base, addition);
    }

    public abstract EchoingLayer computeEchoingLayer(EchoingKeyframe keyFrame);

    public ItemStack craft(SmithingRecipeInput smithingRecipeInput, RegistryWrapper.WrapperLookup wrapperLookup) {
        ItemStack itemStack = smithingRecipeInput.base();
        if (testBase(itemStack)) {
            EchoingKeyframe last = EchoingKeyframe.buildLast(itemStack);

            List<EchoingLayer> echoingLayers = copyEchoingLayers(itemStack);
            if (echoingLayers == null) {
                // Echoing layer limit reached
                return ItemStack.EMPTY;
            }

            echoingLayers.add(computeEchoingLayer(last));

            ItemStack newStack = itemStack.copyWithCount(1);
            newStack.set(ModComponents.ECHOING_LAYERS, echoingLayers);
            return newStack;
        }

        return ItemStack.EMPTY;
    }
}

