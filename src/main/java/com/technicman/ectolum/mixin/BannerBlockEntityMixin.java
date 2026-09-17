package com.technicman.ectolum.mixin;

import com.technicman.ectolum.accessor.EctolumBannerInterface;
import com.technicman.ectolum.component.ModComponents;
import com.technicman.ectolum.util.BannerEffects;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BannerBlockEntity.class)
public class BannerBlockEntityMixin extends BlockEntity implements EctolumBannerInterface {
	@Unique
	private BannerEffects ectolum$effects = BannerEffects.empty();

	public BannerBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public BannerEffects ectolum$getBannerEffects() {
		return ectolum$effects;
	}

	@Inject(at = @At("HEAD"), method = "writeNbt")
	private void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup, CallbackInfo ci) {
		if (ectolum$effects.isGlowing()) {
			nbt.putBoolean("ectolum.glowing", true);
		}
		if (ectolum$effects.isBackgroundHidden()) {
			nbt.putBoolean("ectolum.hide_background", true);
		}
	}

	@Inject(at = @At("HEAD"), method = "readNbt")
	private void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup, CallbackInfo ci) {
		ectolum$effects.setHideBackground(nbt.contains("ectolum.hide_background") && nbt.getBoolean("ectolum.hide_background"));
		ectolum$effects.setGlowing(nbt.contains("ectolum.glowing") && nbt.getBoolean("ectolum.glowing"));
	}

	@Inject(at = @At("TAIL"), method = "readComponents")
	private void readComponents(ComponentsAccess components, CallbackInfo ci) {
		ectolum$effects = components.getOrDefault(ModComponents.BANNER_EFFECTS, BannerEffects.empty());
	}

	@Inject(at = @At("TAIL"), method = "addComponents")
	private void addComponents(ComponentMap.Builder componentMapBuilder, CallbackInfo ci) {
		componentMapBuilder.add(ModComponents.BANNER_EFFECTS, ectolum$getBannerEffects());
	}

	@Inject(at = @At("TAIL"), method = "removeFromCopiedStackNbt")
	private void removeFromCopiedStackNbt(NbtCompound nbt, CallbackInfo ci) {
		nbt.remove("ectolum.glowing");
		nbt.remove("ectolum.hide_background");
	}
}