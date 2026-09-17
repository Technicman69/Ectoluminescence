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

public class SmithingEchoingTwinkleRecipe extends SmithingToggleEchoingFlagRecipe {

    public SmithingEchoingTwinkleRecipe(Ingredient template, Ingredient base, Ingredient addition) {
        super(template, base, addition);
    }

    @Override
    public EchoingLayer computeEchoingLayer(EchoingKeyframe keyFrame) {
        return new EchoingLayer(
                Optional.of(!keyFrame.glowing()),
                Optional.empty(),
                Optional.empty()
        );
    }
    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.SMITHING_ECHOING_TWINKLE_RECIPE;
    }

    public static class Serializer implements RecipeSerializer<SmithingEchoingTwinkleRecipe> {
        private static final MapCodec<SmithingEchoingTwinkleRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) ->
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
                ).apply(instance, SmithingEchoingTwinkleRecipe::new));

        public static final PacketCodec<RegistryByteBuf, SmithingEchoingTwinkleRecipe> PACKET_CODEC =
                PacketCodec.ofStatic(
                        SmithingEchoingTwinkleRecipe.Serializer::write,
                        SmithingEchoingTwinkleRecipe.Serializer::read
                );

        public Serializer() {
        }

        public MapCodec<SmithingEchoingTwinkleRecipe> codec() {
            return CODEC;
        }

        public PacketCodec<RegistryByteBuf, SmithingEchoingTwinkleRecipe> packetCodec() {
            return PACKET_CODEC;
        }

        private static SmithingEchoingTwinkleRecipe read(RegistryByteBuf buf) {
            Ingredient template = Ingredient.PACKET_CODEC.decode(buf);
            Ingredient base = Ingredient.PACKET_CODEC.decode(buf);
            Ingredient addition = Ingredient.PACKET_CODEC.decode(buf);
            return new SmithingEchoingTwinkleRecipe(template, base, addition);
        }

        private static void write(RegistryByteBuf buf, SmithingEchoingTwinkleRecipe recipe) {
            Ingredient.PACKET_CODEC.encode(buf, recipe.getTemplate());
            Ingredient.PACKET_CODEC.encode(buf, recipe.getBase());
            Ingredient.PACKET_CODEC.encode(buf, recipe.getAddition());
        }
    }
}

