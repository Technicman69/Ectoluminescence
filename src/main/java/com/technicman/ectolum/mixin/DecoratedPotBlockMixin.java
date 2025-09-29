package com.technicman.ectolum.mixin;

import com.technicman.ectolum.addon.EctolumDecoratedPotInterface;
import net.minecraft.block.DecoratedPotBlock;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
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
    private static void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options, CallbackInfo ci) {
        NbtCompound nbt = BlockItem.getBlockEntityNbt(stack);
        DecoratedPotBlockEntity.Sherds sherds = DecoratedPotBlockEntity.Sherds.fromNbt(nbt);
        byte[] glowOverrides;
        if (nbt != null && nbt.contains(EctolumDecoratedPotInterface.SHERD_GLOW_OVERRIDES_KEY, NbtCompound.BYTE_ARRAY_TYPE)) {
            glowOverrides = nbt.getByteArray(EctolumDecoratedPotInterface.SHERD_GLOW_OVERRIDES_KEY);
        } else {
            glowOverrides = new byte[]{0,0,0,0};
        }
//        Stream.of(sherds.front(), sherds.left(), sherds.right(), sherds.back()).for((sherd) -> {
//            tooltip.add((new ItemStack(sherd, 1)).getName().copyContentOnly().formatted(Formatting.GRAY));
//        });
        tooltip.add((new ItemStack(sherds.front(), 1)).getName().copyContentOnly().formatted(glowOverrides[EctolumDecoratedPotInterface.FRONT] != 0 ? Formatting.WHITE : Formatting.GRAY));
        tooltip.add((new ItemStack(sherds.left(), 1)).getName().copyContentOnly().formatted(glowOverrides[EctolumDecoratedPotInterface.LEFT] != 0 ? Formatting.WHITE : Formatting.GRAY));
        tooltip.add((new ItemStack(sherds.right(), 1)).getName().copyContentOnly().formatted(glowOverrides[EctolumDecoratedPotInterface.RIGHT] != 0 ? Formatting.WHITE : Formatting.GRAY));
        tooltip.add((new ItemStack(sherds.back(), 1)).getName().copyContentOnly().formatted(glowOverrides[EctolumDecoratedPotInterface.BACK] != 0 ? Formatting.WHITE : Formatting.GRAY));
        ci.cancel();
    }
}
