package com.technicman.ectolum.mixin.client;

import com.technicman.ectolum.Ectoluminescence;
import com.technicman.ectolum.EctoluminescenceClient;
import com.technicman.ectolum.GlowingDecoratedPotPatterns;
import com.technicman.ectolum.addon.EctolumDecoratedPotInterface;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.block.DecoratedPotPatterns;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.DecoratedPotBlockEntityRenderer;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.core.lookup.ResourceBundleLookup;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
    private static SpriteIdentifier getTextureIdFromSherd(Item item) {
        return null;
    }

    @Shadow
    protected abstract void renderDecoratedSide(ModelPart part, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, @Nullable SpriteIdentifier textureId);


    @Inject(
            method = "render(Lnet/minecraft/block/entity/DecoratedPotBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/entity/DecoratedPotBlockEntity;getSherds()Lnet/minecraft/block/entity/DecoratedPotBlockEntity$Sherds;"
            ),
            cancellable = true
    )
    private void injected(DecoratedPotBlockEntity decoratedPotBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j, CallbackInfo ci) {
        DecoratedPotBlockEntity.Sherds sherds = decoratedPotBlockEntity.getSherds();
        EctolumDecoratedPotInterface ectolumPot = (EctolumDecoratedPotInterface) decoratedPotBlockEntity;
        Function<Integer, Integer> light = (index) -> ectolumPot.ectolum$getSherdGlow(index) ? EctoluminescenceClient.GLOW_STRENGTH : i;
        this.renderDecoratedSide(this.front, matrixStack, vertexConsumerProvider, light.apply(ectolumPot.FRONT), j, ectolum$getTextureIdFromSherd(sherds.front(), ectolumPot.ectolum$getSherdGlow(ectolumPot.FRONT)));
        this.renderDecoratedSide(this.back, matrixStack, vertexConsumerProvider, light.apply(ectolumPot.BACK), j, ectolum$getTextureIdFromSherd(sherds.back(), ectolumPot.ectolum$getSherdGlow(ectolumPot.BACK)));
        this.renderDecoratedSide(this.left, matrixStack, vertexConsumerProvider, light.apply(ectolumPot.LEFT), j, ectolum$getTextureIdFromSherd(sherds.left(), ectolumPot.ectolum$getSherdGlow(ectolumPot.LEFT)));
        this.renderDecoratedSide(this.right, matrixStack, vertexConsumerProvider, light.apply(ectolumPot.RIGHT), j, ectolum$getTextureIdFromSherd(sherds.right(), ectolumPot.ectolum$getSherdGlow(ectolumPot.RIGHT)));
        matrixStack.pop();
        ci.cancel();
    }

    @Unique
    @Nullable
    private static SpriteIdentifier ectolum$getTextureIdFromSherd(Item item, boolean glowing) {
        if (glowing) {
            SpriteIdentifier spriteIdentifier = TexturedRenderLayers.getDecoratedPotPatternTextureId(GlowingDecoratedPotPatterns.fromSherd(item));
            if (spriteIdentifier != null) {
                return spriteIdentifier;
            }
        }
        return getTextureIdFromSherd(item);
    }
}
