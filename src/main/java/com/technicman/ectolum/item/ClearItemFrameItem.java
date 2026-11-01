package com.technicman.ectolum.item;

import com.technicman.ectolum.entity.ClearItemFrameEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemFrameItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class ClearItemFrameItem extends ItemFrameItem {
    public ClearItemFrameItem(EntityType<? extends HangingEntity> entityType, Properties settings) {
        super(entityType, settings);
    }

    public InteractionResult useOn(UseOnContext context) {
        BlockPos blockPos = context.getClickedPos();
        Direction direction = context.getClickedFace();
        BlockPos blockPos2 = blockPos.relative(direction);
        Player playerEntity = context.getPlayer();
        ItemStack itemStack = context.getItemInHand();
        if (playerEntity != null && !this.mayPlace(playerEntity, direction, itemStack, blockPos2)) {
            return InteractionResult.FAIL;
        } else {
            Level world = context.getLevel();
            HangingEntity abstractDecorationEntity = new ClearItemFrameEntity(world, blockPos2, direction);

            EntityType.createDefaultStackConfig(world, itemStack, playerEntity).accept(abstractDecorationEntity);
            if (abstractDecorationEntity.survives()) {
                if (!world.isClientSide()) {
                    abstractDecorationEntity.playPlacementSound();
                    world.gameEvent(playerEntity, GameEvent.ENTITY_PLACE, abstractDecorationEntity.position());
                    world.addFreshEntity(abstractDecorationEntity);
                }

                itemStack.shrink(1);
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.CONSUME;
            }
        }
    }
}
