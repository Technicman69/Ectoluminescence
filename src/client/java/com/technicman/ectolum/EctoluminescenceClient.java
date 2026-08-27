package com.technicman.ectolum;

import com.technicman.ectolum.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.ItemFrameEntityRenderer;

public class EctoluminescenceClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(ModEntities.CLEAR_ITEM_FRAME, ItemFrameEntityRenderer::new);
	}
}