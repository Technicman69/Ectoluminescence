package com.technicman.ectolum.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.technicman.ectolum.component.ModComponents;
import com.technicman.ectolum.util.EchoingKeyframe;
import com.technicman.ectolum.util.EchoingLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.*;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.input.SmithingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.List;
import java.util.Optional;

public class SmithingEchoingPigmentRecipe extends SmithingEchoingRecipe {

    public SmithingEchoingPigmentRecipe(Ingredient template, Ingredient base, Ingredient addition) {
        super(template, base, addition);
    }

    public ItemStack craft(SmithingRecipeInput smithingRecipeInput, RegistryWrapper.WrapperLookup wrapperLookup) {
        ItemStack itemStack = smithingRecipeInput.base();
        if (testBase(itemStack)) {
            Optional<RegistryEntry.Reference<ArmorTrimMaterial>> optionalMaterial = ArmorTrimMaterials.get(wrapperLookup, smithingRecipeInput.addition());
            if (optionalMaterial.isPresent()) {
                RegistryEntry<ArmorTrimMaterial> material = optionalMaterial.get();

                EchoingKeyframe last = EchoingKeyframe.buildLast(itemStack);
                if (last.material().equals(material)) {
                    return ItemStack.EMPTY;
                }

                List<EchoingLayer> echoingLayers = copyEchoingLayers(itemStack);
                if (echoingLayers == null) {
                    return ItemStack.EMPTY;
                }

                echoingLayers.add(new EchoingLayer(
                        Optional.empty(),
                        Optional.empty(),
                        Optional.of(material)
                ));

                ItemStack newStack = itemStack.copyWithCount(1);
                newStack.set(ModComponents.ECHOING_LAYERS, echoingLayers);
                return newStack;
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.SMITHING_ECHOING_PIGMENT_RECIPE;
    }

    public static class Serializer implements RecipeSerializer<SmithingEchoingPigmentRecipe> {
        private static final MapCodec<SmithingEchoingPigmentRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) ->
                instance.group(
                        Ingredient.ALLOW_EMPTY_CODEC
                                .fieldOf("template")
                                .forGetter(SmithingEchoingRecipe::getTemplate),
                        Ingredient.ALLOW_EMPTY_CODEC
                                .fieldOf("base")
                                .forGetter(SmithingEchoingRecipe::getBase),
                        Ingredient.ALLOW_EMPTY_CODEC
                                .fieldOf("addition")
                                .forGetter(SmithingEchoingRecipe::getAddition)
                ).apply(instance, SmithingEchoingPigmentRecipe::new));

        public static final PacketCodec<RegistryByteBuf, SmithingEchoingPigmentRecipe> PACKET_CODEC =
                PacketCodec.ofStatic(
                        SmithingEchoingPigmentRecipe.Serializer::write,
                        SmithingEchoingPigmentRecipe.Serializer::read
                );

        public Serializer() {
        }

        public MapCodec<SmithingEchoingPigmentRecipe> codec() {
            return CODEC;
        }

        public PacketCodec<RegistryByteBuf, SmithingEchoingPigmentRecipe> packetCodec() {
            return PACKET_CODEC;
        }

        private static SmithingEchoingPigmentRecipe read(RegistryByteBuf buf) {
            Ingredient template = Ingredient.PACKET_CODEC.decode(buf);
            Ingredient base = Ingredient.PACKET_CODEC.decode(buf);
            Ingredient addition = Ingredient.PACKET_CODEC.decode(buf);
            return new SmithingEchoingPigmentRecipe(template, base, addition);
        }

        private static void write(RegistryByteBuf buf, SmithingEchoingPigmentRecipe recipe) {
            Ingredient.PACKET_CODEC.encode(buf, recipe.getTemplate());
            Ingredient.PACKET_CODEC.encode(buf, recipe.getBase());
            Ingredient.PACKET_CODEC.encode(buf, recipe.getAddition());
        }
    }
}

