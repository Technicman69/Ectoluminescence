package com.technicman.ectolum.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.BitSet;
import java.util.function.Consumer;

public class BannerEffects implements TooltipProvider {
    public static final Codec<BannerEffects> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.BOOL.optionalFieldOf("glowing", false).forGetter(BannerEffects::isGlowing),
            Codec.BOOL.optionalFieldOf("hide_background", false).forGetter(BannerEffects::isBackgroundHidden)
    ).apply(builder, BannerEffects::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, BannerEffects> PACKET_CODEC = StreamCodec.composite(
            ByteBufCodecs.BYTE, BannerEffects::getFlags,
            BannerEffects::new
    );

    public static final BannerEffects EMPTY = new BannerEffects((byte)0);

    private static final int GLOWING = 0;
    private static final int HIDE_BACKGROUND = 1;

    private final BitSet flags;

    public BannerEffects(BitSet flags) {
        this.flags = flags;
    }

    public BannerEffects(boolean glowing, boolean hideBackground) {
        flags = new BitSet(2);
        flags.set(GLOWING, glowing);
        flags.set(HIDE_BACKGROUND, hideBackground);
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

    public BannerEffects withGlowing(boolean value) {
        BitSet newFlags = (BitSet) flags.clone();
        newFlags.set(GLOWING, value);
        return new BannerEffects(newFlags);
    }

    public BannerEffects withHideBackground(boolean value) {
        BitSet newFlags = (BitSet) flags.clone();
        newFlags.set(HIDE_BACKGROUND, value);
        return new BannerEffects(newFlags);
    }

    public byte getFlags() {
        byte[] bytes = flags.toByteArray();
        return bytes.length > 0 ? bytes[0] : (byte)0;
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltip, TooltipFlag type, DataComponentGetter components) {
        if (isGlowing()) {
            tooltip.accept(Component.translatable("block.minecraft.banner.ectolum.glowing").withStyle(ChatFormatting.GRAY));
        }
        if (isBackgroundHidden()) {
            tooltip.accept(Component.translatable("block.minecraft.banner.ectolum.hide_background").withStyle(ChatFormatting.GRAY));
        }
    }
}
