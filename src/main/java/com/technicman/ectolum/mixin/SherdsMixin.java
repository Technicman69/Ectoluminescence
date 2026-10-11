package com.technicman.ectolum.mixin;

import com.technicman.ectolum.component.ModComponents;
import com.technicman.ectolum.target_access.DecoratedPotAccess;
import net.minecraft.block.entity.Sherds;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Mixin(Sherds.class)
public abstract class SherdsMixin {
    @Shadow
    public abstract Optional<Item> front();
    @Shadow
    public abstract Optional<Item> left();
    @Shadow
    public abstract Optional<Item> right();
    @Shadow
    public abstract Optional<Item> back();

    @Inject(method = "appendTooltip", at = @At("HEAD"), cancellable = true)
    private void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components, CallbackInfo ci) {
        if (!Sherds.DEFAULT.equals(this)) {
            List<Boolean> overrides = components.getOrDefault(ModComponents.SHERD_GLOW_OVERRIDES, ModComponents.DEFAULT_SHERD_GLOW_OVERRIDES);
            textConsumer.accept(ScreenTexts.EMPTY);
            ectolum$appendSherdTooltip(textConsumer, front(), overrides.get(DecoratedPotAccess.FRONT));
            ectolum$appendSherdTooltip(textConsumer, left(), overrides.get(DecoratedPotAccess.LEFT));
            ectolum$appendSherdTooltip(textConsumer, right(), overrides.get(DecoratedPotAccess.RIGHT));
            ectolum$appendSherdTooltip(textConsumer, back(), overrides.get(DecoratedPotAccess.BACK));
        }
        ci.cancel();
    }

    @Unique
    private static void ectolum$appendSherdTooltip(Consumer<Text> textConsumer, Optional<Item> sherdItem, boolean glowing) {
        textConsumer.accept((new ItemStack(sherdItem.orElse(Items.BRICK), 1)).getName().copyContentOnly().formatted(glowing ? Formatting.WHITE : Formatting.GRAY));
    }
}
