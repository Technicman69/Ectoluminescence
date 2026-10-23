package com.technicman.ectolum;

import com.technicman.ectolum.block.ModBlocks;
import com.technicman.ectolum.component.BannerEffects;
import com.technicman.ectolum.component.ModComponents;
import com.technicman.ectolum.entity.ModEntities;
import com.technicman.ectolum.item.ModItemGroups;
import com.technicman.ectolum.item.ModItems;
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
		ModLootTableModifiers.onInitialize();
		ModComponents.onInitialize();

		LOGGER.info("Hello Fabric world!");
	}

	public static Identifier id(String name) {
		return Identifier.of(MOD_ID, name);
	}

	private static int getDecoratedPotFaceIndex(Direction hitDirection, Direction sideFacing) {
		if (hitDirection.getAxis().equals(Direction.Axis.Y)) {
			return -1;
		} else if (hitDirection.equals(sideFacing)) {
			return DecoratedPotAccess.BACK;
		} else if (hitDirection.rotateYClockwise().equals(sideFacing)) {
			return DecoratedPotAccess.LEFT;
		} else if (hitDirection.rotateYCounterclockwise().equals(sideFacing)) {
			return DecoratedPotAccess.RIGHT;
		} else {
			return DecoratedPotAccess.FRONT;
		}
	}

	static {
		UseBlockCallback.EVENT.register((player, world, hand, hit) -> {
			ItemStack heldStack = player.getStackInHand(hand);
			boolean hasGlowInkSac = heldStack.isOf(Items.GLOW_INK_SAC) && player.canModifyBlocks();
			boolean hasPhantomMembrane = heldStack.isOf(Items.PHANTOM_MEMBRANE) && player.canModifyBlocks();
			BlockPos pos = hit.getBlockPos();
			BlockEntity blockEntity = world.getBlockEntity(pos);
            switch (blockEntity) {
                case BannerBlockEntity bannerBlockEntity -> {
                    // Process Banner use

                    if (world.isClient()) {
                        return hasGlowInkSac || hasPhantomMembrane ? ActionResult.SUCCESS : ActionResult.CONSUME;
                    }
					BannerAccess banner = ((BannerAccess) bannerBlockEntity);
                    BannerEffects effects = banner.ectolum$getBannerEffects();
                    if (hasPhantomMembrane && !effects.isBackgroundHidden()) {
                        world.playSound(null, pos, ModSounds.PHANTOM_MEMBRANE_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        banner.ectolum$setBannerEffects(effects.withHideBackground(true));
                        blockEntity.markDirty();
                    } else if (hasGlowInkSac && !effects.isGlowing()) {
                        world.playSound(null, pos, SoundEvents.ITEM_GLOW_INK_SAC_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        banner.ectolum$setBannerEffects(effects.withGlowing(true));
                        blockEntity.markDirty();
                    } else {
                        return ActionResult.PASS;
                    }
                }
                case DecoratedPotBlockEntity decoratedPotBlockEntity -> {
                    // Process Decorated Pot use

                    if (world.isClient()) {
                        return hasGlowInkSac ? ActionResult.SUCCESS : ActionResult.CONSUME;
                    }
                    DecoratedPotAccess decoratedPot = (DecoratedPotAccess) decoratedPotBlockEntity;
                    int face = getDecoratedPotFaceIndex(hit.getSide(), decoratedPotBlockEntity.getHorizontalFacing());
                    if (face != -1 && !decoratedPot.ectolum$getSherdGlow(face)) {
                        world.playSound(null, pos, SoundEvents.ITEM_GLOW_INK_SAC_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        decoratedPot.ectolum$setSherdGlow(face, true);
                        blockEntity.markDirty();
                    } else {
                        return ActionResult.PASS;
                    }
                }
                case SignBlockEntity signBlockEntity -> {
                    // Process sign use

                    if (world.isClient()) {
                        return hasPhantomMembrane ? ActionResult.SUCCESS : ActionResult.CONSUME;
                    }
                    SignAccess sign = (SignAccess) signBlockEntity;
                    if (hasPhantomMembrane && !sign.ectolum$isBackgroundHidden()) {
                        sign.ectolum$setHideBackground(true);
                        world.playSound(null, signBlockEntity.getPos(), ModSounds.PHANTOM_MEMBRANE_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        signBlockEntity.markDirty();
                    } else {
                        return ActionResult.PASS;
                    }
                }
                case null, default -> {
                    // If no blocks were procesed, return default ActionResult.PASS
                    return ActionResult.PASS;
                }
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