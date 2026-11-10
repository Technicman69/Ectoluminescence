package com.technicman.ectolum.mixin;

import com.technicman.ectolum.component.ModComponents;
import com.technicman.ectolum.target_access.DecoratedPotAccess;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.entity.PotDecorations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Mixin(PotDecorations.class)
public abstract class SherdsMixin {
    @Shadow
    public abstract Optional<Item> front();
    @Shadow
    public abstract Optional<Item> left();
    @Shadow
    public abstract Optional<Item> right();
    @Shadow
    public abstract Optional<Item> back();

    @Inject(method = "addToTooltip", at = @At("HEAD"), cancellable = true)
    private void appendTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components, CallbackInfo ci) {
        if (!PotDecorations.EMPTY.equals(this)) {
            List<Boolean> overrides = components.getOrDefault(ModComponents.SHERD_GLOW_OVERRIDES, ModComponents.DEFAULT_SHERD_GLOW_OVERRIDES);
            consumer.accept(CommonComponents.EMPTY);
            ectolum$appendSherdTooltip(consumer, front(), overrides.get(DecoratedPotAccess.FRONT));
            ectolum$appendSherdTooltip(consumer, left(), overrides.get(DecoratedPotAccess.LEFT));
            ectolum$appendSherdTooltip(consumer, right(), overrides.get(DecoratedPotAccess.RIGHT));
            ectolum$appendSherdTooltip(consumer, back(), overrides.get(DecoratedPotAccess.BACK));
        }
        ci.cancel();
    }

    @Unique
    private static void ectolum$appendSherdTooltip(Consumer<Component> textConsumer, Optional<Item> sherdItem, boolean glowing) {
        textConsumer.accept((new ItemStack(sherdItem.orElse(Items.BRICK), 1)).getHoverName().plainCopy().withStyle(glowing ? ChatFormatting.WHITE : ChatFormatting.GRAY));
    }
}
