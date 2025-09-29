package com.technicman.ectolum.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.item.trim.ArmorTrimMaterials;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.stream.Stream;

public class SmithingMergeNbtRecipe implements EctolumRecipe {
    private final Identifier id;
    final Ingredient template;
    final Ingredient base;
    final Ingredient addition;
    final NbtCompound nbt;

    public SmithingMergeNbtRecipe(Identifier id, Ingredient template, Ingredient base, Ingredient addition, NbtCompound result) {
        this.id = id;
        this.template = template;
        this.base = base;
        this.addition = addition;
        this.nbt = result;
    }

    public boolean matches(Inventory inventory, World world) {
        return this.template.test(inventory.getStack(0)) && this.base.test(inventory.getStack(1)) && this.addition.test(inventory.getStack(2)) && isTrimValid(inventory.getStack(1));
    }

    public boolean isTrimValid(ItemStack stack) {
        if (this.nbt != null && this.nbt.contains("Trim")) {
            NbtCompound nbt = stack.getOrCreateNbt().copy().copyFrom(this.nbt);
            return nbt.contains("Trim", NbtCompound.COMPOUND_TYPE) &&
                    nbt.getCompound("Trim").contains("pattern", NbtCompound.STRING_TYPE) &&
                    nbt.getCompound("Trim").contains("material", NbtCompound.STRING_TYPE);
        }
        return true;
    }

    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        ItemStack itemStack = inventory.getStack(1);
        ItemStack itemStack2 = itemStack.copy();
        if (nbt != null) {
            itemStack2.getOrCreateNbt().copyFrom(nbt);
            if (itemStack2.getOrCreateNbt().equals(itemStack.getNbt())) {
                return ItemStack.EMPTY;
            }
        }

        return itemStack2;
    }

    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        ItemStack itemStack = new ItemStack(Items.IRON_CHESTPLATE);
        Optional<RegistryEntry.Reference<ArmorTrimPattern>> optional = registryManager.get(RegistryKeys.TRIM_PATTERN).streamEntries().findFirst();
        if (optional.isPresent()) {
            Optional<RegistryEntry.Reference<ArmorTrimMaterial>> optional2 = registryManager.get(RegistryKeys.TRIM_MATERIAL).getEntry(ArmorTrimMaterials.REDSTONE);
            if (optional2.isPresent()) {
                ArmorTrim armorTrim = new ArmorTrim(optional2.get(), optional.get());
                ArmorTrim.apply(registryManager, itemStack, armorTrim);
                if (nbt != null) {
                    itemStack.getOrCreateNbt().copyFrom(nbt);
                }
            }
        }

        return itemStack;
    }

    public boolean testTemplate(ItemStack stack) {
        return this.template.test(stack);
    }

    public boolean testBase(ItemStack stack) {
        return this.base.test(stack);
    }

    public boolean testAddition(ItemStack stack) {
        return this.addition.test(stack);
    }

    public Identifier getId() {
        return this.id;
    }

    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.SMITHING_MERGE_NBT_RECIPE;
    }

    public boolean isEmpty() {
        return Stream.of(this.template, this.base, this.addition).anyMatch(Ingredient::isEmpty);
    }

    @Override
    public Ingredient getTemplate() {
        return template;
    }

    @Override
    public Ingredient getBase() {
        return base;
    }

    @Override
    public Ingredient getAddition() {
        return addition;
    }

    public static class Serializer implements RecipeSerializer<SmithingMergeNbtRecipe> {
        public Serializer() {
        }

        public SmithingMergeNbtRecipe read(Identifier identifier, JsonObject jsonObject) {
            Ingredient ingredient = Ingredient.fromJson(JsonHelper.getElement(jsonObject, "template"));
            Ingredient ingredient2 = Ingredient.fromJson(JsonHelper.getElement(jsonObject, "base"));
            Ingredient ingredient3 = Ingredient.fromJson(JsonHelper.getElement(jsonObject, "addition"));
            NbtCompound nbt = null;
            try {
                nbt = StringNbtReader.parse(JsonHelper.getString(jsonObject, "nbt"));
            } catch (CommandSyntaxException e) {
                throw new JsonParseException("Couldn't parse \"nbt\" field of recipe " + identifier + ": ", e);
            } catch (JsonSyntaxException ignored) {}
            return new SmithingMergeNbtRecipe(identifier, ingredient, ingredient2, ingredient3, nbt);
        }

        public SmithingMergeNbtRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
            Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
            Ingredient ingredient2 = Ingredient.fromPacket(packetByteBuf);
            Ingredient ingredient3 = Ingredient.fromPacket(packetByteBuf);
            NbtCompound nbt = packetByteBuf.readNbt();
            return new SmithingMergeNbtRecipe(identifier, ingredient, ingredient2, ingredient3, nbt);
        }

        public void write(PacketByteBuf packetByteBuf, SmithingMergeNbtRecipe smithingTransformRecipe) {
            smithingTransformRecipe.template.write(packetByteBuf);
            smithingTransformRecipe.base.write(packetByteBuf);
            smithingTransformRecipe.addition.write(packetByteBuf);
            packetByteBuf.writeNbt(smithingTransformRecipe.nbt);
        }
    }
}

