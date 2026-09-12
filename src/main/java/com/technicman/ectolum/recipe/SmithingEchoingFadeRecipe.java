package com.technicman.ectolum.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.technicman.ectolum.util.EchoingKeyframe;
import com.technicman.ectolum.util.EchoingLayer;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;

import java.util.Optional;

public class SmithingEchoingFadeRecipe extends SmithingToggleEchoingFlagRecipe {

    public SmithingEchoingFadeRecipe(Ingredient template, Ingredient base, Ingredient addition) {
        super(template, base, addition);
    }

    @Override
    public EchoingLayer computeEchoingLayer(EchoingKeyframe keyFrame) {
        return new EchoingLayer(
                Optional.empty(),
                Optional.of(keyFrame.hidden()),
                Optional.empty()
        );
    }
    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.SMITHING_ECHOING_FADE_RECIPE;
    }

    public static class Serializer implements RecipeSerializer<SmithingEchoingFadeRecipe> {
        private static final MapCodec<SmithingEchoingFadeRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) ->
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
                ).apply(instance, SmithingEchoingFadeRecipe::new));

        public static final PacketCodec<RegistryByteBuf, SmithingEchoingFadeRecipe> PACKET_CODEC =
                PacketCodec.ofStatic(
                        SmithingEchoingFadeRecipe.Serializer::write,
                        SmithingEchoingFadeRecipe.Serializer::read
                );

        public Serializer() {
        }

        public MapCodec<SmithingEchoingFadeRecipe> codec() {
            return CODEC;
        }

        public PacketCodec<RegistryByteBuf, SmithingEchoingFadeRecipe> packetCodec() {
            return PACKET_CODEC;
        }

        private static SmithingEchoingFadeRecipe read(RegistryByteBuf buf) {
            Ingredient template = Ingredient.PACKET_CODEC.decode(buf);
            Ingredient base = Ingredient.PACKET_CODEC.decode(buf);
            Ingredient addition = Ingredient.PACKET_CODEC.decode(buf);
            return new SmithingEchoingFadeRecipe(template, base, addition);
        }

        private static void write(RegistryByteBuf buf, SmithingEchoingFadeRecipe recipe) {
            Ingredient.PACKET_CODEC.encode(buf, recipe.getTemplate());
            Ingredient.PACKET_CODEC.encode(buf, recipe.getBase());
            Ingredient.PACKET_CODEC.encode(buf, recipe.getAddition());
        }
    }
}

