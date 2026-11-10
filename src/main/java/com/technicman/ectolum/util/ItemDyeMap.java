package com.technicman.ectolum.util;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.EnumMap;

public class ItemDyeMap {
    public static final EnumMap<DyeColor, Item> DYE = new EnumMap<>(DyeColor.class);

    static {
        DYE.put(DyeColor.WHITE, Items.WHITE_DYE);
        DYE.put(DyeColor.ORANGE, Items.ORANGE_DYE);
        DYE.put(DyeColor.MAGENTA, Items.MAGENTA_DYE);
        DYE.put(DyeColor.LIGHT_BLUE, Items.LIGHT_BLUE_DYE);
        DYE.put(DyeColor.YELLOW, Items.YELLOW_DYE);
        DYE.put(DyeColor.LIME, Items.LIME_DYE);
        DYE.put(DyeColor.PINK, Items.PINK_DYE);
        DYE.put(DyeColor.GRAY, Items.GRAY_DYE);
        DYE.put(DyeColor.LIGHT_GRAY, Items.LIGHT_GRAY_DYE);
        DYE.put(DyeColor.CYAN, Items.CYAN_DYE);
        DYE.put(DyeColor.PURPLE, Items.PURPLE_DYE);
        DYE.put(DyeColor.BLUE, Items.BLUE_DYE);
        DYE.put(DyeColor.BROWN, Items.BROWN_DYE);
        DYE.put(DyeColor.GREEN, Items.GREEN_DYE);
        DYE.put(DyeColor.RED, Items.RED_DYE);
        DYE.put(DyeColor.BLACK, Items.BLACK_DYE);
    }
}
