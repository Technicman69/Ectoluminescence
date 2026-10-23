package com.technicman.ectolum.item;

import com.technicman.ectolum.Ectoluminescence;
import com.technicman.ectolum.component.EchoingLayer;
import com.technicman.ectolum.component.ModComponents;
import com.technicman.ectolum.util.BlockDyeMap;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.Block;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.equipment.trim.ArmorTrim;
import net.minecraft.item.equipment.trim.ArmorTrimMaterial;
import net.minecraft.item.equipment.trim.ArmorTrimMaterials;
import net.minecraft.item.equipment.trim.ArmorTrimPatterns;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.technicman.ectolum.item.ModItems.CLEAR_ITEM_FRAME;
import static com.technicman.ectolum.item.ModItems.GLOW_INK_TORCH_ITEM;

public class ModItemGroups {
    private static final Text HELMET = Text.translatable("item.ectolum.gamer_helmet").formatted(Formatting.LIGHT_PURPLE);
    private static final Text CHESTPLATE = Text.translatable("item.ectolum.gamer_chestplate").formatted(Formatting.LIGHT_PURPLE);
    private static final Text LEGGINGS = Text.translatable("item.ectolum.gamer_leggings").formatted(Formatting.LIGHT_PURPLE);
    private static final Text BOOTS = Text.translatable("item.ectolum.gamer_boots").formatted(Formatting.LIGHT_PURPLE);

    private static ItemStack ICON;

    private static List<ItemStack> orderDyedItems(Map<DyeColor, Block> map) {
        return List.of(
                new ItemStack(map.get(DyeColor.WHITE).asItem()),
                new ItemStack(map.get(DyeColor.LIGHT_GRAY).asItem()),
                new ItemStack(map.get(DyeColor.GRAY).asItem()),
                new ItemStack(map.get(DyeColor.BLACK).asItem()),
                new ItemStack(map.get(DyeColor.BROWN).asItem()),
                new ItemStack(map.get(DyeColor.RED).asItem()),
                new ItemStack(map.get(DyeColor.ORANGE).asItem()),
                new ItemStack(map.get(DyeColor.YELLOW).asItem()),
                new ItemStack(map.get(DyeColor.LIME).asItem()),
                new ItemStack(map.get(DyeColor.GREEN).asItem()),
                new ItemStack(map.get(DyeColor.CYAN).asItem()),
                new ItemStack(map.get(DyeColor.LIGHT_BLUE).asItem()),
                new ItemStack(map.get(DyeColor.BLUE).asItem()),
                new ItemStack(map.get(DyeColor.PURPLE).asItem()),
                new ItemStack(map.get(DyeColor.MAGENTA).asItem()),
                new ItemStack(map.get(DyeColor.PINK).asItem())
        );
    }

    public static final ItemGroup ITEM_GROUP = Registry.register(Registries.ITEM_GROUP, Ectoluminescence.id("ectolum"), FabricItemGroup.builder()
            .icon(() -> ICON)
            .displayName(Text.translatable("itemGroup.ectolum.ectolum"))
            .entries((context, entries) -> {
                entries.add(GLOW_INK_TORCH_ITEM);
                entries.add(CLEAR_ITEM_FRAME);

                entries.addAll(orderDyedItems(BlockDyeMap.GLOWING_WOOL));
                entries.addAll(orderDyedItems(BlockDyeMap.GLOWING_CARPET));
                entries.addAll(orderDyedItems(BlockDyeMap.GLOWING_BED));
                entries.addAll(orderDyedItems(BlockDyeMap.GLOWING_BANNER));

                // Gamer armor, less goo
                entries.add(new ItemStack(RegistryEntry.of(Items.NETHERITE_HELMET), 1, gamerComponents(context, HELMET)));
                entries.add(ICON = new ItemStack(RegistryEntry.of(Items.NETHERITE_CHESTPLATE), 1, gamerComponents(context, CHESTPLATE)));
                entries.add(new ItemStack(RegistryEntry.of(Items.NETHERITE_LEGGINGS), 1, gamerComponents(context, LEGGINGS)));
                entries.add(new ItemStack(RegistryEntry.of(Items.NETHERITE_BOOTS), 1, gamerComponents(context, BOOTS)));
            })
            .build());

    private static @NotNull ComponentChanges gamerComponents(ItemGroup.DisplayContext context, Text name) {
        final Optional<Boolean> empty = Optional.empty();
        Function<RegistryKey<ArmorTrimMaterial>, Optional<RegistryEntry<ArmorTrimMaterial>>> material = (key) ->
                Optional.of(context.lookup().getEntryOrThrow(key));

        return ComponentChanges.builder()
                .add(DataComponentTypes.ITEM_NAME, name)
                .add(DataComponentTypes.TRIM, new ArmorTrim(
                        context.lookup().getEntryOrThrow(ArmorTrimMaterials.REDSTONE),
                        context.lookup().getEntryOrThrow(ArmorTrimPatterns.SILENCE)
                )).add(ModComponents.ECHOING_LAYERS, List.of(
                        new EchoingLayer(Optional.of(true), empty, Optional.empty()),
                        new EchoingLayer(empty, empty, Optional.of(context.lookup().getEntryOrThrow((ArmorTrimMaterials.COPPER)))),
                        new EchoingLayer(empty, empty, Optional.of(context.lookup().getEntryOrThrow((ArmorTrimMaterials.GOLD)))),
                        new EchoingLayer(empty, empty, Optional.of(context.lookup().getEntryOrThrow((ArmorTrimMaterials.EMERALD)))),
                        new EchoingLayer(empty, empty, Optional.of(context.lookup().getEntryOrThrow((ArmorTrimMaterials.DIAMOND)))),
                        new EchoingLayer(empty, empty, Optional.of(context.lookup().getEntryOrThrow((ArmorTrimMaterials.LAPIS)))),
                        new EchoingLayer(empty, empty, Optional.of(context.lookup().getEntryOrThrow((ArmorTrimMaterials.AMETHYST))))
                ))
                .build();
    }

    public static void onInitialize() {
    }
}
