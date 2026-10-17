package com.technicman.ectolum.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.technicman.ectolum.block.GlowingBannerBlock;
import net.minecraft.block.BannerBlock;
import net.minecraft.block.Block;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(BannerBlock.class)
public class BannerBlockMixin {
    @WrapOperation(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;")
    )
    private Object preventMapping(Map<DyeColor, Block> instance, Object dyeColor, Object block, Operation<Object> original) {
        if (block instanceof GlowingBannerBlock) {
            return null;
        }
        return original.call(instance, dyeColor, block);
    }
}
