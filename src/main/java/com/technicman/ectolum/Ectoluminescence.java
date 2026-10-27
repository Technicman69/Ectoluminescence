package com.technicman.ectolum;

import com.technicman.ectolum.block.ModBlockUseCallbacks;
import com.technicman.ectolum.block.ModBlocks;
import com.technicman.ectolum.component.BannerEffects;
import com.technicman.ectolum.component.ModComponents;
import com.technicman.ectolum.entity.ModEntities;
import com.technicman.ectolum.item.ModItemGroups;
import com.technicman.ectolum.item.ModItems;
import com.technicman.ectolum.loot.ModEntitySubPredicateTypes;
import com.technicman.ectolum.loot.ModLootTableModifiers;
import com.technicman.ectolum.recipe.ModRecipes;
import com.technicman.ectolum.sound.ModSounds;
import com.technicman.ectolum.target_access.BannerAccess;
import com.technicman.ectolum.target_access.DecoratedPotAccess;
import com.technicman.ectolum.target_access.SignAccess;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.event.GameEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ectoluminescence implements ModInitializer {
	public static final String MOD_ID = "ectolum";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final int ECHOING_LAYER_LIMIT = 8;


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		ModBlocks.onInitialize();
		ModEntities.onInitialize();
		ModItems.onInitialize();
		ModItemGroups.onInitialize();
		ModSounds.onInitialize();
		ModRecipes.onInitialize();
		ModEntitySubPredicateTypes.onInitialize();
		ModLootTableModifiers.onInitialize();
		ModComponents.onInitialize();
		ModBlockUseCallbacks.onInitialize();

		LOGGER.info("Hello Fabric world!");
	}

	public static Identifier id(String name) {
		return Identifier.of(MOD_ID, name);
	}
}