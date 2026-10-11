package com.technicman.ectolum.mixin;

import com.technicman.ectolum.component.ModComponents;
import com.technicman.ectolum.target_access.DecoratedPotAccess;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Mixin(DecoratedPotBlockEntity.class)
public class DecoratedPotBlockEntityMixin implements DecoratedPotAccess {
    @Unique
    private List<Boolean> ectolum$sherdGlowOverrides = ectolum$getEmptyGlowOverrides();

    @Unique
    private static List<Boolean> ectolum$getEmptyGlowOverrides() {
        return List.of(false, false, false, false);
    }

    @Override
    public boolean ectolum$getSherdGlow(int index) {
        return ectolum$sherdGlowOverrides.get(index);
    }

    @Override
    public void ectolum$setSherdGlow(int index, boolean glowing) {
        ectolum$sherdGlowOverrides = new ArrayList<>(ectolum$sherdGlowOverrides);
        ectolum$sherdGlowOverrides.set(index, glowing);
    }

    @Inject(at = @At("TAIL"), method = "writeData")
    private void writeData(WriteView view, CallbackInfo ci) {
        if (
                ectolum$getSherdGlow(0) ||
                ectolum$getSherdGlow(1) ||
                ectolum$getSherdGlow(2) ||
                ectolum$getSherdGlow(3)
        ) {
            view.putByteArray(SHERD_GLOW_OVERRIDES_KEY, new byte[]{
                    (byte) (ectolum$getSherdGlow(0) ? 1 : 0),
                    (byte) (ectolum$getSherdGlow(1) ? 1 : 0),
                    (byte) (ectolum$getSherdGlow(2) ? 1 : 0),
                    (byte) (ectolum$getSherdGlow(3) ? 1 : 0)
            });
        }
    }

    @Inject(at = @At("TAIL"), method = "readData")
    private void readData(ReadView view, CallbackInfo ci) {
        if (view != null) {
            Optional<byte[]> optionalOverrides = view.getOptionalByteArray(SHERD_GLOW_OVERRIDES_KEY);
            if (optionalOverrides.isPresent()) {
                byte[] overrides = optionalOverrides.get();
                ectolum$sherdGlowOverrides = IntStream.range(0, 4).mapToObj(i -> overrides[i] != 0).collect(Collectors.toList());
            }
        } else {
            ectolum$sherdGlowOverrides = ectolum$getEmptyGlowOverrides();
        }
    }

    @Inject(at = @At("TAIL"), method = "addComponents")
    protected void addComponents(ComponentMap.Builder componentMapBuilder, CallbackInfo ci) {
        componentMapBuilder.add(ModComponents.SHERD_GLOW_OVERRIDES, ectolum$sherdGlowOverrides);
    }

    @Inject(at = @At("TAIL"), method = "readComponents")
    protected void readComponents(ComponentsAccess components, CallbackInfo ci) {
        this.ectolum$sherdGlowOverrides = components.get(ModComponents.SHERD_GLOW_OVERRIDES);
        if (ectolum$sherdGlowOverrides == null) {
            ectolum$sherdGlowOverrides = ectolum$getEmptyGlowOverrides();
        }
    }

    @Inject(at = @At("TAIL"), method = "removeFromCopiedStackData")
    protected void removeFromCopiedStackData(WriteView view, CallbackInfo ci) {
        view.remove(SHERD_GLOW_OVERRIDES_KEY);
    }
}
