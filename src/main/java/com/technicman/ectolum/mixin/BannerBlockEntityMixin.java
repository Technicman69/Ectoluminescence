package com.technicman.ectolum.mixin;

import com.technicman.ectolum.component.BannerEffects;
import com.technicman.ectolum.component.ModComponents;
import com.technicman.ectolum.target_access.BannerAccess;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BannerBlockEntity.class)
public class BannerBlockEntityMixin extends BlockEntity implements BannerAccess {
	@Unique
	private BannerEffects ectolum$effects = BannerEffects.EMPTY;

	public BannerBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public BannerEffects ectolum$getBannerEffects() {
		return ectolum$effects;
	}

	@Override
	public void ectolum$setBannerEffects(BannerEffects effects) {
		ectolum$effects = effects;
	}

	@Inject(at = @At("HEAD"), method = "writeData")
	private void writeData(WriteView view, CallbackInfo ci) {
		if (ectolum$effects.isGlowing()) {
			view.putBoolean("ectolum.glowing", true);
		}
		if (ectolum$effects.isBackgroundHidden()) {
			view.putBoolean("ectolum.hide_background", true);
		}
	}

	@Inject(at = @At("HEAD"), method = "readData")
	private void readData(ReadView view, CallbackInfo ci) {
		ectolum$effects = new BannerEffects(
				view.getBoolean("ectolum.glowing", false),
				view.getBoolean("ectolum.hide_background", false)
		);
	}

	@Inject(at = @At("TAIL"), method = "readComponents")
	private void readComponents(ComponentsAccess components, CallbackInfo ci) {
		ectolum$effects = components.getOrDefault(ModComponents.BANNER_EFFECTS, BannerEffects.EMPTY);
	}

	@Inject(at = @At("TAIL"), method = "addComponents")
	private void addComponents(ComponentMap.Builder componentMapBuilder, CallbackInfo ci) {
		componentMapBuilder.add(ModComponents.BANNER_EFFECTS, ectolum$getBannerEffects());
	}

	@Inject(at = @At("TAIL"), method = "removeFromCopiedStackData")
	private void removeFromCopiedStackData(WriteView view, CallbackInfo ci) {
		view.remove("ectolum.glowing");
		view.remove("ectolum.hide_background");
	}
}