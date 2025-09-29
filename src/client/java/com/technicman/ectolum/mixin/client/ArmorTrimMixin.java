package com.technicman.ectolum.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.technicman.ectolum.Ectoluminescence;
import com.technicman.ectolum.addon.EctolumArmorTrimInterface;
import net.fabricmc.fabric.impl.client.rendering.ArmorRendererRegistryImpl;
import net.fabricmc.fabric.mixin.client.indigo.renderer.ItemRendererMixin;
import net.fabricmc.fabric.mixin.item.ArmorItemMixin;
import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.data.server.recipe.SmithingTransformRecipeJsonBuilder;
import net.minecraft.data.server.recipe.SmithingTrimRecipeJsonBuilder;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.item.trim.ArmorTrimMaterials;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.recipe.SmithingTransformRecipe;
import net.minecraft.recipe.SmithingTrimRecipe;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Mixin(ArmorTrim.class)
public class ArmorTrimMixin implements EctolumArmorTrimInterface {
    @Shadow @Final private RegistryEntry<ArmorTrimMaterial> material;
    @Shadow @Final private RegistryEntry<ArmorTrimPattern> pattern;

    @Shadow
    public static Optional<ArmorTrim> getTrim(DynamicRegistryManager registryManager, ItemStack stack) {
        return Optional.empty();
    }

    @Unique
    private static final Text TEXT_GLOWING = Text.translatable(Util.createTranslationKey(
            "item", new Identifier(Ectoluminescence.MOD_ID, "smithing_template.glowing")
    ));
    @Unique
    private static final Text TEXT_HIDDEN = Text.translatable(Util.createTranslationKey(
            "item", new Identifier(Ectoluminescence.MOD_ID, "smithing_template.hidden")
    ));
    @Unique
    private boolean ectolum$glowing;
    @Unique
    private boolean ectolum$hidden;
    @Unique
    private EchoingLayer[] ectolum$echoingLayers;

    public ArmorTrimMixin(boolean ectolum$hidden, EchoingLayer[] ectolum$echoingLayers) {
        this.ectolum$hidden = ectolum$hidden;
        this.ectolum$echoingLayers = ectolum$echoingLayers;
    }
    @Override
    public boolean ectolum$isGlowing() {
        return ectolum$glowing;
    }
    @Override
    public void ectolum$setGlowing(boolean glowing) {
        ectolum$glowing = glowing;
    }

    @Override
    public boolean ectolum$hidden() {
        return ectolum$hidden;
    }

    @Override
    public void ectolum$setHidden(boolean hidden) {
        ectolum$hidden = hidden;
    }

    @Override
    public EchoingLayer[] ectolum$getEchoingLayers() {
        return ectolum$echoingLayers;
    }

    @Unique
    public void ectolum$setEchoingLayers(EchoingLayer[] ectolum$echoingLayers) {
        this.ectolum$echoingLayers = ectolum$echoingLayers;
    }

    @Override
    public boolean ectolum$hasEchoingLayers() {
        return ectolum$echoingLayers != null && ectolum$echoingLayers.length != 0;
    }

    @Override
    public Pair<ArmorTrim, ArmorTrim> ectolum$calculateTrimsAtInterval(long time) {
        EchoingLayer[] layers = ectolum$echoingLayers;
        int interval = (int) (time % (layers.length+1));
        if (interval == layers.length) {
            EchoingLayer firstLayer = layers[0];
            ArmorTrim other = new ArmorTrim(
                    firstLayer.material().isPresent() ? firstLayer.material().get() : material,
                    pattern
            );
            ((EctolumArmorTrimInterface) other).ectolum$setGlowing(
                    firstLayer.glowing().isPresent() ? firstLayer.glowing().get() : ectolum$glowing
            );
            ((EctolumArmorTrimInterface) other).ectolum$setHidden(
                    firstLayer.hidden().isPresent() ? firstLayer.hidden().get() : ectolum$hidden
            );
            return new Pair<>((ArmorTrim)(Object)this, other);
        }

        ArmorTrim first = null;
        ArmorTrim second = null;
        RegistryEntry<ArmorTrimMaterial> newMaterial = material;
        boolean glowing = ectolum$glowing;
        boolean hidden = ectolum$hidden;
        for (int i=0; i<=interval+1 && i<layers.length; i++) {
            EchoingLayer layer = layers[i];
            if (layer.material().isPresent()) {
                newMaterial = layer.material().get();
            }
            if (layer.glowing().isPresent()) {
                glowing = layer.glowing().get();
            }
            if (layer.hidden().isPresent()) {
                hidden = layer.hidden().get();
            }

            if (i == interval) {
                first = new ArmorTrim(newMaterial, pattern);
                ((EctolumArmorTrimInterface) first).ectolum$setGlowing(glowing);
                ((EctolumArmorTrimInterface) first).ectolum$setHidden(hidden);
            }
            if (i == interval+1) {
                second = new ArmorTrim(newMaterial, pattern);
                ((EctolumArmorTrimInterface) second).ectolum$setGlowing(glowing);
                ((EctolumArmorTrimInterface) second).ectolum$setHidden(hidden);
            }
        }
        if (interval == layers.length-1) {
            second = (ArmorTrim)(Object)this;
        }
        return new Pair<>(first, second);
    }

    @Inject(
            method = "apply",
            at = @At("RETURN")
    )
    private static void apply(DynamicRegistryManager registryManager, ItemStack stack, ArmorTrim trim, CallbackInfoReturnable<Boolean> cir) {
        if (stack.isIn(ItemTags.TRIMMABLE_ARMOR) && stack.getNbt() != null) {
            NbtCompound nbt = stack.getNbt().getCompound("Trim");
            EctolumArmorTrimInterface ectolumTrim = (EctolumArmorTrimInterface) trim;
            if (ectolumTrim.ectolum$isGlowing()) {
                nbt.putBoolean("ectolum.glowing", true);
            }
            if (ectolumTrim.ectolum$hidden()) {
                nbt.putBoolean("ectolum.hidden", true);
            }
            EchoingLayer[] layers = ectolumTrim.ectolum$getEchoingLayers();
            if (layers != null && layers.length > 0) {
                NbtList list = new NbtList();
                for (EchoingLayer layer : layers) {
                    list.add(EchoingLayer.CODEC.encodeStart(RegistryOps.of(NbtOps.INSTANCE, registryManager), layer).result().orElseThrow());
                }
                nbt.put("ectolum.echoing_layers", list);
            }
        }
    }

    @Inject(
            method = "getTrim",
            at = @At(value = "INVOKE", target = "Ljava/util/Optional;ofNullable(Ljava/lang/Object;)Ljava/util/Optional;")
    )
    private static void getTrim(DynamicRegistryManager registryManager, ItemStack stack, CallbackInfoReturnable<Optional<ArmorTrim>> cir, @Local NbtCompound nbtCompound, @Local ArmorTrim armorTrim) {
        if (nbtCompound == null || armorTrim == null) {
            return;
        }
        EctolumArmorTrimInterface ectolumTrim = (EctolumArmorTrimInterface) armorTrim;
        ectolumTrim.ectolum$setGlowing(nbtCompound.contains("ectolum.glowing") && nbtCompound.getBoolean("ectolum.glowing"));
        ectolumTrim.ectolum$setHidden(nbtCompound.contains("ectolum.hidden") && nbtCompound.getBoolean("ectolum.hidden"));
        if (nbtCompound.contains("ectolum.echoing_layers", NbtCompound.LIST_TYPE)) {
            NbtList list = nbtCompound.getList("ectolum.echoing_layers", NbtCompound.COMPOUND_TYPE);
            EchoingLayer[] layers = new EchoingLayer[list.size()];
            //Objects.requireNonNull(LOGGER);
            for (int i=0; i<list.size(); i++) {
                NbtCompound element = (NbtCompound) list.get(i);
                DataResult<EchoingLayer> data = EchoingLayer.CODEC.parse(RegistryOps.of(NbtOps.INSTANCE, registryManager), element);
                EchoingLayer layer = data.resultOrPartial(null).orElse(null);
                layers[i] = layer;
            }
            ectolumTrim.ectolum$setEchoingLayers(layers);
        }
    }

    @Inject(
            method = "appendTooltip",
            at = @At("TAIL")
    )
    private static void appendTooltip(ItemStack stack, DynamicRegistryManager registryManager, List<Text> tooltip, CallbackInfo ci) {
        Optional<ArmorTrim> optional = getTrim(registryManager, stack);
        if (optional.isPresent()) {
            ArmorTrim armorTrim = optional.get();
            EctolumArmorTrimInterface ectolumTrim = (EctolumArmorTrimInterface) armorTrim;
            Style color = armorTrim.getMaterial().value().description().getStyle();
            if (ectolumTrim.ectolum$isGlowing()) {
                tooltip.add(ScreenTexts.space().append(TEXT_GLOWING).fillStyle(color));
            }
            if (ectolumTrim.ectolum$hidden()) {
                tooltip.add(ScreenTexts.space().append(TEXT_HIDDEN).fillStyle(color));
            }
            if (ectolumTrim.ectolum$hasEchoingLayers()) {
                EchoingLayer[] echoingLayers = ectolumTrim.ectolum$getEchoingLayers();
                if (echoingLayers.length == 1) {
                    tooltip.add(ScreenTexts.space().append(EchoingLayer.ECHOING_EFFECT_TEXT).setStyle(color));
                    MutableText description = (MutableText) echoingLayers[0].getDescription();
                    if (echoingLayers[0].material().isEmpty()) {
                        description.setStyle(color);
                    }
                    tooltip.add(ScreenTexts.space().append(ScreenTexts.space()).append(description));
                } else {
                    tooltip.add(ScreenTexts.space().append(EchoingLayer.ECHOING_EFFECT_TEXT).formatted(Formatting.GRAY));
                    for (EchoingLayer layer : echoingLayers) {
                        tooltip.add(ScreenTexts.space().append(ScreenTexts.space()).append(layer.getDescription()));
                    }
                }
            }
        }
    }
}
