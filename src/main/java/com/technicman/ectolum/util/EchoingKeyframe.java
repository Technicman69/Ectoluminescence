package com.technicman.ectolum.util;

import com.technicman.ectolum.component.EchoingEffects;
import com.technicman.ectolum.component.ModComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import org.jetbrains.annotations.Nullable;

import java.util.ListIterator;

import static com.technicman.ectolum.component.EchoingEffects.EchoingLayer;

public record EchoingKeyframe(
        boolean glowing,
        boolean hidden,
        Holder<TrimMaterial> material
) {
    public static final boolean DEFAULT_GLOWING = false;
    public static final boolean DEFAULT_HIDDEN = false;

    public static EchoingKeyframe build(EchoingEffects echoingEffects, int index) {
        ListIterator<EchoingLayer> iterator = echoingEffects.layers().listIterator(index+1);

        @Nullable Boolean glowing1 = null;
        @Nullable Boolean hidden1 = null;
        @Nullable Holder<TrimMaterial> material1 = null;
        while (iterator.hasPrevious()) {
            EchoingLayer layer = iterator.previous();
            int nullCount = 0;

            if (glowing1 == null) if (layer.glowing().isPresent()) {
                glowing1 = layer.glowing().get();
            } else {
                nullCount++;
            }

            if (hidden1 == null) if (layer.hidden().isPresent()) {
                hidden1 = layer.hidden().get();
            } else {
                nullCount++;
            }

            if (material1 == null) if (layer.material().isPresent()) {
                material1 = layer.material().get();
            } else {
                nullCount++;
            }

            if (nullCount == 0) {
                return new EchoingKeyframe(glowing1, hidden1, material1);
            }
        }
        return new EchoingKeyframe(Boolean.TRUE.equals(glowing1), Boolean.TRUE.equals(hidden1), material1);
    }

    public static EchoingKeyframe buildNext(EchoingKeyframe previous, EchoingEffects echoingEffects, int nextIndex) {
        if (nextIndex >= echoingEffects.layers().size()) {
            EchoingLayer layer = echoingEffects.layers().getFirst();
            return new EchoingKeyframe(
                    layer.glowing().isPresent()? layer.glowing().get() : DEFAULT_GLOWING,
                    layer.hidden().isPresent()? layer.hidden().get() : DEFAULT_HIDDEN,
                    layer.material().isPresent()? layer.material().get() : null
            );
        }
        EchoingLayer nextLayer = echoingEffects.layers().get(nextIndex);
        return new EchoingKeyframe(
                nextLayer.glowing().isPresent()? nextLayer.glowing().get() : previous.glowing(),
                nextLayer.hidden().isPresent()? nextLayer.hidden().get() : previous.hidden(),
                nextLayer.material().isPresent()? nextLayer.material().get() : previous.material()
        );
    }

    public static EchoingKeyframe buildLast(ItemStack item) {
        if (item.has(ModComponents.ECHOING_EFFECTS)) {
            EchoingEffects echoingEffects = item.get(ModComponents.ECHOING_EFFECTS);
            assert echoingEffects != null;
            EchoingKeyframe keyframe =  build(echoingEffects, echoingEffects.layers().size()-1);
            if (keyframe.material == null) {
                ArmorTrim trim = item.get(DataComponents.TRIM);
                assert trim != null;
                return new EchoingKeyframe(keyframe.glowing, keyframe.hidden, trim.material());
            }
            return keyframe;
        }
        ArmorTrim trim = item.get(DataComponents.TRIM);
        assert trim != null;
        return new EchoingKeyframe(DEFAULT_GLOWING, DEFAULT_HIDDEN, trim.material());
    }

    public static Tuple<EchoingKeyframe, EchoingKeyframe> buildKeyFrames(ItemStack item, long time) {
        EchoingEffects echoingEffects = item.get(ModComponents.ECHOING_EFFECTS);
        assert echoingEffects != null;
        int index = (int) (time % (echoingEffects.layers().size()));

        EchoingKeyframe first = build(echoingEffects, index);
        EchoingKeyframe second = buildNext(first, echoingEffects, index+1);

        ArmorTrim trim = item.get(DataComponents.TRIM);
        assert trim != null;
        if (first.material == null) {
            first = new EchoingKeyframe(first.glowing, first.hidden, trim.material());
        }
        if (second.material == null) {
            second = new EchoingKeyframe(second.glowing, second.hidden, trim.material());
        }

        return new Tuple<>(first, second);
    }

    public static EchoingKeyframe of(ArmorTrim armorTrim) {
        return new EchoingKeyframe(false, false, armorTrim.material());
    }

    public boolean equals(Object o) {
        if (!(o instanceof EchoingKeyframe(boolean glowing1, boolean hidden1, Holder<TrimMaterial> material1))) {
            return false;
        } else {
            return this.glowing == glowing1
                    && this.hidden == hidden1
                    && this.material.equals(material1);
        }
    }

    public int hashCode() {
        int i = this.material.hashCode();
        i = 31 * i + (this.glowing ? 1 : 0);
        i = 31 * i + (this.hidden ? 1 : 0);
        return i;
    }

    public ArmorTrim toTrim(ArmorTrim base) {
        return new ArmorTrim(
                material,
                base.pattern()
        );
    }
}
