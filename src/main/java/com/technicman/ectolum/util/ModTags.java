package com.technicman.ectolum.util;

import com.technicman.ectolum.Ectoluminescence;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModTags {
    public static final TagKey<Block> GLOWING_WOOL = TagKey.of(RegistryKeys.BLOCK, Ectoluminescence.id("glowing_wool"));
    public static final TagKey<Block> GLOWING_WOOL_CARPETS = TagKey.of(RegistryKeys.BLOCK, Ectoluminescence.id("glowing_wool_carpets"));
}
