package com.technicman.ectolum.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.technicman.ectolum.Ectoluminescence;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.technicman.ectolum.Ectoluminescence.ECHOING_LAYER_LIMIT;
import static net.minecraft.network.chat.CommonComponents.space;

public record EchoingEffects(List<EchoingLayer> layers) implements TooltipProvider {
    private static final Component GLOWING_BASE;
    private static final Component HIDDEN_BASE;
    public static final Component ECHOING_EFFECT;
    private static final Component SEPARATION;
    private static final Component BLANK;
    private static final Component GLOWING_ON;
    private static final Component GLOWING_OFF;
    private static final Component HIDDEN_ON;
    private static final Component HIDDEN_OFF;
    private static final Function<Holder<TrimMaterial>, Component> MATERIAL;

    public static final Codec<EchoingEffects> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, EchoingEffects> STREAM_CODEC;

    public EchoingEffects add(EchoingLayer newLayer) {
        if (layers.size() >= ECHOING_LAYER_LIMIT)
            throw new ArrayIndexOutOfBoundsException("Echoing layer limit exceeded. It's " + ECHOING_LAYER_LIMIT);
        return new EchoingEffects(Stream.concat(layers.stream(), Stream.of(newLayer)).toList());
    }

    public EchoingEffects twinkle(boolean glowing) {
        return add(new EchoingLayer(Optional.of(glowing), Optional.empty(), Optional.empty()));
    }

    public EchoingEffects fade(boolean hidden) {
        return add(new EchoingLayer(Optional.empty(), Optional.of(hidden), Optional.empty()));
    }

    public EchoingEffects pigment(Holder<TrimMaterial> material) {
        return add(new EchoingLayer(Optional.empty(), Optional.empty(), Optional.of(material)));
    }

    public record EchoingLayer(
            Optional<Boolean> glowing,
            Optional<Boolean> hidden,
            Optional<Holder<TrimMaterial>> material
    ) {
        public static final Codec<EchoingLayer> CODEC = RecordCodecBuilder.create((builder) -> builder.group(
                Codec.BOOL.optionalFieldOf("glowing").forGetter(EchoingLayer::glowing),
                Codec.BOOL.optionalFieldOf("hidden").forGetter(EchoingLayer::hidden),
                TrimMaterial.CODEC.optionalFieldOf("material").forGetter(EchoingLayer::material)
        ).apply(builder, EchoingLayer::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, EchoingLayer> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.optional(ByteBufCodecs.BOOL), EchoingLayer::glowing,
                ByteBufCodecs.optional(ByteBufCodecs.BOOL), EchoingLayer::hidden,
                ByteBufCodecs.optional(TrimMaterial.STREAM_CODEC), EchoingLayer::material,
                EchoingLayer::new
        );

        public Component getDescription() {
            MutableComponent description = Component.empty();
            ArrayList<Component> components = new ArrayList<>();
            material.ifPresent(trimMaterial -> {
                description.setStyle(trimMaterial.value().description().getStyle());
                components.add(MATERIAL.apply(trimMaterial));
            });
            glowing.ifPresent(isGlowing -> components.add(isGlowing ? GLOWING_ON : GLOWING_OFF));
            hidden.ifPresent(isHidden -> components.add(isHidden ? HIDDEN_ON : HIDDEN_OFF));

            if (components.isEmpty()) {
                return BLANK;
            }
            description.append(components.getFirst());
            for (int i = 1; i < components.size(); i++) {
                description.append(SEPARATION);
                description.append(components.get(i));
            }
            return description;
        }
    }

    public static EchoingEffects empty() {
        return new EchoingEffects(List.of(new EchoingLayer(Optional.empty(), Optional.empty(), Optional.empty())));
    }

    public static Identifier id(String name) {
        return Ectoluminescence.id(name);
    }

    public static MutableComponent addition() {
        return Component.literal(" + ");
    }

    @Override
    public void addToTooltip(Item.@NonNull TooltipContext context, @NonNull Consumer<Component> consumer, @NonNull TooltipFlag flag, @NonNull DataComponentGetter components) {
        ArmorTrim armorTrim = components.get(DataComponents.TRIM);
        if (armorTrim != null && !layers.isEmpty()) {
            EchoingLayer first = layers.getFirst();
            Style color = first.material.orElseGet(armorTrim::material).value().description().getStyle();

            first.material.ifPresent(trimMaterial ->
                consumer.accept(space().append(MATERIAL.apply(trimMaterial)).withStyle(color)));
            if (first.glowing.isPresent() && first.glowing.get()) {
                consumer.accept(space().append(GLOWING_BASE).withStyle(color));
            }
            if (first.hidden.isPresent() && first.hidden.get()) {
                consumer.accept(space().append(HIDDEN_BASE).withStyle(color));
            }

            if (layers.size() > 1) {
                consumer.accept(space().append(ECHOING_EFFECT).withStyle(ChatFormatting.GRAY));
                Style lastColor = color;
                for (ListIterator<EchoingLayer> it = layers.listIterator(1); it.hasNext(); ) {
                    Component description = it.next().getDescription();
                    consumer.accept(space().withStyle(lastColor).append(space()).append(description));
                    if (description.getStyle() != Style.EMPTY) lastColor = description.getStyle();
                }
            }
        }
    }

    static {
        CODEC = EchoingLayer.CODEC.listOf().xmap(EchoingEffects::new, EchoingEffects::layers);
        STREAM_CODEC = EchoingLayer.STREAM_CODEC.apply(ByteBufCodecs.list()).map(EchoingEffects::new, EchoingEffects::layers);

        GLOWING_BASE = Component.translatable(Util.makeDescriptionId(
            "item", id("smithing_template.glowing")
        ));
        HIDDEN_BASE = Component.translatable(Util.makeDescriptionId(
                "item", id("smithing_template.glowing")
        ));
        ECHOING_EFFECT = Component.translatable(Util.makeDescriptionId("item", id("smithing_template.echoing_effect")));

        SEPARATION = Component.translatable(Util.makeDescriptionId("echoing_effect", id("separation")));
        BLANK = Component.translatable(Util.makeDescriptionId("echoing_effect", id("blank")));

        GLOWING_ON = Component.translatable(Util.makeDescriptionId("echoing_effect", id("glowing.on")));
        GLOWING_OFF = Component.translatable(Util.makeDescriptionId("echoing_effect", id("glowing.off")));
        HIDDEN_ON = Component.translatable(Util.makeDescriptionId("echoing_effect", id("hidden.on")));
        HIDDEN_OFF = Component.translatable(Util.makeDescriptionId("echoing_effect", id("hidden.off")));

        MATERIAL = material ->
                Component.translatable(Util.makeDescriptionId("echoing_effect", id("material")), material.value().description());
    }
}
