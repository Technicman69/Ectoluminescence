package com.technicman.ectolum.util;

import com.technicman.ectolum.Ectoluminescence;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModTags {
    public static final TagKey<Block> GLOWING_WOOL = TagKey.of(RegistryKeys.BLOCK, Ectoluminescence.id("glowing_wool"));
    public static final TagKey<Block> GLOWING_WOOL_CARPETS = TagKey.of(RegistryKeys.BLOCK, Ectoluminescence.id("glowing_wool_carpets"));
    public static final TagKey<Block> GLOWING_BEDS = TagKey.of(RegistryKeys.BLOCK, Ectoluminescence.id("glowing_beds"));
    public static final TagKey<Block> GLOWING_BANNERS = TagKey.of(RegistryKeys.BLOCK, Ectoluminescence.id("glowing_banners"));
    public static final TagKey<Block> GLOWING_WALL_BANNERS = TagKey.of(RegistryKeys.BLOCK, Ectoluminescence.id("glowing_wall_banners"));

    public static final TagKey<Item> GLOWING_WOOL_ITEMS = TagKey.of(RegistryKeys.ITEM, Ectoluminescence.id("glowing_wool"));
    public static final TagKey<Item> GLOWING_WOOL_CARPET_ITEMS = TagKey.of(RegistryKeys.ITEM, Ectoluminescence.id("glowing_wool_carpets"));
    public static final TagKey<Item> GLOWING_BED_ITEMS = TagKey.of(RegistryKeys.ITEM, Ectoluminescence.id("glowing_beds"));
    public static final TagKey<Item> GLOWING_BANNER_ITEMS = TagKey.of(RegistryKeys.ITEM, Ectoluminescence.id("glowing_banners"));
}
