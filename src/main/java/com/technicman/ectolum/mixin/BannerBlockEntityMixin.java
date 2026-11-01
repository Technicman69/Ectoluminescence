package com.technicman.ectolum.mixin;

import com.technicman.ectolum.component.BannerEffects;
import com.technicman.ectolum.component.ModComponents;
import com.technicman.ectolum.target_access.BannerAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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

	@Inject(at = @At("HEAD"), method = "saveAdditional")
	private void writeData(ValueOutput output, CallbackInfo ci) {
		if (ectolum$effects.isGlowing()) {
			output.putBoolean("ectolum.glowing", true);
		}
		if (ectolum$effects.isBackgroundHidden()) {
			output.putBoolean("ectolum.hide_background", true);
		}
	}

	@Inject(at = @At("HEAD"), method = "loadAdditional")
	private void readData(ValueInput input, CallbackInfo ci) {
		ectolum$effects = new BannerEffects(
				input.getBooleanOr("ectolum.glowing", false),
				input.getBooleanOr("ectolum.hide_background", false)
		);
	}

	@Inject(at = @At("TAIL"), method = "applyImplicitComponents")
	private void readComponents(DataComponentGetter components, CallbackInfo ci) {
		ectolum$effects = components.getOrDefault(ModComponents.BANNER_EFFECTS, BannerEffects.EMPTY);
	}

	@Inject(at = @At("TAIL"), method = "collectImplicitComponents")
	private void addComponents(DataComponentMap.Builder components, CallbackInfo ci) {
		components.set(ModComponents.BANNER_EFFECTS, ectolum$getBannerEffects());
	}

	@Inject(at = @At("TAIL"), method = "removeComponentsFromTag")
	private void removeFromCopiedStackData(ValueOutput output, CallbackInfo ci) {
		output.discard("ectolum.glowing");
		output.discard("ectolum.hide_background");
	}
}