package com.technicman.ectolum.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Pair;
import com.technicman.ectolum.util.CanvasRenderer;
import com.technicman.ectolum.addon.EctolumBannerInterface;
import net.minecraft.block.WallBannerBlock;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BannerBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BannerBlockEntityRenderer.class)
public class BannerBlockEntityRendererMixin {
    @Unique
    private ModelPart patterns;
    @Final
    @Shadow
    private ModelPart pillar;
    @Final
    @Shadow
    private ModelPart crossbar;

    @Shadow @Final private ModelPart banner;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void BannerBlockEntityRenderer(BlockEntityRendererFactory.Context ctx, CallbackInfo ci) {
        ModelPart modelPart = ctx.getLayerModelPart(EntityModelLayers.BANNER);
        this.patterns = modelPart.getChild("patterns");
    }

    @Inject(method = "getTexturedModelData", at = @At("TAIL"))
    private static void injected(CallbackInfoReturnable<TexturedModelData> cir, @Local ModelPartData modelPartData) {
        modelPartData.addChild("patterns", ModelPartBuilder.create().uv(0, 0).cuboid(-10.0F, 0.0F, -2.0F, 20.0F, 40.0F, 1.0F), ModelTransform.NONE);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V"), method = "render(Lnet/minecraft/block/entity/BannerBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V")
	private void render(BannerBlockEntity bannerBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j, CallbackInfo info) {
        this.crossbar.visible = true;
        this.banner.visible = true;
        if (((EctolumBannerInterface) bannerBlockEntity).ectolum$isBackgroundHidden()) {
            this.pillar.visible = false;
            this.crossbar.visible = false;
            this.banner.visible = false;
        }
	}

    @Inject(
            method = "render(Lnet/minecraft/block/entity/BannerBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
            cancellable = true,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/entity/BannerBlockEntityRenderer;renderCanvas(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/util/SpriteIdentifier;ZLjava/util/List;)V")
    )
    private void renderCanvas(BannerBlockEntity bannerBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j, CallbackInfo ci) {
        List<Pair<RegistryEntry<BannerPattern>, DyeColor>> list = bannerBlockEntity.getPatterns();
        EctolumBannerInterface banner = (EctolumBannerInterface) bannerBlockEntity;
        this.patterns.pitch = this.banner.pitch;
        this.patterns.pivotY = this.banner.pivotY;
        boolean isWall = bannerBlockEntity.getCachedState().getBlock() instanceof WallBannerBlock;
        CanvasRenderer.renderCanvas(matrixStack, vertexConsumerProvider, i, j, this.banner, this.patterns, ModelLoader.BANNER_BASE, true, list, false, banner.ectolum$isGlowing(), banner.ectolum$isBackgroundHidden(), isWall);
        // Rest of code
        matrixStack.pop();
        matrixStack.pop();
        ci.cancel();
    }
}