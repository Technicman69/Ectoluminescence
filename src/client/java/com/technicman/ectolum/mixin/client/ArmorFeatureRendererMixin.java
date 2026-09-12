package com.technicman.ectolum.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.technicman.ectolum.component.ModComponents;
import com.technicman.ectolum.util.EchoingKeyframe;
import com.technicman.ectolum.util.EchoingLayer;
import com.technicman.ectolum.util.GlobalVariables;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {
    @Mutable
    @Final
    @Shadow
    private final SpriteAtlasTexture armorTrimsAtlas;
    public ArmorFeatureRendererMixin(FeatureRendererContext<T, M> context, SpriteAtlasTexture armorTrimsAtlas) {
        super(context);
        this.armorTrimsAtlas = armorTrimsAtlas;
    }

    @Redirect(
            method = "renderArmor",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/render/entity/feature/ArmorFeatureRenderer;renderTrim(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/item/trim/ArmorTrim;Lnet/minecraft/client/render/entity/model/BipedEntityModel;Z)V"
            )
    )
    private void injected(ArmorFeatureRenderer<?, ?, ?> instance, RegistryEntry<ArmorMaterial> armorMaterial, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ArmorTrim trim, A model, boolean leggings, @Local ItemStack stack, @Local boolean bl) {
        Item item = stack.getItem();
        if (item instanceof ArmorItem armorItem) {
            PlayerEntity player = MinecraftClient.getInstance().player;
            if (player != null) {
                long time = player.getWorld().getTime();
                float t = Math.floorMod(time, 10L) * 0.1f;
                float w = -(MathHelper.cos(MathHelper.PI * t) + 1) * 0.5f;

                List<EchoingLayer> echoingLayers = stack.get(ModComponents.ECHOING_LAYERS);
                if (echoingLayers != null && !echoingLayers.isEmpty()) {
                    Pair<EchoingKeyframe, EchoingKeyframe> pair = EchoingKeyframe.buildKeyFrames(stack, Math.floorDiv(time, 10L));
                    if (pair.getRight().hidden()) {
                        ectolum$renderTrim(armorItem.getMaterial(), matrices, vertexConsumers, light, 1.0f - w, trim, pair.getLeft(), model, bl);
                        return;
                    }
                    ectolum$renderTrim(armorItem.getMaterial(), matrices, vertexConsumers, light, 1.0f, trim, pair.getLeft(), model, bl);
                    ectolum$renderTrim(armorItem.getMaterial(), matrices, vertexConsumers, light, w, trim, pair.getRight(), model, bl);
                } else {
                    ectolum$renderTrim(armorItem.getMaterial(), matrices, vertexConsumers, light, 1.0f, trim, null, model, bl);
                }
            }
        }
    }

    @Redirect(
            method = "renderArmorParts",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/RenderLayer;getArmorCutoutNoCull(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;"
            )
    )
    private RenderLayer getArmorCutoutNoCull(Identifier texture) {
        return RenderLayer.getEntityCutoutNoCull(texture);
    }

    @Unique
    private void ectolum$renderTrim(RegistryEntry<ArmorMaterial> material, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float alpha, ArmorTrim base, @Nullable EchoingKeyframe keyframe, A model, boolean leggings) {
        ArmorTrim trim;
        if (keyframe != null) {
            if (keyframe.hidden()) {
                return;
            }
            if (keyframe.glowing()) {
                light = GlobalVariables.GLOW_STRENGTH;
            }
            trim = keyframe.toTrim(base);
        } else {
            trim = base;
        }
        Sprite sprite = this.armorTrimsAtlas.getSprite(leggings ? trim.getLeggingsModelId(material) : trim.getGenericModelId(material));
        VertexConsumer vertexConsumer = sprite.getTextureSpecificVertexConsumer(vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TexturedRenderLayers.ARMOR_TRIMS_ATLAS_TEXTURE)));
        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, ColorHelper.Argb.withAlpha(ColorHelper.channelFromFloat(alpha), 0xFFFFFF));
    }
}
