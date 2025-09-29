package com.technicman.ectolum.mixin;

import com.technicman.ectolum.addon.EctolumBannerInterface;
import com.technicman.ectolum.addon.EctolumDecoratedPotInterface;
import com.technicman.ectolum.sound.ModSounds;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BannerBlockEntity.class)
public class BannerBlockEntityMixin extends BlockEntity implements EctolumBannerInterface {
	@Unique
	private boolean ectolum$glowing = false;
	@Unique
	private boolean ectolum$hideBackground = false;

	public BannerBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Inject(at = @At("HEAD"), method = "writeNbt")
	private void writeNbt(NbtCompound nbt, CallbackInfo info) {
		if (this.ectolum$glowing) {
			nbt.putBoolean("ectolum.glowing", true);
		}
		if (this.ectolum$hideBackground) {
			nbt.putBoolean("ectolum.hide_background", true);
		}
	}

	@Inject(at = @At("HEAD"), method = "readNbt")
	private void readNbt(NbtCompound nbt, CallbackInfo info) {
		ectolum$hideBackground = nbt.contains("ectolum.hide_background") && nbt.getBoolean("ectolum.hide_background");
		ectolum$glowing = nbt.contains("ectolum.glowing") && nbt.getBoolean("ectolum.glowing");
	}

	@Override
	public boolean ectolum$isGlowing() {
		return ectolum$glowing;
	}

	@Override
	public boolean ectolum$isBackgroundHidden() {
		return ectolum$hideBackground;
	}

	@Override
	public void ectolum$setGlowing(boolean glowing) {
		ectolum$glowing = glowing;
		markDirty();
	}

	@Override
	public void ectolum$setHideBackground(boolean hideBackground) {
		ectolum$hideBackground = hideBackground;
		markDirty();
	}
}