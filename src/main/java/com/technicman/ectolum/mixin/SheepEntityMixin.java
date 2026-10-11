package com.technicman.ectolum.mixin;

import com.technicman.ectolum.target_access.SheepAccess;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SheepEntity.class)
public abstract class SheepEntityMixin extends AnimalEntity implements SheepAccess {

	@Unique
	private static final TrackedData<Boolean> GLOWING =
			DataTracker.registerData(SheepEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	protected SheepEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/AnimalEntity;interactMob(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;"), method = "interactMob", cancellable = true)
	private void interactMob (PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
		ItemStack itemStack = player.getStackInHand(hand);
		if (itemStack.isOf(Items.GLOW_INK_SAC)) {
			if (!this.getEntityWorld().isClient()) {
				if (ectolum$isGlowing()) {
					cir.setReturnValue(ActionResult.SUCCESS);
					return;
				}
				ectolum$setGlowing(true);

				if (player instanceof ServerPlayerEntity serverPlayer) {
					Criteria.PLAYER_INTERACTED_WITH_ENTITY.trigger(serverPlayer, itemStack, this);
				}
				this.emitGameEvent(GameEvent.ENTITY_INTERACT, player);
				this.getEntityWorld().playSoundFromEntity(null, this, SoundEvents.ITEM_GLOW_INK_SAC_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
				player.incrementStat(Stats.USED.getOrCreateStat(itemStack.getItem()));
				itemStack.decrement(1);
				cir.setReturnValue(ActionResult.SUCCESS);
			} else {
				cir.setReturnValue(ActionResult.CONSUME);
			}
		}
	}

	@Inject(method = "initDataTracker", at = @At("TAIL"))
	private void initDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
		builder.add(GLOWING, false);
	}

	public boolean ectolum$isGlowing() {
		return this.dataTracker.get(GLOWING);
	}

	public void ectolum$setGlowing(boolean glowing) {
		this.dataTracker.set(GLOWING, glowing);
	}

	@Inject(at = @At("HEAD"), method = "writeCustomData")
	private void writeCustomData(WriteView view, CallbackInfo ci) {
		if (ectolum$isGlowing()) {
			view.putBoolean("ectolum.glowing", true);
		}
	}

	@Inject(at = @At("HEAD"), method = "readCustomData")
	private void readCustomData(ReadView view, CallbackInfo ci) {
		ectolum$setGlowing(view.getBoolean("ectolum.glowing", false));
	}
}