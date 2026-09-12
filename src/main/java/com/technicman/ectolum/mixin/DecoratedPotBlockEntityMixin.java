package com.technicman.ectolum.mixin;

import com.technicman.ectolum.Ectoluminescence;
import com.technicman.ectolum.accessor.EctolumDecoratedPotInterface;
import com.technicman.ectolum.component.ModComponents;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.block.entity.Sherds;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

@Mixin(DecoratedPotBlockEntity.class)
public class DecoratedPotBlockEntityMixin implements EctolumDecoratedPotInterface {
    @Unique
    private List<Boolean> ectolum$sherdGlowOverrides = ectolum$getEmptyGlowOverrides();

    @Unique
    private static List<Boolean> ectolum$getEmptyGlowOverrides() {
        return Stream.of(false, false, false, false).collect(Collectors.toList());
    }

    @Override
    public boolean ectolum$getSherdGlow(int index) {
        return ectolum$sherdGlowOverrides.get(index);
    }

    @Override
    public void ectolum$setSherdGlow(int index, boolean glowing) {
        ectolum$sherdGlowOverrides.set(index, glowing);
    }

    @Inject(at = @At("HEAD"), method = "writeNbt")
    private void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup, CallbackInfo ci) {
        if (
                ectolum$getSherdGlow(0) ||
                ectolum$getSherdGlow(1) ||
                ectolum$getSherdGlow(2) ||
                ectolum$getSherdGlow(3)
        ) {
            nbt.putByteArray(SHERD_GLOW_OVERRIDES_KEY, ectolum$sherdGlowOverrides.stream().map(b -> b ? (byte) 1 : (byte) 0).collect(Collectors.toUnmodifiableList()));
        }
    }

    @Inject(at = @At("HEAD"), method = "readNbt")
    private void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup, CallbackInfo ci) {
        if (nbt != null && nbt.contains(SHERD_GLOW_OVERRIDES_KEY, NbtCompound.BYTE_ARRAY_TYPE)) {
            byte[] overrides = nbt.getByteArray(SHERD_GLOW_OVERRIDES_KEY);
            ectolum$sherdGlowOverrides = IntStream.range(0, 4).mapToObj(i -> overrides[i] != 0).collect(Collectors.toList());
        } else {
            ectolum$sherdGlowOverrides = ectolum$getEmptyGlowOverrides();
        }
    }

    @Inject(at = @At("TAIL"), method = "addComponents")
    protected void addComponents(ComponentMap.Builder componentMapBuilder, CallbackInfo ci) {
        componentMapBuilder.add(ModComponents.SHERD_GLOW_OVERRIDES, ectolum$sherdGlowOverrides);
    }

    @Inject(at = @At("TAIL"), method = "readComponents")
    protected void readComponents(BlockEntity.ComponentsAccess components, CallbackInfo ci) {
        this.ectolum$sherdGlowOverrides = components.get(ModComponents.SHERD_GLOW_OVERRIDES);
        if (ectolum$sherdGlowOverrides == null) {
            ectolum$sherdGlowOverrides = ectolum$getEmptyGlowOverrides();
        }
    }

    @Inject(at = @At("TAIL"), method = "removeFromCopiedStackNbt")
    protected void removeFromCopiedStackNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.remove(SHERD_GLOW_OVERRIDES_KEY);
    }
}
