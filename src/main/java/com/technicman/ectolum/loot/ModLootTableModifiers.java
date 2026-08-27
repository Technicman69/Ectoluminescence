package com.technicman.ectolum.loot;

import com.google.gson.JsonSerializationContext;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.function.CopyNbtLootFunction;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.SetNbtLootFunction;
import net.minecraft.loot.provider.nbt.ContextLootNbtProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtInt;
import net.minecraft.predicate.NumberRange.IntRange;
import net.minecraft.predicate.item.EnchantmentPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ModLootTableModifiers  {
    private static final Set<Identifier> BANNERS_LOOT_TABLE_IDS = Registries.BLOCK.stream()
            .filter((block -> block instanceof AbstractBannerBlock))
            .map(AbstractBlock::getLootTableId)
            .collect(Collectors.toSet());
    public static void onInitialize() {
        // Modify banner loot tables, to drop with effects
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            // Let's only modify built-in loot tables and leave data pack loot tables untouched by checking the source.
            if (source.isBuiltin() && BANNERS_LOOT_TABLE_IDS.contains(id)) {
                //System.out.println("Changed loot table: " + id);
                LootFunction function = CopyNbtLootFunction.builder(ContextLootNbtProvider.BLOCK_ENTITY)
                        .withOperation("\"ectolum.glowing\"", "BlockEntityTag.\"ectolum.glowing\"")
                        .withOperation("\"ectolum.hide_background\"", "BlockEntityTag.\"ectolum.hide_background\"")
                        .conditionally(
                                MatchToolLootCondition.builder(
                                        ItemPredicate.Builder.create()
                                                .enchantment(new EnchantmentPredicate(
                                                        Enchantments.SILK_TOUCH,
                                                        IntRange.atLeast(1)
                                                ))
                                )
                        ).build();
                tableBuilder.apply(function);
            }
        });
    }
}
