package com.technicman.ectolum.mixin;

import com.technicman.ectolum.target_access.SignAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SignBlockEntity.class)
public class SignBlockEntityMixin extends BlockEntity implements SignAccess {

	@Unique
	private boolean ectolum$hideBackground = false;

	public SignBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Inject(at = @At("HEAD"), method = "saveAdditional")
	private void writeData(ValueOutput view, CallbackInfo ci) {
		if (this.ectolum$hideBackground) view.putBoolean("ectolum.hide_background", true);
	}

	@Inject(at = @At("HEAD"), method = "loadAdditional")
	private void readData(ValueInput view, CallbackInfo ci) {
		ectolum$hideBackground = view.getBooleanOr("ectolum.hide_background", false);
	}

	@Override
	public boolean ectolum$isBackgroundHidden() {
		return ectolum$hideBackground;
	}

	@Override
	public void ectolum$setHideBackground(boolean hideBackground) {
		ectolum$hideBackground = hideBackground;
		setChanged();
	}

}