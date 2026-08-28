package com.technicman.ectolum.integration.jei;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.technicman.ectolum.recipe.SmithingModifyNbtRecipe;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.item.ItemStack;

public class SmithingModifyNbtRecipeExtension extends EctolumSmithingExtension<SmithingModifyNbtRecipe> {
    @Override
    public void onDisplayedIngredientsUpdate(SmithingModifyNbtRecipe recipe, IRecipeSlotDrawable templateSlot, IRecipeSlotDrawable baseSlot, IRecipeSlotDrawable additionSlot, IRecipeSlotDrawable outputSlot, IFocusGroup focuses) {
        baseSlot.getDisplayedItemStack().ifPresent((inputArmor) -> {
            additionSlot.getDisplayedItemStack().ifPresent((addition) -> {
                ItemStack outputStack = inputArmor.copy();
                if (recipe.getNbt() != null) {
                    try {
                        recipe.getOperator().merge(outputStack, recipe.getPath(), recipe.getNbt());
                        outputSlot.createDisplayOverrides().addItemStack(outputStack);
                    } catch (CommandSyntaxException ignored) {

                    }
                }
            });
        });
    }
}
