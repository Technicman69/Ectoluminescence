package com.technicman.ectolum.recipe;

import com.google.gson.JsonObject;
import com.technicman.ectolum.Ectoluminescence;
import com.technicman.ectolum.accessor.EctolumArmorTrimInterface;
import com.technicman.ectolum.accessor.EctolumArmorTrimInterface.EchoingLayer;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.item.trim.ArmorTrimMaterials;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;

import java.util.Arrays;
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

    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        ItemStack itemStack = inventory.getStack(1);
        if (this.base.test(itemStack)) {
            Optional<RegistryEntry.Reference<ArmorTrimMaterial>> optionalMaterial = ArmorTrimMaterials.get(registryManager, inventory.getStack(2));
            if (optionalMaterial.isPresent()) {
                Optional<ArmorTrim> optionalTrim = ArmorTrim.getTrim(registryManager, itemStack);
                if (optionalTrim.isEmpty()) {
                    return ItemStack.EMPTY;
                }
                EctolumArmorTrimInterface trim = (EctolumArmorTrimInterface) optionalTrim.get();
                RegistryEntry<ArmorTrimMaterial> material = optionalMaterial.get();
                if (trim.ectolum$hasEchoingLayers()) {
                    Optional<RegistryEntry<ArmorTrimMaterial>> optional3 = trim.ectolum$getEchoingLayers()[0].material();

                    if (optional3.isPresent() && optional3.get().equals(material)) {
                        return ItemStack.EMPTY;
                    }
                }

                ItemStack itemStack2 = itemStack.copy();
                itemStack2.setCount(1);

                EchoingLayer[] oldLayers = trim.ectolum$hasEchoingLayers() ? trim.ectolum$getEchoingLayers() : new EchoingLayer[]{};
                // Refuse to craft if echoing layer limit reached
                if (oldLayers.length >= Ectoluminescence.ECHOING_LAYER_LIMIT) {
                    return ItemStack.EMPTY;
                }
                EchoingLayer[] layers = Arrays.copyOf(oldLayers, oldLayers.length + 1);

                EchoingLayer echoingLayer = new EchoingLayer(Optional.of(material), Optional.empty(), Optional.empty());
                layers[oldLayers.length] = echoingLayer;
                trim.ectolum$setEchoingLayers(layers);

                if (ArmorTrim.apply(registryManager, itemStack2, (ArmorTrim) trim)) {
                    return itemStack2;
                }
            }
        }

        return ItemStack.EMPTY;
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
        return this.base.test(stack) && stack.getOrCreateNbt().contains("Trim", NbtElement.COMPOUND_TYPE);
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

