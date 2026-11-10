package com.technicman.ectolum.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.technicman.ectolum.component.BannerEffects;
import com.technicman.ectolum.component.ModComponents;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.ShieldDecorationRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShieldDecorationRecipe.class)
public class ShieldDecorationRecipeMixin {
    @Unique
    private BannerEffects ectolum$copiedEffects = null;

    @WrapOperation(
            method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;)Lnet/minecraft/world/item/ItemStack;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;"
            )
    )
    private Object copyBannerEffectsComponent(ItemStack instance, DataComponentType<?> dataComponentType, Operation<Object> original) {
        ectolum$copiedEffects = instance.get(ModComponents.BANNER_EFFECTS);
        return original.call(instance, dataComponentType);
    }

    @WrapOperation(
            method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;)Lnet/minecraft/world/item/ItemStack;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/crafting/TransmuteRecipe;createWithOriginalComponents(Lnet/minecraft/world/item/ItemStackTemplate;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;"
            )
    )
    private ItemStack pasteBannerEffectsComponent(ItemStackTemplate target, ItemStack input, Operation<ItemStack> original) {
        ItemStack result = original.call(target, input);
        if (ectolum$copiedEffects != null) {
            result.set(ModComponents.BANNER_EFFECTS, ectolum$copiedEffects);
        }
        return result;
    }
}
