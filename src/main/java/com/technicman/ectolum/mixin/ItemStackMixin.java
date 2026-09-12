package com.technicman.ectolum.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.technicman.ectolum.util.EchoingLayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(
            method = "getTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;appendTooltip(Lnet/minecraft/component/ComponentType;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/item/tooltip/TooltipType;)V",
                    shift = At.Shift.AFTER
            ),
            slice = @Slice(
                    from = @At(
                            value = "FIELD",
                            target = "Lnet/minecraft/component/DataComponentTypes;TRIM:Lnet/minecraft/component/ComponentType;"
                    ),
                    to = @At(
                            value = "FIELD",
                            target = "Lnet/minecraft/component/DataComponentTypes;STORED_ENCHANTMENTS:Lnet/minecraft/component/ComponentType;"
                    )
            )
    )
    private void appendEchoingEffectsTooltip(Item.TooltipContext context, PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, @Local Consumer<Text> consumer) {
        EchoingLayer.appendTooltip((ItemStack)(Object)this, context, consumer, type);
    }
}
