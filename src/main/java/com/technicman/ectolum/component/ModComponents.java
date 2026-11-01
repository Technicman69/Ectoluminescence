package com.technicman.ectolum.component;

import com.mojang.serialization.Codec;
import com.technicman.ectolum.Ectoluminescence;
import net.fabricmc.fabric.api.item.v1.ItemComponentTooltipProviderRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import java.util.List;
import java.util.function.UnaryOperator;

public class ModComponents {
    public static void onInitialize() {
        ItemComponentTooltipProviderRegistry.addAfter(DataComponents.BANNER_PATTERNS, BANNER_EFFECTS);
    }

    public static final DataComponentType<List<Boolean>> SHERD_GLOW_OVERRIDES = registerComponent(
            "sherd_glow_overrides", builder -> builder
                    .persistent(Codec.list(Codec.BOOL))
                    .networkSynchronized(StreamCodec.ofMember(
                            (value, buf) -> value.forEach(buf::writeBoolean),
                            buf -> List.of(buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean())
                    ))
                    .cacheEncoding());

    public static final DataComponentType<BannerEffects> BANNER_EFFECTS = registerComponent(
            "banner_effects", builder -> builder
                    .persistent(BannerEffects.CODEC)
                    .networkSynchronized(BannerEffects.PACKET_CODEC)
                    .cacheEncoding());

    public static final DataComponentType<List<EchoingLayer>> ECHOING_LAYERS = registerComponent(
            "echoing_layers", builder -> builder
                    .persistent(EchoingLayer.CODEC.listOf(1, Ectoluminescence.ECHOING_LAYER_LIMIT))
                    .networkSynchronized(EchoingLayer.PACKET_CODEC.apply(ByteBufCodecs.list()))
                    .cacheEncoding());


    public static final List<Boolean> DEFAULT_SHERD_GLOW_OVERRIDES = List.of(false, false, false, false);

    private static <T> DataComponentType<T> registerComponent(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Ectoluminescence.id(id), builderOperator.apply(DataComponentType.builder()).build());
    }
}
