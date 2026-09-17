package com.technicman.ectolum.loot;

import com.technicman.ectolum.component.ModComponents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.function.CopyComponentsLootFunction;
import net.minecraft.loot.function.CopyNbtLootFunction;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.provider.nbt.ContextLootNbtProvider;
import net.minecraft.predicate.NumberRange.IntRange;
import net.minecraft.predicate.item.EnchantmentPredicate;
import net.minecraft.predicate.item.EnchantmentsPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.predicate.item.ItemSubPredicateTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ModLootTableModifiers  {
    private static final Set<RegistryKey<LootTable>> BANNERS_LOOT_TABLE_IDS = Registries.BLOCK.stream()
            .filter((block -> block instanceof AbstractBannerBlock))
            .map(AbstractBlock::getLootTableKey)
            .collect(Collectors.toSet());

    public static void onInitialize() {
        // Modify banner loot tables, to drop with effects

        LootTableEvents.MODIFY.register((registryKey, tableBuilder, source, registries) -> {
            // Let's only modify built-in loot tables and leave data pack loot tables untouched by checking the source.
            var optionalEnchantment = registries.createRegistryLookup().getOptionalEntry(RegistryKeys.ENCHANTMENT, Enchantments.SILK_TOUCH);
            if (optionalEnchantment.isEmpty()) return;
            RegistryEntry<Enchantment> silkTouch = optionalEnchantment.get();

            if (source.isBuiltin())
                if (registryKey.equals(Blocks.DECORATED_POT.getLootTableKey())) {
                    LootFunction function = CopyComponentsLootFunction.builder(CopyComponentsLootFunction.Source.BLOCK_ENTITY)
                            .include(ModComponents.SHERD_GLOW_OVERRIDES)
                            .conditionally(
                                    MatchToolLootCondition.builder(
                                            ItemPredicate.Builder.create()
                                                    .subPredicate(ItemSubPredicateTypes.ENCHANTMENTS, EnchantmentsPredicate.Enchantments.enchantments(
                                                            List.of(new EnchantmentPredicate(
                                                                    silkTouch,
                                                                    IntRange.atLeast(1)
                                                            ))
                                                    ))
                                    )
                            ).build();
                    tableBuilder.apply(function);
                }
                else if (BANNERS_LOOT_TABLE_IDS.contains(registryKey)) {
                    System.out.println("Changed loot table: " + registryKey.getValue());
                    LootFunction function = CopyComponentsLootFunction.builder(CopyComponentsLootFunction.Source.BLOCK_ENTITY)
                            .include(ModComponents.BANNER_EFFECTS)
//                            .withOperation("\"ectolum.glowing\"", "BlockEntityTag.\"ectolum.glowing\"")
//                            .withOperation("\"ectolum.hide_background\"", "BlockEntityTag.\"ectolum.hide_background\"")
                            .conditionally(
                                    MatchToolLootCondition.builder(
                                            ItemPredicate.Builder.create()
                                                    .subPredicate(ItemSubPredicateTypes.ENCHANTMENTS, EnchantmentsPredicate.Enchantments.enchantments(
                                                            List.of(new EnchantmentPredicate(
                                                                    silkTouch,
                                                                    IntRange.atLeast(1)
                                                            ))
                                                    ))
                                    )
                            ).build();
                    tableBuilder.apply(function);
                }
        });
    }
}
