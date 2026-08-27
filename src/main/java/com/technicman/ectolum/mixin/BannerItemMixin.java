package com.technicman.ectolum.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.item.BannerItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BannerItem.class)
public class BannerItemMixin {
    @Inject(method = "appendBannerTooltip", at = @At("TAIL"))
    private static void appendTooltip(ItemStack stack, List<Text> tooltip, CallbackInfo ci) {
        NbtCompound nbt = BlockItem.getBlockEntityNbt(stack);
        if (nbt != null) {
            if (nbt.getBoolean("ectolum.glowing")) {
                tooltip.add(Text.translatable("block.minecraft.banner.ectolum.glowing").formatted(Formatting.WHITE));
            }
            if (nbt.getBoolean("ectolum.hide_background")) {
                tooltip.add(Text.translatable("block.minecraft.banner.ectolum.hide_background").formatted(Formatting.WHITE));
            }
        }
    }
}
