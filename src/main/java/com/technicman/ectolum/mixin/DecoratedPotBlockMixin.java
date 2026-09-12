package com.technicman.ectolum.mixin;

import com.technicman.ectolum.accessor.EctolumDecoratedPotInterface;
import com.technicman.ectolum.component.ModComponents;
import net.minecraft.block.DecoratedPotBlock;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.block.entity.Sherds;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.stream.Stream;

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
        List<Boolean> glowOverrides = stack.getOrDefault(ModComponents.SHERD_GLOW_OVERRIDES, ModComponents.DEFAULT_SHERD_GLOW_OVERRIDES);
        Stream.of(
                new Pair<>(sherds.front(), EctolumDecoratedPotInterface.FRONT),
                new Pair<>(sherds.left(), EctolumDecoratedPotInterface.LEFT),
                new Pair<>(sherds.right(), EctolumDecoratedPotInterface.RIGHT),
                new Pair<>(sherds.back(), EctolumDecoratedPotInterface.BACK)
        ).forEach(
                pair -> tooltip.add((new ItemStack(pair.getLeft().orElse(Items.BRICK), 1)).getName().copyContentOnly().formatted(glowOverrides.get(pair.getRight()) ? Formatting.WHITE : Formatting.GRAY))
        );
        ci.cancel();
    }
}
