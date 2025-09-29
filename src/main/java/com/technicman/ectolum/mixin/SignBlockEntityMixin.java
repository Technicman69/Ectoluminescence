package com.technicman.ectolum.mixin;

import com.technicman.ectolum.addon.EctolumSignInterface;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SignBlockEntity.class)
public class SignBlockEntityMixin extends BlockEntity implements EctolumSignInterface {

	@Unique
	private boolean ectolum$hideBackground = false;

	public SignBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Inject(at = @At("HEAD"), method = "writeNbt")
	private void writeNbt(NbtCompound nbt, CallbackInfo info) {
		if (this.ectolum$hideBackground) {
			nbt.putBoolean("ectolum.hide_background", true);
		}
	}

	@Inject(at = @At("HEAD"), method = "readNbt")
	private void readNbt(NbtCompound nbt, CallbackInfo info) {
		ectolum$hideBackground = nbt.contains("ectolum.hide_background") && nbt.getBoolean("ectolum.hide_background");
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