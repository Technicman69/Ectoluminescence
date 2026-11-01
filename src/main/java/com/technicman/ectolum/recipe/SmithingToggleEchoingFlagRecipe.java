package com.technicman.ectolum.recipe;

import com.technicman.ectolum.component.EchoingLayer;
import com.technicman.ectolum.component.ModComponents;
import com.technicman.ectolum.util.EchoingKeyframe;
import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import org.jspecify.annotations.NonNull;

public abstract class SmithingToggleEchoingFlagRecipe extends SmithingEchoingRecipe {

    public SmithingToggleEchoingFlagRecipe(Recipe.CommonInfo commonInfo, Ingredient template, Ingredient base, Ingredient addition) {
        super(commonInfo, template, base, addition);
    }

    public abstract EchoingLayer computeEchoingLayer(EchoingKeyframe keyFrame);

    @Override
    public @NonNull ItemStack assemble(SmithingRecipeInput smithingRecipeInput) {
        ItemStack itemStack = smithingRecipeInput.base();
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
}

