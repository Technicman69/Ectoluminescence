package com.technicman.ectolum;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.Map;

public class ModModelModifier implements ModelLoadingPlugin {
    @Override
    public void initialize(@NonNull Context pluginContext) {
        pluginContext.modifyModelOnLoad().register(((model, context) -> {
            String path = context.id().getPath();
            if (/*path.startsWith("item/") &&*/ path.endsWith("_trim")) {
                Map<String, TextureSlots.SlotContents> newTextures = new HashMap<>();
                model.textureSlots().values().forEach((s, slotContents) -> {
                    newTextures.put(s, new TextureSlots.Value(new Material(switch(slotContents) {
                        case TextureSlots.Value value ->
                                value.material().sprite();
                        case TextureSlots.Reference reference ->
                                Identifier.parse(reference.target());
                    }, true)));
                });

                model.textureSlots().values = Map.copyOf(newTextures);
            }
            return model;
        }));
    }
}
