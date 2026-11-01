package com.technicman.ectolum.loot;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.technicman.ectolum.component.ModComponents;
import com.technicman.ectolum.util.BlockDyeMap;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.advancements.criterion.DataComponentMatchers;
import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds.Ints;
import net.minecraft.core.Holder;
import net.minecraft.core.component.predicates.EnchantmentsPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static net.minecraft.core.component.predicates.DataComponentPredicates.ENCHANTMENTS;

public class ModLootTableModifiers  {
    private static final Set<ResourceKey<LootTable>> BANNERS_LOOT_TABLE_IDS = BuiltInRegistries.BLOCK.stream()
            .filter((block -> block instanceof AbstractBannerBlock))
            .map(BlockBehaviour::getLootTable)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toSet());

    public static void onInitialize() {
        // Modify banner loot tables, to drop with effects
        ResourceKey<LootTable> potLoot = Blocks.DECORATED_POT.getLootTable().orElse(null);
        BiMap<ResourceKey<LootTable>, DyeColor> sheepDyeColorFromDrops =
                HashBiMap.create(BuiltInLootTables.SHEEP_BY_DYE).inverse();
        BiMap<ResourceKey<LootTable>, DyeColor> sheepDyeColorFromShearing =
                HashBiMap.create(BuiltInLootTables.SHEAR_SHEEP_BY_DYE).inverse();

        LootTableEvents.MODIFY.register((registryKey, tableBuilder, source, registries) -> {
            // Let's only modify built-in loot tables and leave data pack loot tables untouched by checking the source.
            var enchantmentRegistry = registries.lookup(Registries.ENCHANTMENT);
            if (enchantmentRegistry.isEmpty()) return;
            var optionalEnchantment = enchantmentRegistry.get().get(Enchantments.SILK_TOUCH);
            if (optionalEnchantment.isEmpty()) return;
            Holder<Enchantment> silkTouch = optionalEnchantment.get();

            if (source.isBuiltin()) {
                if (registryKey.equals(potLoot)) {
                    // Decorated pots should keep sherd glow overrides
                    LootItemFunction function = CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
                            .include(ModComponents.SHERD_GLOW_OVERRIDES)
                            .when(toolEnchantedWith(silkTouch))
                            .build();
                    tableBuilder.apply(function);

                } else if (BANNERS_LOOT_TABLE_IDS.contains(registryKey)) {
                    // Banners should keep sherd their effects
                    LootItemFunction function = CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
                            .include(ModComponents.BANNER_EFFECTS)
                            .when(toolEnchantedWith(silkTouch))
                            .build();
                    tableBuilder.apply(function);

                } else if (sheepDyeColorFromDrops.containsKey(registryKey)) {
                    // Dropped sheep wool should keep the glow
                    DyeColor color = sheepDyeColorFromDrops.get(registryKey);

                    tableBuilder.modifyPools(poolBuilder ->
                            poolBuilder.when(sheepGlowing(false)));
                    tableBuilder.withPool(LootPool.lootPool()
                            .add(LootItem.lootTableItem(BlockDyeMap.GLOWING_WOOL.get(color)))
                            .when(sheepGlowing(true)));

                } else if (sheepDyeColorFromShearing.containsKey(registryKey)) {
                    // Sheared sheep wool should keep the glow
                    DyeColor color = sheepDyeColorFromShearing.get(registryKey);

                    tableBuilder.modifyPools(poolBuilder ->
                            poolBuilder.when(sheepGlowing(false)));
                    tableBuilder.withPool(LootPool.lootPool()
                            .add(LootItem.lootTableItem(BlockDyeMap.GLOWING_WOOL.get(color)))
                            .setRolls(UniformGenerator.between(1, 3))
                            .when(sheepGlowing(true)));
                }
            }
        });
    }

    public static LootItemCondition sheepGlowing(boolean glowing) {
        return LootItemEntityPropertyCondition.hasProperties(
                        LootContext.EntityTarget.THIS,
                        EntityPredicate.Builder.entity()
                                .subPredicate(SheepPredicate.glowing(glowing))
                                .build()
                )
                .build();
    }

    public static LootItemCondition.Builder toolEnchantedWith(Holder<Enchantment> enchantment) {
        return MatchTool.toolMatches(
                ItemPredicate.Builder.item()
                        .withComponents(DataComponentMatchers.Builder.components()
                                .partial(ENCHANTMENTS, EnchantmentsPredicate.Enchantments.enchantments(
                                        List.of(new EnchantmentPredicate(
                                                enchantment,
                                                Ints.atLeast(1)
                                        ))
                                ))
                                .build()
                        )
        );
    }
}
