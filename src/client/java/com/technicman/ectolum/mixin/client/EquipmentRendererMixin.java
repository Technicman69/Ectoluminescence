package com.technicman.ectolum.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Function3;
import com.technicman.ectolum.EctoluminescenceClient;
import com.technicman.ectolum.component.EchoingEffects;
import com.technicman.ectolum.component.ModComponents;
import com.technicman.ectolum.util.EchoingKeyframe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(EquipmentLayerRenderer.class)
public class EquipmentRendererMixin {
    @Final
    @Shadow
    private Function<EquipmentLayerRenderer.TrimSpriteKey, TextureAtlasSprite> trimSpriteLookup;

    @Unique
    private static final RenderType ECHOING_TRIMS_RENDER_LAYER = RenderTypes.armorTranslucent(Sheets.ARMOR_TRIMS_SHEET);

    @Inject(
            method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;II)V",
            at = @At(value = "FIELD", target = "Lnet/minecraft/core/component/DataComponents;TRIM:Lnet/minecraft/core/component/DataComponentType;", opcode = Opcodes.GETSTATIC),
            cancellable = true
    )
    private <S> void render(EquipmentClientInfo.LayerType layerType, ResourceKey<EquipmentAsset> equipmentAssetId, Model<? super S> model, S state, ItemStack itemStack, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, @Nullable Identifier playerTextureOverride, int outlineColor, int order, CallbackInfo ci, @Local(argsOnly = true, name = "outlineColor") int j) {
        ArmorTrim armorTrim = itemStack.get(DataComponents.TRIM);
        if (armorTrim != null) {
            // Trim rendering function
            Function3<EchoingKeyframe, Float, Integer, Void> renderTrim = (keyframe, alphaBlend, o) -> {
                if (keyframe.hidden()) return null;
                TextureAtlasSprite sprite = trimSpriteLookup.apply(new EquipmentLayerRenderer.TrimSpriteKey(keyframe.toTrim(armorTrim), layerType, equipmentAssetId));
                submitNodeCollector.order(o).submitModel(
                        model,
                        state,
                        poseStack,
                        ECHOING_TRIMS_RENDER_LAYER,
                        keyframe.glowing() ? EctoluminescenceClient.GLOW_STRENGTH : lightCoords,
                        OverlayTexture.NO_OVERLAY,
                        ARGB.multiplyAlpha(0xFFFFFFFF, alphaBlend),
                        sprite,
                        outlineColor,
                        null
                );
                return null;
            };

            // Find the keyframes, and smooth transition between them
            Level world = Minecraft.getInstance().level;
            if (world != null) {
                EchoingEffects echoingEffects = itemStack.get(ModComponents.ECHOING_EFFECTS);
                if (echoingEffects != null && !echoingEffects.layers().isEmpty()) {
                    long time = world.getGameTime();
                    float t = Math.floorMod(time, 10L) * 0.1f;
                    float w = (Mth.cos(Mth.PI * t) + 1) * 0.5f;

                    Tuple<EchoingKeyframe, EchoingKeyframe> pair = EchoingKeyframe.buildKeyFrames(itemStack, Math.floorDiv(time, 10L));
                    if (pair.getB().hidden()) {
                        renderTrim.apply(pair.getA(), w, ++j);
                    } else {
                        renderTrim.apply(pair.getA(), 1.0f, ++j);
                        renderTrim.apply(pair.getB(), 1.0f - w, ++j);
                    }
                    ci.cancel();
                    return;
                }
            }
            renderTrim.apply(EchoingKeyframe.of(armorTrim), 1.0f, ++j);
        }
        ci.cancel();
    }
}
