package com.technicman.ectolum.item;

import com.technicman.ectolum.entity.ClearItemFrameEntity;
import com.technicman.ectolum.entity.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.GlowItemFrameEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemFrameItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Optional;

public class ClearItemFrameItem extends ItemFrameItem {
    public ClearItemFrameItem(EntityType<? extends AbstractDecorationEntity> entityType, Settings settings) {
        super(entityType, settings);
    }

    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos blockPos = context.getBlockPos();
        Direction direction = context.getSide();
        BlockPos blockPos2 = blockPos.offset(direction);
        PlayerEntity playerEntity = context.getPlayer();
        ItemStack itemStack = context.getStack();
        if (playerEntity != null && !this.canPlaceOn(playerEntity, direction, itemStack, blockPos2)) {
            return ActionResult.FAIL;
        } else {
            World world = context.getWorld();
            AbstractDecorationEntity abstractDecorationEntity = new ClearItemFrameEntity(world, blockPos2, direction);


            NbtCompound nbtCompound = itemStack.getNbt();
            if (nbtCompound != null) {
                EntityType.loadFromEntityNbt(world, playerEntity, abstractDecorationEntity, nbtCompound);
            }

            if (abstractDecorationEntity.canStayAttached()) {
                if (!world.isClient) {
                    abstractDecorationEntity.onPlace();
                    world.emitGameEvent(playerEntity, GameEvent.ENTITY_PLACE, abstractDecorationEntity.getPos());
                    world.spawnEntity(abstractDecorationEntity);
                }

                itemStack.decrement(1);
                return ActionResult.success(world.isClient);
            } else {
                return ActionResult.CONSUME;
            }
        }
    }
}
