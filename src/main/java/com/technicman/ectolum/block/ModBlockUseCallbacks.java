package com.technicman.ectolum.block;

import com.technicman.ectolum.component.BannerEffects;
import com.technicman.ectolum.sound.ModSounds;
import com.technicman.ectolum.target_access.BannerAccess;
import com.technicman.ectolum.target_access.DecoratedPotAccess;
import com.technicman.ectolum.target_access.SignAccess;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.gameevent.GameEvent;

public class ModBlockUseCallbacks {
    private static int getDecoratedPotFaceIndex(Direction hitDirection, Direction sideFacing) {
        if (hitDirection.getAxis().equals(Direction.Axis.Y)) {
            return -1;
        } else if (hitDirection.equals(sideFacing)) {
            return DecoratedPotAccess.BACK;
        } else if (hitDirection.getClockWise().equals(sideFacing)) {
            return DecoratedPotAccess.LEFT;
        } else if (hitDirection.getCounterClockWise().equals(sideFacing)) {
            return DecoratedPotAccess.RIGHT;
        } else {
            return DecoratedPotAccess.FRONT;
        }
    }

    public static void onInitialize() {
        UseBlockCallback.EVENT.register((player, world, hand, hit) -> {
            ItemStack heldStack = player.getItemInHand(hand);
            boolean hasGlowInkSac = heldStack.is(Items.GLOW_INK_SAC) && player.mayBuild();
            boolean hasPhantomMembrane = heldStack.is(Items.PHANTOM_MEMBRANE) && player.mayBuild();
            BlockPos pos = hit.getBlockPos();
            BlockEntity blockEntity = world.getBlockEntity(pos);
            switch (blockEntity) {
                case BannerBlockEntity bannerBlockEntity -> {
                    // Process Banner use

                    if (world.isClientSide()) {
                        return hasGlowInkSac || hasPhantomMembrane ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
                    }
                    BannerAccess banner = ((BannerAccess) bannerBlockEntity);
                    BannerEffects effects = banner.ectolum$getBannerEffects();
                    if (hasPhantomMembrane && !effects.isBackgroundHidden()) {
                        world.playSound(null, pos, ModSounds.PHANTOM_MEMBRANE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                        banner.ectolum$setBannerEffects(effects.withHideBackground(true));
                        blockEntity.setChanged();
                    } else if (hasGlowInkSac && !effects.isGlowing()) {
                        world.playSound(null, pos, SoundEvents.GLOW_INK_SAC_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                        banner.ectolum$setBannerEffects(effects.withGlowing(true));
                        blockEntity.setChanged();
                    } else {
                        return InteractionResult.PASS;
                    }
                }
                case DecoratedPotBlockEntity decoratedPotBlockEntity -> {
                    // Process Decorated Pot use

                    if (world.isClientSide()) {
                        return hasGlowInkSac ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
                    }
                    DecoratedPotAccess decoratedPot = (DecoratedPotAccess) decoratedPotBlockEntity;
                    int face = getDecoratedPotFaceIndex(hit.getDirection(), decoratedPotBlockEntity.getDirection());
                    if (face != -1 && !decoratedPot.ectolum$getSherdGlow(face)) {
                        world.playSound(null, pos, SoundEvents.GLOW_INK_SAC_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                        decoratedPot.ectolum$setSherdGlow(face, true);
                        blockEntity.setChanged();
                    } else {
                        return InteractionResult.PASS;
                    }
                }
                case SignBlockEntity signBlockEntity -> {
                    // Process sign use

                    if (world.isClientSide()) {
                        return hasPhantomMembrane ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
                    }
                    SignAccess sign = (SignAccess) signBlockEntity;
                    if (hasPhantomMembrane && !sign.ectolum$isBackgroundHidden()) {
                        sign.ectolum$setHideBackground(true);
                        world.playSound(null, signBlockEntity.getBlockPos(), ModSounds.PHANTOM_MEMBRANE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                        signBlockEntity.setChanged();
                    } else {
                        return InteractionResult.PASS;
                    }
                }
                case null, default -> {
                    // If no blocks were procesed, return default ActionResult.PASS
                    return InteractionResult.PASS;
                }
            }

            // If any block was processed, update stats and block and stuff
            if (player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, heldStack);
            }
            world.gameEvent(GameEvent.BLOCK_CHANGE, blockEntity.getBlockPos(), GameEvent.Context.of(player, blockEntity.getBlockState()));
            player.awardStat(Stats.ITEM_USED.get(heldStack.getItem()));

            world.sendBlockUpdated(pos, blockEntity.getBlockState(), blockEntity.getBlockState(), 0);
            if (!player.isCreative()) {
                heldStack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        });
    }
}
