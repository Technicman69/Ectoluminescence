package com.technicman.ectolum.entity;

import com.technicman.ectolum.item.ModItems;
import com.technicman.ectolum.sound.ModSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ClearItemFrameEntity extends ItemFrameEntity {
    public ClearItemFrameEntity(EntityType<? extends ItemFrameEntity> entityType, World world) {
        super(entityType, world);
    }

    public ClearItemFrameEntity(World world, BlockPos blockPos, Direction direction) {
        super(ModEntities.CLEAR_ITEM_FRAME, world, blockPos, direction);
    }

    public void setHeldItemStack(ItemStack value, boolean update) {
        this.setInvisible(!value.isEmpty());
        super.setHeldItemStack(value, update);
    }

    public SoundEvent getRemoveItemSound() {
        return ModSounds.ENTITY_CLEAR_ITEM_FRAME_REMOVE_ITEM;
    }

    public SoundEvent getBreakSound() {
        return ModSounds.ENTITY_CLEAR_ITEM_FRAME_BREAK;
    }

    public SoundEvent getPlaceSound() {
        return ModSounds.ENTITY_CLEAR_ITEM_FRAME_PLACE;
    }

    public SoundEvent getAddItemSound() {
        return ModSounds.ENTITY_CLEAR_ITEM_FRAME_ADD_ITEM;
    }

    public SoundEvent getRotateItemSound() {
        return ModSounds.ENTITY_CLEAR_ITEM_FRAME_ROTATE_ITEM;
    }

    protected ItemStack getAsItemStack() {
        return new ItemStack(ModItems.CLEAR_ITEM_FRAME);
    }
}

