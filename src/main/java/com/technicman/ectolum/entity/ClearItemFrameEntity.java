package com.technicman.ectolum.entity;

import com.technicman.ectolum.item.ModItems;
import com.technicman.ectolum.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ClearItemFrameEntity extends ItemFrame {
    public ClearItemFrameEntity(EntityType<? extends ItemFrame> entityType, Level world) {
        super(entityType, world);
    }

    public ClearItemFrameEntity(Level world, BlockPos blockPos, Direction direction) {
        super(ModEntities.CLEAR_ITEM_FRAME, world, blockPos, direction);
    }

    public void setItem(ItemStack value, boolean update) {
        this.setInvisible(!value.isEmpty());
        super.setItem(value, update);
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

    protected ItemStack getFrameItemStack() {
        return new ItemStack(ModItems.CLEAR_ITEM_FRAME);
    }
}

