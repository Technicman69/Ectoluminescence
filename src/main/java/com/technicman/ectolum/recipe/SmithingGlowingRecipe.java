package com.technicman.ectolum.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.technicman.ectolum.component.ModComponents;
import com.technicman.ectolum.util.EchoingLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.input.SmithingRecipeInput;
import net.minecraft.registry.RegistryWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SmithingGlowingRecipe extends SmithingEchoingRecipe {

    public SmithingGlowingRecipe(Ingredient template, Ingredient base, Ingredient addition) {
        super(template, base, addition);
    }

    public ItemStack craft(SmithingRecipeInput smithingRecipeInput, RegistryWrapper.WrapperLookup wrapperLookup) {
        ItemStack itemStack = smithingRecipeInput.base();
        if (testBase(itemStack)) {
            List<EchoingLayer> echoingLayers = new ArrayList<>();
            echoingLayers.add(new EchoingLayer(
                    Optional.of(true),
                    Optional.empty(),
                    Optional.empty()
            ));
            ItemStack newStack = itemStack.copyWithCount(1);
            newStack.set(ModComponents.ECHOING_LAYERS, echoingLayers);
            return newStack;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.SMITHING_GLOWING_RECIPE;
    }

    public static class Serializer implements RecipeSerializer<SmithingGlowingRecipe> {
        private static final MapCodec<SmithingGlowingRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) ->
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
                ).apply(instance, SmithingGlowingRecipe::new));

        public static final PacketCodec<RegistryByteBuf, SmithingGlowingRecipe> PACKET_CODEC =
                PacketCodec.ofStatic(
                        SmithingGlowingRecipe.Serializer::write,
                        SmithingGlowingRecipe.Serializer::read
                );

        public Serializer() {
        }

        public MapCodec<SmithingGlowingRecipe> codec() {
            return CODEC;
        }

        public PacketCodec<RegistryByteBuf, SmithingGlowingRecipe> packetCodec() {
            return PACKET_CODEC;
        }

        private static SmithingGlowingRecipe read(RegistryByteBuf buf) {
            Ingredient template = Ingredient.PACKET_CODEC.decode(buf);
            Ingredient base = Ingredient.PACKET_CODEC.decode(buf);
            Ingredient addition = Ingredient.PACKET_CODEC.decode(buf);
            return new SmithingGlowingRecipe(template, base, addition);
        }

        private static void write(RegistryByteBuf buf, SmithingGlowingRecipe recipe) {
            Ingredient.PACKET_CODEC.encode(buf, recipe.getTemplate());
            Ingredient.PACKET_CODEC.encode(buf, recipe.getBase());
            Ingredient.PACKET_CODEC.encode(buf, recipe.getAddition());
        }
    }
}

