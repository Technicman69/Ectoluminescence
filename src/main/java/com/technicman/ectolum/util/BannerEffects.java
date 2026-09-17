package com.technicman.ectolum.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.BitSet;
import java.util.function.Consumer;

public class BannerEffects implements TooltipAppender {
    public static final Codec<BannerEffects> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.BOOL.optionalFieldOf("glowing", false).forGetter(BannerEffects::isGlowing),
            Codec.BOOL.optionalFieldOf("hide_background", false).forGetter(BannerEffects::isBackgroundHidden)
    ).apply(builder, BannerEffects::new));

    public static final PacketCodec<RegistryByteBuf, BannerEffects> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.BYTE, BannerEffects::getFlags,
            BannerEffects::new
    );

    public static BannerEffects empty() {
        return new BannerEffects((byte)0);
    }

    public static final BannerEffects EMPTY = empty();

    private static final int GLOWING = 0;
    private static final int HIDE_BACKGROUND = 1;

    private final BitSet flags;

    public BannerEffects(boolean glowing, boolean hideBackground) {
        flags = new BitSet(2);
        setGlowing(glowing);
        setHideBackground(hideBackground);
    }

    public BannerEffects(byte flags) {
        this.flags = BitSet.valueOf(new byte[] {flags});
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

    public byte getFlags() {
        byte[] bytes = flags.toByteArray();
        return bytes.length > 0 ? bytes[0] : (byte)0;
    }

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> tooltip, TooltipType type) {
        if (isGlowing()) {
            tooltip.accept(Text.translatable("block.minecraft.banner.ectolum.glowing").formatted(Formatting.WHITE));
        }
        if (isBackgroundHidden()) {
            tooltip.accept(Text.translatable("block.minecraft.banner.ectolum.hide_background").formatted(Formatting.WHITE));
        }
    }
}
