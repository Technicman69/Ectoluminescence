package com.technicman.ectolum.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.BitSet;

public class BannerEffectsComponent {
    public static final Codec<BannerEffectsComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.BOOL.optionalFieldOf("glowing", false).forGetter(BannerEffectsComponent::isGlowing),
            Codec.BOOL.optionalFieldOf("hide_background", false).forGetter(BannerEffectsComponent::isBackgroundHidden)
    ).apply(builder, BannerEffectsComponent::new));

    private static final int GLOWING = 0;
    private static final int HIDE_BACKGROUND = 1;

    private final BitSet flags = new BitSet(2);

    public BannerEffectsComponent(boolean glowing, boolean hideBackground) {
        setGlowing(glowing);
        setHideBackground(hideBackground);
    }

    public boolean isGlowing() {
        return flags.get(GLOWING);
    }

    public boolean isBackgroundHidden() {
        return flags.get(HIDE_BACKGROUND);
    }

    public void setGlowing(boolean value) {
        flags.set(GLOWING, value);
    }

    public void setHideBackground(boolean value) {
        flags.set(HIDE_BACKGROUND, value);
    }
}
