package com.technicman.ectolum.mixin;

import com.technicman.ectolum.addon.EctolumDecoratedPotInterface;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(DecoratedPotBlockEntity.class)
public class DecoratedPotBlockEntityMixin implements EctolumDecoratedPotInterface {
    @Unique
    private byte[] ectolum$sherdGlowOverrides = new byte[4];

    @Override
    public boolean ectolum$getSherdGlow(int index) {
        return ectolum$sherdGlowOverrides[index] != (byte)0;
    }

    @Override
    public void ectolum$setSherdGlow(int index, boolean glowing) {
        ectolum$sherdGlowOverrides[index] = glowing ? (byte)1 : (byte)0;
    }

    @Inject(at = @At("HEAD"), method = "writeNbt")
    private void writeNbt(NbtCompound nbt, CallbackInfo info) {
        if (
                ectolum$getSherdGlow(0) ||
                ectolum$getSherdGlow(1) ||
                ectolum$getSherdGlow(2) ||
                ectolum$getSherdGlow(3)
        ) {
            nbt.putByteArray("ectolum.sherd_glow_overrides", ectolum$sherdGlowOverrides);
        }
    }

    @Inject(at = @At("HEAD"), method = "readNbt")
    private void readNbt(NbtCompound nbt, CallbackInfo info) {
        ectolum$readSherdGlowOverrides(nbt);
    }

    @Inject(method = "readNbtFromStack", at = @At("HEAD"))
    private void readNbtFromStack(ItemStack stack, CallbackInfo ci) {
        ectolum$readSherdGlowOverrides(BlockItem.getBlockEntityNbt(stack));
    }

    @Unique
    private void ectolum$readSherdGlowOverrides(NbtCompound nbt) {
        if (nbt != null && nbt.contains("ectolum.sherd_glow_overrides", NbtCompound.BYTE_ARRAY_TYPE)) {
            ectolum$sherdGlowOverrides = nbt.getByteArray("ectolum.sherd_glow_overrides");
        } else {
            ectolum$sherdGlowOverrides = new byte[4];
        }
    }
}
