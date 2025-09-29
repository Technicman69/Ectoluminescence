package com.technicman.ectolum.mixin.client;

import com.mojang.datafixers.util.Pair;
import com.technicman.ectolum.EctoluminescenceClient;
import com.technicman.ectolum.addon.EctolumArmorTrimInterface;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

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

    @Shadow
    protected abstract boolean usesInnerModel(EquipmentSlot slot);

    @Redirect(
            method = "renderArmor",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/item/trim/ArmorTrim;getTrim(Lnet/minecraft/registry/DynamicRegistryManager;Lnet/minecraft/item/ItemStack;)Ljava/util/Optional;"
            )
    )
    private Optional<ArmorTrim> injected(DynamicRegistryManager registryManager, ItemStack stack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, EquipmentSlot armorSlot, int light, A model) {
        ItemStack itemStack = entity.getEquippedStack(armorSlot);
        Item item = itemStack.getItem();
        if (item instanceof ArmorItem armorItem) {
            boolean bl = this.usesInnerModel(armorSlot);
            long time = entity.getWorld().getTime();
            float t = Math.floorMod(time, 10L) * 0.1f;
            float w = ((MathHelper.cos(3.1415927F * t) + 1) * 0.5f);
            ArmorTrim.getTrim(entity.getWorld().getRegistryManager(), itemStack).ifPresent((trim) -> {
                EctolumArmorTrimInterface ectolumTrim = (EctolumArmorTrimInterface) trim;
                if (ectolumTrim.ectolum$hasEchoingLayers()) {
                    Pair<ArmorTrim, ArmorTrim> pair = ectolumTrim.ectolum$calculateTrimsAtInterval(Math.floorDiv(time, 10L));
                    if (((EctolumArmorTrimInterface) pair.getSecond()).ectolum$hidden()) {
                        ectolum$renderTrim(armorItem.getMaterial(), matrices, vertexConsumers, light, w, pair.getFirst(), model, bl);
                        return;
                    }
                    ectolum$renderTrim(armorItem.getMaterial(), matrices, vertexConsumers, light, 1.0f, pair.getFirst(), model, bl);
                    ectolum$renderTrim(armorItem.getMaterial(), matrices, vertexConsumers, light, 1.0f-w, pair.getSecond(), model, bl);
                } else {
                    ectolum$renderTrim(armorItem.getMaterial(), matrices, vertexConsumers, light, 1.0f, trim, model, bl);
                }
            });
        }
        return Optional.empty();
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
    private void ectolum$renderTrim(ArmorMaterial material, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float alpha, ArmorTrim trim, A model, boolean leggings) {
        EctolumArmorTrimInterface ectolumTrim = (EctolumArmorTrimInterface) trim;
        if (ectolumTrim.ectolum$hidden()) {
            return;
        }
        Sprite sprite = this.armorTrimsAtlas.getSprite(leggings ? trim.getLeggingsModelId(material) : trim.getGenericModelId(material));
        VertexConsumer vertexConsumer = sprite.getTextureSpecificVertexConsumer(vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TexturedRenderLayers.ARMOR_TRIMS_ATLAS_TEXTURE)));
        model.render(matrices, vertexConsumer, ectolumTrim.ectolum$isGlowing() ? EctoluminescenceClient.GLOW_STRENGTH : light, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, alpha);
    }
}
