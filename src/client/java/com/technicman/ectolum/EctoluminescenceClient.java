package com.technicman.ectolum;

import com.technicman.ectolum.component.EchoingLayer;
import com.technicman.ectolum.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.SimpleUnbakedExtraModel;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.ItemFrameEntityRenderer;

public class EctoluminescenceClient implements ClientModInitializer {

	public static final int GLOW_STRENGTH = 15728880;
	public static final float COLOR_VALUE_MULTIPLIER = 1.2f;

	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(ModEntities.CLEAR_ITEM_FRAME, ItemFrameEntityRenderer::new);
		ModelLoadingPlugin.register(context -> context.addModel(
				ExtraModelKey.create(() -> "clear_item_frame"),
				SimpleUnbakedExtraModel.blockStateModel(Ectoluminescence.id("block/clear_item_frame"))
		));
		ModelLoadingPlugin.register(context -> context.addModel(
				ExtraModelKey.create(() -> "clear_item_frame_map"),
				SimpleUnbakedExtraModel.blockStateModel(Ectoluminescence.id("block/clear_item_frame_map"))
		));

		ItemTooltipCallback.EVENT.register(EchoingLayer::appendTooltip);
	}
}