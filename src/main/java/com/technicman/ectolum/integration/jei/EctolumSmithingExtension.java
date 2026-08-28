package com.technicman.ectolum.integration.jei;

import com.technicman.ectolum.recipe.EctolumRecipe;
import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.recipe.category.extensions.vanilla.smithing.ISmithingCategoryExtension;
import net.minecraft.item.trim.ArmorTrimMaterials;
import net.minecraft.item.trim.ArmorTrimPatterns;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtString;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public abstract class EctolumSmithingExtension<R extends EctolumRecipe> implements ISmithingCategoryExtension<R> {
    public static final String[] MATERIALS = new String[] {
            ArmorTrimMaterials.QUARTZ.getValue().toString(),
            ArmorTrimMaterials.IRON.getValue().toString(),
            ArmorTrimMaterials.NETHERITE.getValue().toString(),
            ArmorTrimMaterials.REDSTONE.getValue().toString(),
            ArmorTrimMaterials.COPPER.getValue().toString(),
            ArmorTrimMaterials.GOLD.getValue().toString(),
            ArmorTrimMaterials.EMERALD.getValue().toString(),
            ArmorTrimMaterials.DIAMOND.getValue().toString(),
            ArmorTrimMaterials.LAPIS.getValue().toString(),
            ArmorTrimMaterials.AMETHYST.getValue().toString(),
    };

    public static final String[] PATTERNS = new String[] {
            ArmorTrimPatterns.SENTRY.getValue().toString(),
            ArmorTrimPatterns.DUNE.getValue().toString(),
            ArmorTrimPatterns.COAST.getValue().toString(),
            ArmorTrimPatterns.WILD.getValue().toString(),
            ArmorTrimPatterns.WARD.getValue().toString(),
            ArmorTrimPatterns.EYE.getValue().toString(),
            ArmorTrimPatterns.VEX.getValue().toString(),
            ArmorTrimPatterns.TIDE.getValue().toString(),
            ArmorTrimPatterns.SNOUT.getValue().toString(),
            ArmorTrimPatterns.RIB.getValue().toString(),
            ArmorTrimPatterns.SPIRE.getValue().toString(),
            ArmorTrimPatterns.WAYFINDER.getValue().toString(),
            ArmorTrimPatterns.SHAPER.getValue().toString(),
            ArmorTrimPatterns.SILENCE.getValue().toString(),
            ArmorTrimPatterns.RAISER.getValue().toString(),
            ArmorTrimPatterns.HOST.getValue().toString(),
    };

    @Override
    public <T extends IIngredientAcceptor<T>> void setTemplate(R recipe, T ingredientAcceptor) {
        ingredientAcceptor.addIngredients(recipe.getTemplate());
    }

    @Override
    public <T extends IIngredientAcceptor<T>> void setBase(R recipe, T ingredientAcceptor) {
        Random random = new Random();
        ingredientAcceptor.addItemStacks(Arrays.stream(recipe.getBase().getMatchingStacks())
                .peek((stack) -> {
                    NbtCompound trim = new NbtCompound();
                    trim.put("pattern", NbtString.of(PATTERNS[random.nextInt(PATTERNS.length)]));
                    trim.put("material", NbtString.of(MATERIALS[random.nextInt(MATERIALS.length)]));

                    stack.getOrCreateNbt().put("Trim", trim);

                }).collect(Collectors.toList())
        );
    }

    @Override
    public <T extends IIngredientAcceptor<T>> void setAddition(R recipe, T ingredientAcceptor) {
        ingredientAcceptor.addIngredients(recipe.getAddition());
    }
}
