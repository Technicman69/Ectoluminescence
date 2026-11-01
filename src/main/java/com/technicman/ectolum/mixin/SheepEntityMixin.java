package com.technicman.ectolum.mixin;

import com.technicman.ectolum.target_access.SheepAccess;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Sheep.class)
public abstract class SheepEntityMixin extends Animal implements SheepAccess {

	@Unique
	private static final EntityDataAccessor<Boolean> GLOWING =
			SynchedEntityData.defineId(Sheep.class, EntityDataSerializers.BOOLEAN);

	protected SheepEntityMixin(EntityType<? extends Animal> entityType, Level world) {
		super(entityType, world);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Animal;mobInteract(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;"), method = "mobInteract", cancellable = true)
	private void interactMob (Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
		ItemStack itemStack = player.getItemInHand(hand);
		if (itemStack.is(Items.GLOW_INK_SAC)) {
			if (!this.level().isClientSide() && !ectolum$isGlowing()) {
				ectolum$setGlowing(true);

				if (player instanceof ServerPlayer serverPlayer) {
					CriteriaTriggers.PLAYER_INTERACTED_WITH_ENTITY.trigger(serverPlayer, itemStack, this);
				}
				this.gameEvent(GameEvent.ENTITY_INTERACT, player);
				this.level().playSound(null, this, SoundEvents.GLOW_INK_SAC_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
				player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
				itemStack.shrink(1);
				cir.setReturnValue(InteractionResult.SUCCESS_SERVER);
			} else {
				cir.setReturnValue(InteractionResult.CONSUME);
			}
		}
	}

	@Inject(method = "defineSynchedData", at = @At("TAIL"))
	private void initDataTracker(SynchedEntityData.Builder builder, CallbackInfo ci) {
		builder.define(GLOWING, false);
	}

	public boolean ectolum$isGlowing() {
		return this.entityData.get(GLOWING);
	}

	public void ectolum$setGlowing(boolean glowing) {
		this.entityData.set(GLOWING, glowing);
	}

	@Inject(at = @At("HEAD"), method = "addAdditionalSaveData")
	private void writeCustomData(ValueOutput view, CallbackInfo ci) {
		if (ectolum$isGlowing()) {
			view.putBoolean("ectolum.glowing", true);
		}
	}

	@Inject(at = @At("HEAD"), method = "readAdditionalSaveData")
	private void readCustomData(ValueInput view, CallbackInfo ci) {
		ectolum$setGlowing(view.getBooleanOr("ectolum.glowing", false));
	}
}