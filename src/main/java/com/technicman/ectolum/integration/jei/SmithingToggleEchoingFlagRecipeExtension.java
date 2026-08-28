package com.technicman.ectolum.integration.jei;

import com.technicman.ectolum.recipe.SmithingToggleEchoingFlagRecipe;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public class SmithingToggleEchoingFlagRecipeExtension extends EctolumSmithingExtension<SmithingToggleEchoingFlagRecipe> {
    @Override
    public void onDisplayedIngredientsUpdate(SmithingToggleEchoingFlagRecipe recipe, IRecipeSlotDrawable templateSlot, IRecipeSlotDrawable baseSlot, IRecipeSlotDrawable additionSlot, IRecipeSlotDrawable outputSlot, IFocusGroup focuses) {
        baseSlot.getDisplayedItemStack().ifPresent((inputArmor) -> {
            additionSlot.getDisplayedItemStack().ifPresent((addition) -> {
                NbtList echoingLayers = new NbtList();
                NbtCompound echoingLayer = new NbtCompound();
                echoingLayer.putBoolean(recipe.getFlag(), true);
                echoingLayers.add(echoingLayer);
                ItemStack outputStack = inputArmor.copy();
                outputStack.getOrCreateNbt().getCompound("Trim").put("ectolum.echoing_layers", echoingLayers);
                outputSlot.createDisplayOverrides().addItemStack(outputStack);
            });
        });
    }
}
