package com.technicman.ectolum.component;

import com.technicman.ectolum.Ectoluminescence;
import net.minecraft.component.Component;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.registry.*;
import net.minecraft.text.Style;
import net.minecraft.util.dynamic.Codecs;

import java.util.function.UnaryOperator;

public class ModComponents {
    public static void onInitialize() {
    }

    public static final ComponentType<Boolean> BANNER_BACKGROUND_HIDDEN = registerComponent(
            "animal", builder -> builder.persistent(Codecs.BOO).networkSynchronized(Component.PACKET_CODEC).cacheEncoding());

    private static <T> ComponentType<T> registerComponent(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Ectoluminescence.identifier(id), builderOperator.apply(ComponentType.builder()).build());
    }
}
