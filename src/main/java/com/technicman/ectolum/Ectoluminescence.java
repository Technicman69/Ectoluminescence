package com.technicman.ectolum;

import com.technicman.ectolum.addon.EctolumBannerInterface;
import com.technicman.ectolum.addon.EctolumDecoratedPotInterface;
import com.technicman.ectolum.block.ModBlocks;
import com.technicman.ectolum.entity.ModEntities;
import com.technicman.ectolum.item.ModItems;
import com.technicman.ectolum.recipe.ModRecipes;
import com.technicman.ectolum.sound.ModSounds;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.AbstractCandleBlock;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
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

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		ModBlocks.onInitialize();
		ModEntities.onInitialize();
		ModItems.onInitialize();
		ModSounds.onInitialize();
		ModRecipes.onInitialize();
		GlowingDecoratedPotPatterns.onInitialize();

		LOGGER.info("Hello Fabric world!");
	}

	private static int getDecoratedPotFaceIndex(Direction hitDirection, Direction sideFacing) {
		if (hitDirection.getAxis().equals(Direction.Axis.Y)) {
			return -1;
		} else if (hitDirection.equals(sideFacing)) {
			return EctolumDecoratedPotInterface.BACK;
		} else if (hitDirection.rotateYClockwise().equals(sideFacing)) {
			return EctolumDecoratedPotInterface.LEFT;
		} else if (hitDirection.rotateYCounterclockwise().equals(sideFacing)) {
			return EctolumDecoratedPotInterface.RIGHT;
		} else {
			return EctolumDecoratedPotInterface.FRONT;
		}
	}

	static {
		UseBlockCallback.EVENT.register((player, world, hand, hit) -> {
			ItemStack heldStack = player.getStackInHand(hand);
			boolean hasGlowInkSac = heldStack.isOf(Items.GLOW_INK_SAC) && player.canModifyBlocks();
			boolean hasPhantomMembrane = heldStack.isOf(Items.PHANTOM_MEMBRANE) && player.canModifyBlocks();
			BlockPos pos = hit.getBlockPos();
			BlockEntity blockEntity = world.getBlockEntity(pos);
			// Process Banner use
			if (blockEntity instanceof BannerBlockEntity bannerBlockEntity) {
				if (world.isClient) {
					return !(hasGlowInkSac || hasPhantomMembrane) ? ActionResult.CONSUME : ActionResult.SUCCESS;
				}
				EctolumBannerInterface ectolumBanner = (EctolumBannerInterface) bannerBlockEntity;
				if (hasPhantomMembrane && !ectolumBanner.ectolum$isBackgroundHidden()) {
					world.playSound(null, pos, ModSounds.PHANTOM_MEMBRANE_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
					((EctolumBannerInterface) bannerBlockEntity).ectolum$setHideBackground(true);
				} else if (hasGlowInkSac && !ectolumBanner.ectolum$isGlowing()) {
					world.playSound(null, pos, SoundEvents.ITEM_GLOW_INK_SAC_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
					((EctolumBannerInterface) bannerBlockEntity).ectolum$setGlowing(true);
				} else {
					return ActionResult.PASS;
				}
				// Process Decorated Pot use
			} else if (blockEntity instanceof DecoratedPotBlockEntity decoratedPotBlockEntity) {
				if (world.isClient) {
					return !hasGlowInkSac ? ActionResult.CONSUME : ActionResult.SUCCESS;
				}
				EctolumDecoratedPotInterface ectolumPot = (EctolumDecoratedPotInterface) decoratedPotBlockEntity;
				int face = getDecoratedPotFaceIndex(hit.getSide(), decoratedPotBlockEntity.getHorizontalFacing());
				if (face != -1 && !ectolumPot.ectolum$getSherdGlow(face)) {
					world.playSound(null, pos, SoundEvents.ITEM_GLOW_INK_SAC_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
					ectolumPot.ectolum$setSherdGlow(face, true);
				} else {
					return ActionResult.PASS;
				}
				// If no blocks were procesed, return default ActionResult.PASS
			} else {
				return ActionResult.PASS;
			}

			// If any block was processed, update stats and block and stuff
			if (player instanceof ServerPlayerEntity serverPlayer) {
				Criteria.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, heldStack);
			}
			world.emitGameEvent(GameEvent.BLOCK_CHANGE, blockEntity.getPos(), GameEvent.Emitter.of(player, blockEntity.getCachedState()));
			player.incrementStat(Stats.USED.getOrCreateStat(heldStack.getItem()));

			world.updateListeners(pos, blockEntity.getCachedState(), blockEntity.getCachedState(), 0);
			if (!player.isCreative()) {
				heldStack.decrement(1);
			}
			return ActionResult.SUCCESS;
		});
	}
}