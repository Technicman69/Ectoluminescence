package com.technicman.ectolum.addon;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.technicman.ectolum.Ectoluminescence;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.item.trim.ArmorTrimMaterials;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.Optional;

public interface EctolumArmorTrimInterface {

    record EchoingLayer(Optional<RegistryEntry<ArmorTrimMaterial>> material, Optional<Boolean> glowing, Optional<Boolean> hidden) {
        public static final Text ECHOING_EFFECT_TEXT;
        private static final Text GLOWING_TEXT;
        private static final Text HIDDEN_TEXT;

        public static final Codec<EchoingLayer> CODEC = RecordCodecBuilder.create((i) -> i.group(
                ArmorTrimMaterial.ENTRY_CODEC.optionalFieldOf("material").forGetter(EchoingLayer::material),
                Codec.BOOL.optionalFieldOf("glowing").forGetter(EchoingLayer::glowing),
                Codec.BOOL.optionalFieldOf("hidden").forGetter(EchoingLayer::hidden)
        ).apply(i, EchoingLayer::new));

        public static Identifier id(String name) {
            return new Identifier(Ectoluminescence.MOD_ID, name);
        }
        public static MutableText addition() {
            return Text.literal(" + ");
        }
        public static MutableText subtraction() {
            return Text.literal(" - ");
        }
        public static MutableText subtractionLineStart() {
            return Text.literal("- ");
        }
        public static Text getTextForMaterial(ArmorTrimMaterial material) {
            return Text.translatable(Util.createTranslationKey("echoing_effect", id("material")), material.description());
        }
        public static void addNecessarySeparator(MutableText text, boolean lineStart, boolean value) {
            if (lineStart) {
                if (!value) {
                    text.append(subtractionLineStart());
                }
            } else {
                if (value) {
                    text.append(addition());
                } else {
                    text.append(subtraction());
                }
            }
        }

        public Text getDescription() {
            MutableText description = Text.empty().formatted(Formatting.GRAY);
            boolean lineStart = true;
            if (material.isPresent()) {
                ArmorTrimMaterial material = this.material.get().value();
                description.setStyle(material.description().getStyle());
                description.append(getTextForMaterial(material));
                lineStart = false;
            }
            if (glowing.isPresent()) {
                addNecessarySeparator(description, lineStart, glowing.get());
                description.append(GLOWING_TEXT);
                lineStart = false;
            }
            if (hidden.isPresent()) {
                addNecessarySeparator(description, lineStart, hidden.get());
                description.append(HIDDEN_TEXT);
                lineStart = false;
            }
            return description;
        }

        @Override
        public String toString() {
            return material.map(armorTrimMaterialRegistryEntry -> "EchoingLayer{" +
                    "material=" + armorTrimMaterialRegistryEntry.value() +
                    ", glowing=" + glowing +
                    ", hidden=" + hidden +
                    '}').orElseGet(() -> "EchoingLayer{" +
                    "glowing=" + glowing +
                    ", hidden=" + hidden +
                    '}');
        }

        static {
            ECHOING_EFFECT_TEXT = Text.translatable(Util.createTranslationKey("item", id("smithing_template.echoing_effect")));
            GLOWING_TEXT = Text.translatable(Util.createTranslationKey("echoing_effect", id("glowing")));
            HIDDEN_TEXT = Text.translatable(Util.createTranslationKey("echoing_effect", id("hidden")));
        }
    }
    boolean ectolum$isGlowing();
    void ectolum$setGlowing(boolean glowing);
    boolean ectolum$hidden();
    void ectolum$setHidden(boolean glowing);

    EchoingLayer[] ectolum$getEchoingLayers();
    void ectolum$setEchoingLayers(EchoingLayer[] layers);
    boolean ectolum$hasEchoingLayers();
    Pair<ArmorTrim, ArmorTrim> ectolum$calculateTrimsAtInterval(long time);
}
