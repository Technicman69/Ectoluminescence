package com.technicman.ectolum.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.technicman.ectolum.component.ModComponents;
import com.technicman.ectolum.mixin.client.accessor.ItemRenderStateAccessor;
import com.technicman.ectolum.util.EchoingKeyframe;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import static net.minecraft.core.component.DataComponents.TRIM;

@Mixin(ItemModelResolver.class)
public class ItemModelManagerMixin {
    @Unique
    public void ectolum$applyEchoingKeyFrame(ItemStack stack, EchoingKeyframe keyframe, ArmorTrim original) {
        if (keyframe.hidden()) {
            stack.remove(TRIM);
        } else {
            stack.set(TRIM, keyframe.toTrim(original));
        }
    }

    @Unique
    public void ectolum$setOpacity(ItemStackRenderState state, float alpha, boolean renderBase) {
        ItemStackRenderState.LayerRenderState[] layers = ((ItemRenderStateAccessor) state).ectolum$getLayers();
        ItemStackRenderState.LayerRenderState lastLayer = layers[layers.length-1];
        // Increase tints length
        int[] tints = lastLayer.prepareTintLayers(lastLayer.prepareTintLayers(-1).length + 2);

        // Add new opacity tint
        // (hide armor completely and make "top echoing layer trim" transparent)
        int tintArmorBase = ARGB.white(ARGB.as8BitChannel(renderBase ? 1f : 0f));
        int tintTrim = ARGB.white(ARGB.as8BitChannel(alpha));
        // The tints should be empty so I could've used just indexes 0 and 1
        // but other mods and resourcepacks could change something in the models or
        // the rendering itself so...
        // I'm actually not sure if that changes anything...
        tints[tints.length-2] = tintArmorBase;
        tints[tints.length-1] = tintTrim;
        state.appendModelIdentityElement(tintArmorBase);
        state.appendModelIdentityElement(tintTrim);
    }

    @WrapOperation(
            method = "appendItemLayers",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/item/ItemModel;update(Lnet/minecraft/client/renderer/item/ItemStackRenderState;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/renderer/item/ItemModelResolver;Lnet/minecraft/world/item/ItemDisplayContext;Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/world/entity/ItemOwner;I)V")
    )
    private void updateEchoingLayers(ItemModel instance, ItemStackRenderState state, ItemStack stack, ItemModelResolver itemModelManager, ItemDisplayContext itemDisplayContext, ClientLevel world, ItemOwner heldItemContext, int i, Operation<Void> original) {
        if (world != null && stack.has(TRIM) && stack.has(ModComponents.ECHOING_LAYERS)) {
            ArmorTrim savedTrim = stack.get(TRIM);

            long worldTime = world.getGameTime();
            float t = Math.floorMod(worldTime, 10L) * 0.1f;
            long time = Math.floorDiv(worldTime, 10L);
            Tuple<EchoingKeyframe, EchoingKeyframe> pair = EchoingKeyframe.buildKeyFrames(stack, time);

            ectolum$applyEchoingKeyFrame(stack, pair.getLeft(), savedTrim);
            original.call(instance, state, stack, itemModelManager, itemDisplayContext, world, heldItemContext, i);
            // Check if layers are different. If so, overlay one on top of the other
            if (
                    pair.getLeft().hidden() != pair.getRight().hidden() ||
                            !pair.getLeft().material().equals(pair.getRight().material())
            ) {
                // If the top layer is invisible, it can't be overlayed with alpha,
                // so the bottom one's opacity should be changed
                if (pair.getRight().hidden()) {
                    ectolum$setOpacity(state, (Mth.cos(Mth.PI * t) + 1) * 0.5f, true);
                } else {
                    ectolum$applyEchoingKeyFrame(stack, pair.getRight(), savedTrim);
                    original.call(instance, state, stack, itemModelManager, itemDisplayContext, world, heldItemContext, i);
                    ectolum$setOpacity(state, (-Mth.cos(Mth.PI * t) + 1) * 0.5f, false);
                }
            }

            stack.set(TRIM, savedTrim);
        } else {
            original.call(instance, state, stack, itemModelManager, itemDisplayContext, world, heldItemContext, i);
        }
    }
}
