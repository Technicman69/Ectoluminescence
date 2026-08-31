package com.technicman.ectolum.mixin;

import com.technicman.ectolum.accessor.EctolumDecoratedPotInterface;
import net.minecraft.block.DecoratedPotBlock;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.block.entity.Sherds;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(DecoratedPotBlock.class)
public class DecoratedPotBlockMixin {

    @Inject(
            method = "appendTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/stream/Stream;of([Ljava/lang/Object;)Ljava/util/stream/Stream;"
            ),
            cancellable = true
    )
    private void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options, CallbackInfo ci) {
        Sherds sherds = stack.getOrDefault(DataComponentTypes.POT_DECORATIONS, Sherds.DEFAULT);
        stack.getOrDefault()
        byte[] glowOverrides;
        if (nbt != null && nbt.contains(EctolumDecoratedPotInterface.SHERD_GLOW_OVERRIDES_KEY, NbtCompound.BYTE_ARRAY_TYPE)) {
            glowOverrides = nbt.getByteArray(EctolumDecoratedPotInterface.SHERD_GLOW_OVERRIDES_KEY);
        } else {
            glowOverrides = new byte[]{0,0,0,0};
        }
        tooltip.add((new ItemStack(sherds.front(), 1)).getName().copyContentOnly().formatted(glowOverrides[EctolumDecoratedPotInterface.FRONT] != 0 ? Formatting.WHITE : Formatting.GRAY));
        tooltip.add((new ItemStack(sherds.left(), 1)).getName().copyContentOnly().formatted(glowOverrides[EctolumDecoratedPotInterface.LEFT] != 0 ? Formatting.WHITE : Formatting.GRAY));
        tooltip.add((new ItemStack(sherds.right(), 1)).getName().copyContentOnly().formatted(glowOverrides[EctolumDecoratedPotInterface.RIGHT] != 0 ? Formatting.WHITE : Formatting.GRAY));
        tooltip.add((new ItemStack(sherds.back(), 1)).getName().copyContentOnly().formatted(glowOverrides[EctolumDecoratedPotInterface.BACK] != 0 ? Formatting.WHITE : Formatting.GRAY));
        ci.cancel();
    }
}
