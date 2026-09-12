package com.technicman.ectolum.mixin.client;

import com.technicman.ectolum.accessor.EctolumDecoratedPotInterface;
import com.technicman.ectolum.util.GlobalVariables;
import com.technicman.ectolum.util.GlowingDecoratedPotPatterns;
import net.fabricmc.loader.impl.lib.classtweaker.utils.EntryTriple;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.block.entity.Sherds;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.DecoratedPotBlockEntityRenderer;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.data.client.BlockStateVariantMap;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

@Mixin(DecoratedPotBlockEntityRenderer.class)
public abstract class DecoratedPotBlockEntityRendererMixin {
    @Mutable
    @Final
    @Shadow
    private final ModelPart front;
    @Mutable
    @Final
    @Shadow
    private final ModelPart back;
    @Mutable
    @Final
    @Shadow
    private final ModelPart left;
    @Mutable
    @Final
    @Shadow
    private final ModelPart right;

    protected DecoratedPotBlockEntityRendererMixin(ModelPart front, ModelPart back, ModelPart left, ModelPart right) {
        this.front = front;
        this.back = back;
        this.left = left;
        this.right = right;
    }

    @Shadow
    @Nullable
    private static SpriteIdentifier getTextureIdFromSherd(Optional<Item> sherd) {
        return null;
    }

    @Shadow
    protected abstract void renderDecoratedSide(ModelPart part, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, @Nullable SpriteIdentifier textureId);


    @Inject(
            method = "render(Lnet/minecraft/block/entity/DecoratedPotBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/entity/DecoratedPotBlockEntity;getSherds()Lnet/minecraft/block/entity/Sherds;"
            ),
            cancellable = true
    )
    private void injected(DecoratedPotBlockEntity decoratedPotBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j, CallbackInfo ci) {
        Sherds sherds = decoratedPotBlockEntity.getSherds();
        EctolumDecoratedPotInterface ectolumPot = (EctolumDecoratedPotInterface) decoratedPotBlockEntity;
        Set.of(
                Triple.of(this.front, ectolumPot.FRONT, sherds.front()),
                Triple.of(this.back, ectolumPot.BACK, sherds.back()),
                Triple.of(this.left, ectolumPot.LEFT, sherds.left()),
                Triple.of(this.right, ectolumPot.RIGHT, sherds.right())
        ).forEach(t -> renderDecoratedSide(
                t.getLeft(),
                matrixStack,
                vertexConsumerProvider,
                ectolumPot.ectolum$getSherdGlow(t.getMiddle()) ? GlobalVariables.GLOW_STRENGTH : i,
                j,
                ectolum$getTextureIdFromSherd(
                        t.getRight(),
                        ectolumPot.ectolum$getSherdGlow(t.getMiddle())
                )
        ));
        matrixStack.pop();
        ci.cancel();
    }

    @Unique
    @Nullable
    private static SpriteIdentifier ectolum$getTextureIdFromSherd(Optional<Item> item, boolean glowing) {
        if (item.isPresent() && glowing) {
            SpriteIdentifier spriteIdentifier = TexturedRenderLayers.getDecoratedPotPatternTextureId(GlowingDecoratedPotPatterns.fromSherd(item.get()));
            if (spriteIdentifier != null) {
                return spriteIdentifier;
            }
        }
        return getTextureIdFromSherd(item);
    }
}
