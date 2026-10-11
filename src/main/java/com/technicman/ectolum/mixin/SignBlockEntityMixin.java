package com.technicman.ectolum.mixin;

import com.technicman.ectolum.target_access.SignAccess;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
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

	@Inject(at = @At("HEAD"), method = "writeData")
	private void writeData(WriteView view, CallbackInfo ci) {
		if (this.ectolum$hideBackground) view.putBoolean("ectolum.hide_background", true);
	}

	@Inject(at = @At("HEAD"), method = "readData")
	private void readData(ReadView view, CallbackInfo ci) {
		ectolum$hideBackground = view.getBoolean("ectolum.hide_background", false);
	}

	@Override
	public boolean ectolum$isBackgroundHidden() {
		return ectolum$hideBackground;
	}

	@Override
	public void ectolum$setHideBackground(boolean hideBackground) {
		ectolum$hideBackground = hideBackground;
		markDirty();
	}

}