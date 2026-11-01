package com.technicman.ectolum.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.technicman.ectolum.component.BannerEffects;
import com.technicman.ectolum.component.ModComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.ShieldDecorationRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShieldDecorationRecipe.class)
public class ShieldDecorationRecipeMixin {
    @Inject(
            method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/core/component/DataComponents;BANNER_PATTERNS:Lnet/minecraft/core/component/DataComponentType;"
            )
    )
    private void copyBannerEffectsComponent(CraftingInput craftingRecipeInput, HolderLookup.Provider wrapperLookup, CallbackInfoReturnable<ItemStack> cir, @Local(ordinal = 0) ItemStack originalStack, @Local(ordinal = 1) ItemStack copyStack) {
        BannerEffects effects = originalStack.get(ModComponents.BANNER_EFFECTS);
        if (effects != null) {
            copyStack.set(ModComponents.BANNER_EFFECTS, effects);
        }
    }
}
