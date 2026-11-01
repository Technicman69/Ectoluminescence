package com.technicman.ectolum.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
import net.minecraft.world.level.block.entity.DecoratedPotPatterns;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DecoratedPotPatterns.class)
public class DecoratedPotPatternsMixin {
    @WrapOperation(
            method = "bootstrap",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/entity/DecoratedPotPatterns;register(Lnet/minecraft/core/Registry;Lnet/minecraft/resources/ResourceKey;Ljava/lang/String;)Lnet/minecraft/world/level/block/entity/DecoratedPotPattern;")
    )
    private static DecoratedPotPattern registerGlowingPatterns(Registry<DecoratedPotPattern> registry, ResourceKey<DecoratedPotPattern> id, String assetId, Operation<DecoratedPotPattern> original) {
        ResourceKey<DecoratedPotPattern> glowingKey = ResourceKey.create(registry.key(), id.identifier().withSuffix("_glowing"));
        String glowingId = assetId + "_glowing";
        original.call(registry, glowingKey, glowingId);
        return original.call(registry, id, assetId);
    }
}
