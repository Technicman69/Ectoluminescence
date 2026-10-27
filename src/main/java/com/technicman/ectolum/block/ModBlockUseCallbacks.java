package com.technicman.ectolum.block;

import com.technicman.ectolum.component.BannerEffects;
import com.technicman.ectolum.sound.ModSounds;
import com.technicman.ectolum.target_access.BannerAccess;
import com.technicman.ectolum.target_access.DecoratedPotAccess;
import com.technicman.ectolum.target_access.SignAccess;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.event.GameEvent;

public class ModBlockUseCallbacks {
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

    public static void onInitialize() {
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
