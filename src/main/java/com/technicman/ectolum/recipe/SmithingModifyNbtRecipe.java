package com.technicman.ectolum.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.technicman.ectolum.Ectoluminescence;
import com.technicman.ectolum.util.NbtOperator;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.item.trim.ArmorTrimMaterials;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.stream.Stream;

public class SmithingModifyNbtRecipe implements EctolumRecipe {
    private final Identifier id;
    final Ingredient template;
    final Ingredient base;
    final Ingredient addition;
    final NbtElement nbt;
    final String path;
    final NbtPathArgumentType.NbtPath parsedPath;
    final NbtOperator operator;

    public SmithingModifyNbtRecipe(Identifier id, Ingredient template, Ingredient base, Ingredient addition, NbtElement nbt, String path, NbtOperator operator) {
        this.id = id;
        this.template = template;
        this.base = base;
        this.addition = addition;
        this.nbt = nbt;
        this.path = path;
        if (path == null) {
            switch (operator) {
                case MERGE, REPLACE -> {
                    if (nbt.getType() != NbtElement.COMPOUND_TYPE) {
                        throw new IllegalArgumentException("Expected \"nbt\" to be an object when \"path\" field is empty in recipe: " + id);
                    }
                }
                case APPEND ->
                        throw new IllegalArgumentException("\"path\" field cannot be empty when operator is set to \"append\" in recipe: " + id);
            }
            parsedPath = null;
        } else {
            try {
                parsedPath = (new NbtPathArgumentType()).parse(new StringReader(path));
            } catch (CommandSyntaxException var2) {
                throw new IllegalArgumentException("Failed to parse path " + path, var2);
            }
        }
        this.operator = operator;
    }

    public NbtElement getNbt() {
        return nbt;
    }

    public NbtPathArgumentType.NbtPath getPath() {
        return parsedPath;
    }

    public NbtOperator getOperator() {
        return operator;
    }

    public boolean matches(Inventory inventory, World world) {
        return this.template.test(inventory.getStack(0)) && this.base.test(inventory.getStack(1)) && this.addition.test(inventory.getStack(2)) && isNbtValid(inventory.getStack(1));
    }

    public boolean isNbtValid(ItemStack stack) {
        ItemStack item = stack.copy();
        try {
            operator.merge(item, parsedPath, nbt);
        } catch (CommandSyntaxException e) {
            return false;
        }

        NbtCompound trim = item.getOrCreateNbt().getCompound("Trim");
        if (!trim.isEmpty()) {
            return trim.contains("material") && trim.contains("pattern");
        }

        return true;
    }

    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        ItemStack itemStack = inventory.getStack(1);
        ItemStack itemStack2 = itemStack.copy();
        if (nbt != null) {
            try {
                operator.merge(itemStack2, parsedPath, nbt);
            } catch (CommandSyntaxException e) {
                Ectoluminescence.LOGGER.warn("Couldn't take result from smithing modify nbt recipe: " + e);
            }
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
                    try {
                        operator.merge(itemStack, parsedPath, nbt);
                    } catch (CommandSyntaxException e) {
                        Ectoluminescence.LOGGER.warn("Couldn't take result from smithing modify nbt recipe: " + e);
                    }
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

    public static class Serializer implements RecipeSerializer<SmithingModifyNbtRecipe> {
        public Serializer() {
        }

        public SmithingModifyNbtRecipe read(Identifier identifier, JsonObject jsonObject) {
            Ingredient ingredient = Ingredient.fromJson(JsonHelper.getElement(jsonObject, "template"));
            Ingredient ingredient2 = Ingredient.fromJson(JsonHelper.getElement(jsonObject, "base"));
            Ingredient ingredient3 = Ingredient.fromJson(JsonHelper.getElement(jsonObject, "addition"));
            NbtCompound nbt = null;
            try {
                nbt = StringNbtReader.parse(JsonHelper.getString(jsonObject, "nbt"));
            } catch (CommandSyntaxException e) {
                throw new JsonParseException("Couldn't parse \"nbt\" field of recipe " + identifier + ": ", e);
            } catch (JsonSyntaxException ignored) {}
            String path = JsonHelper.getString(jsonObject, "path", null);
            NbtOperator operator = NbtOperator.get(JsonHelper.getString(jsonObject, "operator", "merge"));
            return new SmithingModifyNbtRecipe(identifier, ingredient, ingredient2, ingredient3, nbt, path, operator);
        }

        public SmithingModifyNbtRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
            Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
            Ingredient ingredient2 = Ingredient.fromPacket(packetByteBuf);
            Ingredient ingredient3 = Ingredient.fromPacket(packetByteBuf);
            NbtCompound nbt;
            try {
                nbt = StringNbtReader.parse(packetByteBuf.readString());
            } catch (CommandSyntaxException e) {
                throw new JsonParseException("Unexpected error. Couldn't parse \"nbt\" field of recipe " + identifier + " from packet: ", e);
            }
            String path = packetByteBuf.readString();
            NbtOperator operator = NbtOperator.values()[packetByteBuf.readByte()];
            return new SmithingModifyNbtRecipe(identifier, ingredient, ingredient2, ingredient3, nbt, path, operator);
        }

        public void write(PacketByteBuf packetByteBuf, SmithingModifyNbtRecipe smithingTransformRecipe) {
            smithingTransformRecipe.template.write(packetByteBuf);
            smithingTransformRecipe.base.write(packetByteBuf);
            smithingTransformRecipe.addition.write(packetByteBuf);
            packetByteBuf.writeString(smithingTransformRecipe.nbt.asString());
            packetByteBuf.writeString(smithingTransformRecipe.path);
            packetByteBuf.writeByte(smithingTransformRecipe.operator.ordinal());
        }
    }
}

