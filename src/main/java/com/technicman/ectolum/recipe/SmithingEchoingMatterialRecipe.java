package com.technicman.ectolum.recipe;

import com.google.gson.JsonObject;
import com.technicman.ectolum.addon.EctolumArmorTrimInterface;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.trim.*;
import net.minecraft.nbt.NbtCompound;
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

public class SmithingEchoingMatterialRecipe implements EctolumRecipe {
    private final Identifier id;
    final Ingredient template;
    final Ingredient base;
    final Ingredient addition;

    public SmithingEchoingMatterialRecipe(Identifier id, Ingredient template, Ingredient base, Ingredient addition) {
        this.id = id;
        this.template = template;
        this.base = base;
        this.addition = addition;
    }

    public boolean matches(Inventory inventory, World world) {
        return this.template.test(inventory.getStack(0)) && this.base.test(inventory.getStack(1)) && this.addition.test(inventory.getStack(2));
    }

//    public boolean isTrimValid(ItemStack stack) {
//        if (stack.getNbt() == null) {
//            return false;
//        }
//        NbtCompound nbt = stack.getNbt();
//        return nbt.contains("Trim", NbtCompound.COMPOUND_TYPE) &&
//                nbt.getCompound("Trim").contains("pattern", NbtCompound.STRING_TYPE) &&
//                nbt.getCompound("Trim").contains("material", NbtCompound.STRING_TYPE);
//    }

    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        ItemStack itemStack = inventory.getStack(1);
        if (this.base.test(itemStack)) {
            Optional<RegistryEntry.Reference<ArmorTrimMaterial>> optional = ArmorTrimMaterials.get(registryManager, inventory.getStack(2));
            if (optional.isPresent()) {
                Optional<ArmorTrim> optional2 = ArmorTrim.getTrim(registryManager, itemStack);
                // Refuse to craft if item has no trim
                if (optional2.isEmpty()) {
                    return ItemStack.EMPTY;
                }
                EctolumArmorTrimInterface trim = (EctolumArmorTrimInterface) optional2.get();
                RegistryEntry<ArmorTrimMaterial> material = optional.get();
                if (trim.ectolum$hasEchoingLayers()) {
                    Optional<RegistryEntry<ArmorTrimMaterial>> optional3 = trim.ectolum$getEchoingLayers()[0].material();

                    if (optional3.isPresent() && optional3.get().equals(material)) {
                        return ItemStack.EMPTY;
                    }
                }

                ItemStack itemStack2 = itemStack.copy();
                itemStack2.setCount(1);
                trim.ectolum$setEchoingLayers(new EctolumArmorTrimInterface.EchoingLayer[]{new EctolumArmorTrimInterface.EchoingLayer(Optional.of(material), Optional.empty(), Optional.empty())});
                if (ArmorTrim.apply(registryManager, itemStack2, (ArmorTrim) trim)) {
                    return itemStack2;
                }
            }
        }

        return itemStack;
    }

    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        ItemStack itemStack = new ItemStack(Items.IRON_CHESTPLATE);
        Optional<RegistryEntry.Reference<ArmorTrimPattern>> optional = registryManager.get(RegistryKeys.TRIM_PATTERN).streamEntries().findFirst();
        if (optional.isPresent()) {
            Optional<RegistryEntry.Reference<ArmorTrimMaterial>> optional2 = registryManager.get(RegistryKeys.TRIM_MATERIAL).getEntry(ArmorTrimMaterials.REDSTONE);
            if (optional2.isPresent()) {
                Optional<RegistryEntry.Reference<ArmorTrimMaterial>> optional3 = registryManager.get(RegistryKeys.TRIM_MATERIAL).getEntry(ArmorTrimMaterials.GOLD);
                if (optional3.isPresent()) {
                    ArmorTrim armorTrim = new ArmorTrim(optional2.get(), optional.get());
                    RegistryEntry<ArmorTrimMaterial> material = optional3.get();
                    EctolumArmorTrimInterface.EchoingLayer layer = new EctolumArmorTrimInterface.EchoingLayer(Optional.of(material), Optional.empty(), Optional.empty());
                    ((EctolumArmorTrimInterface) armorTrim).ectolum$setEchoingLayers(new EctolumArmorTrimInterface.EchoingLayer[]{layer});
                    ArmorTrim.apply(registryManager, itemStack, armorTrim);
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
        return ModRecipes.SMITHING_ECHOING_MATERIAL_RECIPE;
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

    public static class Serializer implements RecipeSerializer<SmithingEchoingMatterialRecipe> {
        public Serializer() {
        }

        public SmithingEchoingMatterialRecipe read(Identifier identifier, JsonObject jsonObject) {
            Ingredient ingredient = Ingredient.fromJson(JsonHelper.getElement(jsonObject, "template"));
            Ingredient ingredient2 = Ingredient.fromJson(JsonHelper.getElement(jsonObject, "base"));
            Ingredient ingredient3 = Ingredient.fromJson(JsonHelper.getElement(jsonObject, "addition"));
            return new SmithingEchoingMatterialRecipe(identifier, ingredient, ingredient2, ingredient3);
        }

        public SmithingEchoingMatterialRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
            Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
            Ingredient ingredient2 = Ingredient.fromPacket(packetByteBuf);
            Ingredient ingredient3 = Ingredient.fromPacket(packetByteBuf);
            return new SmithingEchoingMatterialRecipe(identifier, ingredient, ingredient2, ingredient3);
        }

        public void write(PacketByteBuf packetByteBuf, SmithingEchoingMatterialRecipe smithingTransformRecipe) {
            smithingTransformRecipe.template.write(packetByteBuf);
            smithingTransformRecipe.base.write(packetByteBuf);
            smithingTransformRecipe.addition.write(packetByteBuf);
        }
    }
}

