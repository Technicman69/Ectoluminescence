package com.technicman.ectolum.util;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.DyeColor;

import java.util.EnumMap;

public class BlockDyeMap {
    public static final EnumMap<DyeColor, Block> WOOL = new EnumMap<>(DyeColor.class);
    public static final EnumMap<DyeColor, Block> CARPET = new EnumMap<>(DyeColor.class);
    public static final EnumMap<DyeColor, Block> BED = new EnumMap<>(DyeColor.class);
    public static final EnumMap<DyeColor, Block> BANNER = new EnumMap<>(DyeColor.class);
    public static final EnumMap<DyeColor, Block> WALL_BANNER = new EnumMap<>(DyeColor.class);

    public static final EnumMap<DyeColor, Block> GLOWING_WOOL = new EnumMap<>(DyeColor.class);
    public static final EnumMap<DyeColor, Block> GLOWING_CARPET = new EnumMap<>(DyeColor.class);
    public static final EnumMap<DyeColor, Block> GLOWING_BED = new EnumMap<>(DyeColor.class);
    public static final EnumMap<DyeColor, Block> GLOWING_BANNER = new EnumMap<>(DyeColor.class);
    public static final EnumMap<DyeColor, Block> GLOWING_WALL_BANNER = new EnumMap<>(DyeColor.class);

    static {
        WOOL.put(DyeColor.WHITE, Blocks.WHITE_WOOL);
        WOOL.put(DyeColor.ORANGE, Blocks.ORANGE_WOOL);
        WOOL.put(DyeColor.MAGENTA, Blocks.MAGENTA_WOOL);
        WOOL.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_WOOL);
        WOOL.put(DyeColor.YELLOW, Blocks.YELLOW_WOOL);
        WOOL.put(DyeColor.LIME, Blocks.LIME_WOOL);
        WOOL.put(DyeColor.PINK, Blocks.PINK_WOOL);
        WOOL.put(DyeColor.GRAY, Blocks.GRAY_WOOL);
        WOOL.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_WOOL);
        WOOL.put(DyeColor.CYAN, Blocks.CYAN_WOOL);
        WOOL.put(DyeColor.PURPLE, Blocks.PURPLE_WOOL);
        WOOL.put(DyeColor.BLUE, Blocks.BLUE_WOOL);
        WOOL.put(DyeColor.BROWN, Blocks.BROWN_WOOL);
        WOOL.put(DyeColor.GREEN, Blocks.GREEN_WOOL);
        WOOL.put(DyeColor.RED, Blocks.RED_WOOL);
        WOOL.put(DyeColor.BLACK, Blocks.BLACK_WOOL);

        CARPET.put(DyeColor.WHITE, Blocks.WHITE_CARPET);
        CARPET.put(DyeColor.ORANGE, Blocks.ORANGE_CARPET);
        CARPET.put(DyeColor.MAGENTA, Blocks.MAGENTA_CARPET);
        CARPET.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_CARPET);
        CARPET.put(DyeColor.YELLOW, Blocks.YELLOW_CARPET);
        CARPET.put(DyeColor.LIME, Blocks.LIME_CARPET);
        CARPET.put(DyeColor.PINK, Blocks.PINK_CARPET);
        CARPET.put(DyeColor.GRAY, Blocks.GRAY_CARPET);
        CARPET.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_CARPET);
        CARPET.put(DyeColor.CYAN, Blocks.CYAN_CARPET);
        CARPET.put(DyeColor.PURPLE, Blocks.PURPLE_CARPET);
        CARPET.put(DyeColor.BLUE, Blocks.BLUE_CARPET);
        CARPET.put(DyeColor.BROWN, Blocks.BROWN_CARPET);
        CARPET.put(DyeColor.GREEN, Blocks.GREEN_CARPET);
        CARPET.put(DyeColor.RED, Blocks.RED_CARPET);
        CARPET.put(DyeColor.BLACK, Blocks.BLACK_CARPET);

        BED.put(DyeColor.WHITE, Blocks.WHITE_BED);
        BED.put(DyeColor.ORANGE, Blocks.ORANGE_BED);
        BED.put(DyeColor.MAGENTA, Blocks.MAGENTA_BED);
        BED.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_BED);
        BED.put(DyeColor.YELLOW, Blocks.YELLOW_BED);
        BED.put(DyeColor.LIME, Blocks.LIME_BED);
        BED.put(DyeColor.PINK, Blocks.PINK_BED);
        BED.put(DyeColor.GRAY, Blocks.GRAY_BED);
        BED.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_BED);
        BED.put(DyeColor.CYAN, Blocks.CYAN_BED);
        BED.put(DyeColor.PURPLE, Blocks.PURPLE_BED);
        BED.put(DyeColor.BLUE, Blocks.BLUE_BED);
        BED.put(DyeColor.BROWN, Blocks.BROWN_BED);
        BED.put(DyeColor.GREEN, Blocks.GREEN_BED);
        BED.put(DyeColor.RED, Blocks.RED_BED);
        BED.put(DyeColor.BLACK, Blocks.BLACK_BED);

        BANNER.put(DyeColor.WHITE, Blocks.WHITE_BANNER);
        BANNER.put(DyeColor.ORANGE, Blocks.ORANGE_BANNER);
        BANNER.put(DyeColor.MAGENTA, Blocks.MAGENTA_BANNER);
        BANNER.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_BANNER);
        BANNER.put(DyeColor.YELLOW, Blocks.YELLOW_BANNER);
        BANNER.put(DyeColor.LIME, Blocks.LIME_BANNER);
        BANNER.put(DyeColor.PINK, Blocks.PINK_BANNER);
        BANNER.put(DyeColor.GRAY, Blocks.GRAY_BANNER);
        BANNER.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_BANNER);
        BANNER.put(DyeColor.CYAN, Blocks.CYAN_BANNER);
        BANNER.put(DyeColor.PURPLE, Blocks.PURPLE_BANNER);
        BANNER.put(DyeColor.BLUE, Blocks.BLUE_BANNER);
        BANNER.put(DyeColor.BROWN, Blocks.BROWN_BANNER);
        BANNER.put(DyeColor.GREEN, Blocks.GREEN_BANNER);
        BANNER.put(DyeColor.RED, Blocks.RED_BANNER);
        BANNER.put(DyeColor.BLACK, Blocks.BLACK_BANNER);

        WALL_BANNER.put(DyeColor.WHITE, Blocks.WHITE_WALL_BANNER);
        WALL_BANNER.put(DyeColor.ORANGE, Blocks.ORANGE_WALL_BANNER);
        WALL_BANNER.put(DyeColor.MAGENTA, Blocks.MAGENTA_WALL_BANNER);
        WALL_BANNER.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_WALL_BANNER);
        WALL_BANNER.put(DyeColor.YELLOW, Blocks.YELLOW_WALL_BANNER);
        WALL_BANNER.put(DyeColor.LIME, Blocks.LIME_WALL_BANNER);
        WALL_BANNER.put(DyeColor.PINK, Blocks.PINK_WALL_BANNER);
        WALL_BANNER.put(DyeColor.GRAY, Blocks.GRAY_WALL_BANNER);
        WALL_BANNER.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_WALL_BANNER);
        WALL_BANNER.put(DyeColor.CYAN, Blocks.CYAN_WALL_BANNER);
        WALL_BANNER.put(DyeColor.PURPLE, Blocks.PURPLE_WALL_BANNER);
        WALL_BANNER.put(DyeColor.BLUE, Blocks.BLUE_WALL_BANNER);
        WALL_BANNER.put(DyeColor.BROWN, Blocks.BROWN_WALL_BANNER);
        WALL_BANNER.put(DyeColor.GREEN, Blocks.GREEN_WALL_BANNER);
        WALL_BANNER.put(DyeColor.RED, Blocks.RED_WALL_BANNER);
        WALL_BANNER.put(DyeColor.BLACK, Blocks.BLACK_WALL_BANNER);
    }
}
