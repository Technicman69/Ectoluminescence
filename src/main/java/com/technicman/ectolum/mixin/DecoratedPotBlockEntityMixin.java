package com.technicman.ectolum.mixin;

import com.technicman.ectolum.component.ModComponents;
import com.technicman.ectolum.target_access.DecoratedPotAccess;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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

    @Inject(at = @At("TAIL"), method = "saveAdditional")
    private void writeData(ValueOutput output, CallbackInfo ci) {
        if (
                ectolum$getSherdGlow(0) ||
                ectolum$getSherdGlow(1) ||
                ectolum$getSherdGlow(2) ||
                ectolum$getSherdGlow(3)
        ) {
            output.putByteArray(SHERD_GLOW_OVERRIDES_KEY, new byte[]{
                    (byte) (ectolum$getSherdGlow(0) ? 1 : 0),
                    (byte) (ectolum$getSherdGlow(1) ? 1 : 0),
                    (byte) (ectolum$getSherdGlow(2) ? 1 : 0),
                    (byte) (ectolum$getSherdGlow(3) ? 1 : 0)
            });
        }
    }

    @Inject(at = @At("TAIL"), method = "loadAdditional")
    private void readData(ValueInput input, CallbackInfo ci) {
        if (input != null) {
            Optional<byte[]> optionalOverrides = input.getOptionalByteArray(SHERD_GLOW_OVERRIDES_KEY);
            optionalOverrides.ifPresent(overrides ->
                    ectolum$sherdGlowOverrides = IntStream.range(0, 4)
                            .mapToObj(i -> overrides[i] != 0)
                            .collect(Collectors.toList()));
        } else {
            ectolum$sherdGlowOverrides = ectolum$getEmptyGlowOverrides();
        }
    }

    @Inject(at = @At("TAIL"), method = "collectImplicitComponents")
    protected void addComponents(DataComponentMap.Builder components, CallbackInfo ci) {
        components.set(ModComponents.SHERD_GLOW_OVERRIDES, ectolum$sherdGlowOverrides);
    }

    @Inject(at = @At("TAIL"), method = "applyImplicitComponents")
    protected void readComponents(DataComponentGetter components, CallbackInfo ci) {
        this.ectolum$sherdGlowOverrides = components.get(ModComponents.SHERD_GLOW_OVERRIDES);
        if (ectolum$sherdGlowOverrides == null) {
            ectolum$sherdGlowOverrides = ectolum$getEmptyGlowOverrides();
        }
    }

    @Inject(at = @At("TAIL"), method = "removeComponentsFromTag")
    protected void removeFromCopiedStackData(ValueOutput output, CallbackInfo ci) {
        output.discard(SHERD_GLOW_OVERRIDES_KEY);
    }
}
