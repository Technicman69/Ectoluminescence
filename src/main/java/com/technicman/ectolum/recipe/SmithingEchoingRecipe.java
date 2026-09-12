package com.technicman.ectolum.recipe;

import com.technicman.ectolum.Ectoluminescence;
import com.technicman.ectolum.component.ModComponents;
import com.technicman.ectolum.util.EchoingLayer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.item.trim.ArmorTrimMaterials;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.recipe.input.SmithingRecipeInput;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class SmithingEchoingRecipe implements SmithingRecipe {
    private final Ingredient template;
    private final Ingredient base;
    private final Ingredient addition;

    public List<EchoingLayer> copyEchoingLayers(ItemStack item) {
        List<EchoingLayer> echoingLayers = item.get(ModComponents.ECHOING_LAYERS);
        if (echoingLayers == null) return EchoingLayer.emptyList();
            // If the echoing layer limit reached, refuse to craft
        else if (echoingLayers.size() >= Ectoluminescence.ECHOING_LAYER_LIMIT) {
            return null;
        }
        // If not, then copy layers from original item stack
        else {
            return new ArrayList<>(echoingLayers);
        }
    }

    public ItemStack getStackWithLayers(ItemStack item, List<EchoingLayer> layers) {
        ItemStack newStack = item.copyWithCount(1);
        newStack.set(ModComponents.ECHOING_LAYERS, layers);
        return newStack;
    }

    public SmithingEchoingRecipe(Ingredient template, Ingredient base, Ingredient addition) {
        this.template = template;
        this.base = base;
        this.addition = addition;
    }

    public Ingredient getTemplate() {
        return template;
    }

    public Ingredient getBase() {
        return base;
    }

    public Ingredient getAddition() {
        return addition;
    }

    @Override
    public boolean testTemplate(ItemStack stack) {
        return template.test(stack);
    }

    @Override
    public boolean testBase(ItemStack stack) {
        return base.test(stack) && stack.contains(DataComponentTypes.TRIM);
    }

    @Override
    public boolean testAddition(ItemStack stack) {
        return addition.test(stack);
    }

    @Override
    public boolean matches(SmithingRecipeInput smithingRecipeInput, World world) {
        return template.test(smithingRecipeInput.template()) && base.test(smithingRecipeInput.base()) && addition.test(smithingRecipeInput.addition());
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        ItemStack itemStack = new ItemStack(Items.IRON_CHESTPLATE);
        Optional<RegistryEntry.Reference<ArmorTrimPattern>> pattern = registriesLookup.getWrapperOrThrow(RegistryKeys.TRIM_PATTERN).streamEntries().findFirst();
        Optional<RegistryEntry.Reference<ArmorTrimMaterial>> material = registriesLookup.getWrapperOrThrow(RegistryKeys.TRIM_MATERIAL).getOptional(ArmorTrimMaterials.REDSTONE);
        if (pattern.isPresent() && material.isPresent()) {
            itemStack.set(DataComponentTypes.TRIM, new ArmorTrim(material.get(), pattern.get()));
        }

        return itemStack;
    }

    @Override
    public boolean isEmpty() {
        return Stream.of(template, base, addition).anyMatch(Ingredient::isEmpty);
    }
}
