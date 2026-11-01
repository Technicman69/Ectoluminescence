package com.technicman.ectolum.util;

import com.technicman.ectolum.Ectoluminescence;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static final TagKey<Block> GLOWING_WOOL = TagKey.create(Registries.BLOCK, Ectoluminescence.id("glowing_wool"));
    public static final TagKey<Block> GLOWING_WOOL_CARPETS = TagKey.create(Registries.BLOCK, Ectoluminescence.id("glowing_wool_carpets"));
    public static final TagKey<Block> GLOWING_BEDS = TagKey.create(Registries.BLOCK, Ectoluminescence.id("glowing_beds"));
    public static final TagKey<Block> GLOWING_BANNERS = TagKey.create(Registries.BLOCK, Ectoluminescence.id("glowing_banners"));
    public static final TagKey<Block> GLOWING_WALL_BANNERS = TagKey.create(Registries.BLOCK, Ectoluminescence.id("glowing_wall_banners"));

    public static final TagKey<Item> GLOWING_WOOL_ITEMS = TagKey.create(Registries.ITEM, Ectoluminescence.id("glowing_wool"));
    public static final TagKey<Item> GLOWING_WOOL_CARPET_ITEMS = TagKey.create(Registries.ITEM, Ectoluminescence.id("glowing_wool_carpets"));
    public static final TagKey<Item> GLOWING_BED_ITEMS = TagKey.create(Registries.ITEM, Ectoluminescence.id("glowing_beds"));
    public static final TagKey<Item> GLOWING_BANNER_ITEMS = TagKey.create(Registries.ITEM, Ectoluminescence.id("glowing_banners"));
}
