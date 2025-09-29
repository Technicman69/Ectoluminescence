package com.technicman.ectolum;

import com.technicman.ectolum.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.impl.client.model.loading.ModelLoaderHooks;
import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.client.render.model.ModelLoader;

public class EctoluminescenceClient implements ClientModInitializer {
	public static final int GLOW_STRENGTH = 15728880;
	public static final float COLOR_VALUE_MULTIPLIER = 1.2f;
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.INSTANCE.register(ModEntities.CLEAR_ITEM_FRAME, ItemFrameEntityRenderer::new);
	}
}