package com.technicman.ectolum.recipe;

import com.google.gson.JsonObject;
import com.technicman.ectolum.Ectoluminescence;
import com.technicman.ectolum.accessor.EctolumArmorTrimInterface;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.item.trim.ArmorTrimMaterials;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
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

public class SmithingToggleEchoingFlagRecipe implements EctolumRecipe {
    private final Identifier id;
    final Ingredient template;
    final Ingredient base;
    final Ingredient addition;
    final String flag;

    public SmithingToggleEchoingFlagRecipe(Identifier id, Ingredient template, Ingredient base, Ingredient addition, String flag) {
        this.id = id;
        this.template = template;
        this.base = base;
        this.addition = addition;
        this.flag = flag;
    }

    public boolean matches(Inventory inventory, World world) {
        return this.template.test(inventory.getStack(0)) && this.base.test(inventory.getStack(1)) && this.addition.test(inventory.getStack(2));
    }

    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        ItemStack original = inventory.getStack(1);
        ItemStack result = original.copy();

        NbtCompound trim = result.getOrCreateNbt().getCompound("Trim");
        if (trim.isEmpty()) {
            return ItemStack.EMPTY;
        }
        boolean negation = !trim.getBoolean("ectolum." + flag);
        if (!trim.contains("ectolum.echoing_layers", NbtElement.LIST_TYPE)) {

            NbtCompound echoingLayer = new NbtCompound();
            echoingLayer.putBoolean(flag, negation);

            NbtList echoingLayers = new NbtList();
            echoingLayers.add(echoingLayer);

            trim.put("ectolum.echoing_layers", echoingLayers);
            return result;
        }

        NbtList echoingLayers = trim.getList("ectolum.echoing_layers", NbtElement.COMPOUND_TYPE);

        if (echoingLayers.size() >= Ectoluminescence.ECHOING_LAYER_LIMIT) {
            // Echoing layer limit reached
            return ItemStack.EMPTY;
        }

        for (int i = echoingLayers.size()-1; i >= 0; i--) {
            NbtCompound echoingLayer = echoingLayers.getCompound(i);
            if (echoingLayer.contains(flag)) {
                negation = !echoingLayer.getBoolean(flag);
                break;
            }
        }

        NbtCompound echoingLayer = new NbtCompound();
        echoingLayer.putBoolean(flag, negation);

        echoingLayers.add(echoingLayer);
        return result;
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
                    EctolumArmorTrimInterface.EchoingLayer layer = new EctolumArmorTrimInterface.EchoingLayer(Optional.of(material), Optional.of(true), Optional.empty());
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
        return ModRecipes.SMITHING_TOGGLE_ECHOING_FLAG_RECIPE;
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

    public static class Serializer implements RecipeSerializer<SmithingToggleEchoingFlagRecipe> {
        public Serializer() {
        }

        public SmithingToggleEchoingFlagRecipe read(Identifier identifier, JsonObject jsonObject) {
            Ingredient ingredient = Ingredient.fromJson(JsonHelper.getElement(jsonObject, "template"));
            Ingredient ingredient2 = Ingredient.fromJson(JsonHelper.getElement(jsonObject, "base"));
            Ingredient ingredient3 = Ingredient.fromJson(JsonHelper.getElement(jsonObject, "addition"));
            String flag = JsonHelper.getString(jsonObject, "flag");
            return new SmithingToggleEchoingFlagRecipe(identifier, ingredient, ingredient2, ingredient3, flag);
        }

        public SmithingToggleEchoingFlagRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
            Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
            Ingredient ingredient2 = Ingredient.fromPacket(packetByteBuf);
            Ingredient ingredient3 = Ingredient.fromPacket(packetByteBuf);
            String flag = packetByteBuf.readString();
            return new SmithingToggleEchoingFlagRecipe(identifier, ingredient, ingredient2, ingredient3, flag);
        }

        public void write(PacketByteBuf packetByteBuf, SmithingToggleEchoingFlagRecipe smithingTransformRecipe) {
            smithingTransformRecipe.template.write(packetByteBuf);
            smithingTransformRecipe.base.write(packetByteBuf);
            smithingTransformRecipe.addition.write(packetByteBuf);
            packetByteBuf.writeString(smithingTransformRecipe.flag);
        }
    }
}

