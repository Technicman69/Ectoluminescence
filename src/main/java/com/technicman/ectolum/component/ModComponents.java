package com.technicman.ectolum.component;

import com.mojang.serialization.Codec;
import com.technicman.ectolum.Ectoluminescence;
import com.technicman.ectolum.util.BannerEffects;
import com.technicman.ectolum.util.EchoingLayer;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.*;

import java.util.List;
import java.util.function.UnaryOperator;

public class ModComponents {
    public static void onInitialize() {
    }

    public static final ComponentType<Boolean> SIGN_HIDE_BACKGROUND = registerComponent(
            "sign_hide_background", builder -> builder
                    .codec(Codec.BOOL)
                    .packetCodec(PacketCodecs.BOOL)
                    .cache());

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
                    .codec(EchoingLayer.CODEC.sizeLimitedListOf(Ectoluminescence.ECHOING_LAYER_LIMIT))
                    .packetCodec(EchoingLayer.PACKET_CODEC.collect(PacketCodecs.toList()))
                    .cache());


    public static final List<Boolean> DEFAULT_SHERD_GLOW_OVERRIDES = List.of(false, false, false, false);

    private static <T> ComponentType<T> registerComponent(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Ectoluminescence.identifier(id), builderOperator.apply(ComponentType.builder()).build());
    }
}
