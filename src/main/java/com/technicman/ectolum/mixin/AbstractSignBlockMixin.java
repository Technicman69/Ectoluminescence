package com.technicman.ectolum.mixin;

import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractSignBlock.class)
public abstract class AbstractSignBlockMixin {
    @Shadow protected abstract boolean isOtherPlayerEditing(PlayerEntity player, SignBlockEntity blockEntity);
    //TODO needed?

//    @Inject(method = "onUseWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockEntity(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/entity/BlockEntity;"), cancellable = true)
//    private void onUse(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
//        ItemStack itemStack = player.getStackInHand(hand);
//        if (itemStack.isOf(Items.PHANTOM_MEMBRANE)) {
//            BlockEntity blockEntity = world.getBlockEntity(pos);
//            if (
//                    blockEntity instanceof SignBlockEntity signBlockEntity &&
//                    signBlockEntity instanceof SignAccess ectolumSign &&
//                    !world.isClient() &&
//                    !ectolumSign.ectolum$isBackgroundHidden() &&
//                    !this.isOtherPlayerEditing(player, signBlockEntity) &&
//                    player.canModifyBlocks()
//            ) {
//                ectolumSign.ectolum$setHideBackground(true);
//                world.playSound(null, signBlockEntity.getPos(), ModSounds.PHANTOM_MEMBRANE_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
//                signBlockEntity.markDirty();
//                world.updateListeners(pos, state, signBlockEntity.getCachedState(), 0);
//
//                if (!player.isCreative()) {
//                    itemStack.decrement(1);
//                }
//
//                world.emitGameEvent(GameEvent.BLOCK_CHANGE, signBlockEntity.getPos(), GameEvent.Emitter.of(player, signBlockEntity.getCachedState()));
//                player.incrementStat(Stats.USED.getOrCreateStat(itemStack.getItem()));
//                cir.setReturnValue(ActionResult.SUCCESS);
//            }
//        }
//    }
}
