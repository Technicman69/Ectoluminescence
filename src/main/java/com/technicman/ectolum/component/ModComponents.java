package com.technicman.ectolum.component;

import com.mojang.serialization.Codec;
import com.technicman.ectolum.Ectoluminescence;
import net.fabricmc.fabric.api.item.v1.ComponentTooltipAppenderRegistry;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.List;
import java.util.function.UnaryOperator;

public class ModComponents {
    public static void onInitialize() {
        ComponentTooltipAppenderRegistry.addAfter(DataComponentTypes.BANNER_PATTERNS, BANNER_EFFECTS);
    }

    public static final ComponentType<List<Boolean>> SHERD_GLOW_OVERRIDES = registerComponent(
            "sherd_glow_overrides", builder -> builder
                    .codec(Codec.list(Codec.BOOL))
                    .packetCodec(PacketCodec.of(
                            (value, buf) -> value.forEach(buf::writeBoolean),
                            buf -> List.of(buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean())
                    ))
                    .cache());

    public static final ComponentType<BannerEffects> BANNER_EFFECTS = registerComponent(
            "banner_effects", builder -> builder
                    .codec(BannerEffects.CODEC)
                    .packetCodec(BannerEffects.PACKET_CODEC)
                    .cache());

    public static final ComponentType<List<EchoingLayer>> ECHOING_LAYERS = registerComponent(
            "echoing_layers", builder -> builder
                    .codec(EchoingLayer.CODEC.listOf(1, Ectoluminescence.ECHOING_LAYER_LIMIT))
                    .packetCodec(EchoingLayer.PACKET_CODEC.collect(PacketCodecs.toList()))
                    .cache());


    public static final List<Boolean> DEFAULT_SHERD_GLOW_OVERRIDES = List.of(false, false, false, false);

    private static <T> ComponentType<T> registerComponent(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Ectoluminescence.id(id), builderOperator.apply(ComponentType.builder()).build());
    }
}
