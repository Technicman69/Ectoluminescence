package com.technicman.ectolum.item;

import com.technicman.ectolum.Ectoluminescence;
import com.technicman.ectolum.component.EchoingLayer;
import com.technicman.ectolum.component.ModComponents;
import com.technicman.ectolum.util.BlockDyeMap;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimMaterials;
import net.minecraft.world.item.equipment.trim.TrimPatterns;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.technicman.ectolum.item.ModItems.CLEAR_ITEM_FRAME;
import static com.technicman.ectolum.item.ModItems.GLOW_INK_TORCH_ITEM;

public class ModItemGroups {
    private static final Component HELMET = Component.translatable("item.ectolum.gamer_helmet").withStyle(ChatFormatting.LIGHT_PURPLE);
    private static final Component CHESTPLATE = Component.translatable("item.ectolum.gamer_chestplate").withStyle(ChatFormatting.LIGHT_PURPLE);
    private static final Component LEGGINGS = Component.translatable("item.ectolum.gamer_leggings").withStyle(ChatFormatting.LIGHT_PURPLE);
    private static final Component BOOTS = Component.translatable("item.ectolum.gamer_boots").withStyle(ChatFormatting.LIGHT_PURPLE);

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

    public static final CreativeModeTab ITEM_GROUP = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Ectoluminescence.id("ectolum"), FabricCreativeModeTab.builder()
            .icon(() -> ICON)
            .title(Component.translatable("itemGroup.ectolum.ectolum"))
            .displayItems((context, entries) -> {
                entries.accept(GLOW_INK_TORCH_ITEM);
                entries.accept(CLEAR_ITEM_FRAME);

                entries.acceptAll(orderDyedItems(BlockDyeMap.GLOWING_WOOL));
                entries.acceptAll(orderDyedItems(BlockDyeMap.GLOWING_CARPET));
                entries.acceptAll(orderDyedItems(BlockDyeMap.GLOWING_BED));
                entries.acceptAll(orderDyedItems(BlockDyeMap.GLOWING_BANNER));

                // Gamer armor, less goo
                entries.accept(new ItemStack(Holder.direct(Items.NETHERITE_HELMET), 1, gamerComponents(context, HELMET)));
                entries.accept(ICON = new ItemStack(Holder.direct(Items.NETHERITE_CHESTPLATE), 1, gamerComponents(context, CHESTPLATE)));
                entries.accept(new ItemStack(Holder.direct(Items.NETHERITE_LEGGINGS), 1, gamerComponents(context, LEGGINGS)));
                entries.accept(new ItemStack(Holder.direct(Items.NETHERITE_BOOTS), 1, gamerComponents(context, BOOTS)));
            })
            .build());

    private static @NotNull DataComponentPatch gamerComponents(CreativeModeTab.ItemDisplayParameters context, Component name) {
        final Optional<Boolean> empty = Optional.empty();
        Function<ResourceKey<TrimMaterial>, Optional<Holder<TrimMaterial>>> material = (key) ->
                Optional.of(context.holders().getOrThrow(key));

        return DataComponentPatch.builder()
                .set(DataComponents.ITEM_NAME, name)
                .set(DataComponents.TRIM, new ArmorTrim(
                        context.holders().getOrThrow(TrimMaterials.REDSTONE),
                        context.holders().getOrThrow(TrimPatterns.SILENCE)
                )).set(ModComponents.ECHOING_LAYERS, List.of(
                        new EchoingLayer(Optional.of(true), empty, Optional.empty()),
                        new EchoingLayer(empty, empty, Optional.of(context.holders().getOrThrow((TrimMaterials.COPPER)))),
                        new EchoingLayer(empty, empty, Optional.of(context.holders().getOrThrow((TrimMaterials.GOLD)))),
                        new EchoingLayer(empty, empty, Optional.of(context.holders().getOrThrow((TrimMaterials.EMERALD)))),
                        new EchoingLayer(empty, empty, Optional.of(context.holders().getOrThrow((TrimMaterials.DIAMOND)))),
                        new EchoingLayer(empty, empty, Optional.of(context.holders().getOrThrow((TrimMaterials.LAPIS)))),
                        new EchoingLayer(empty, empty, Optional.of(context.holders().getOrThrow((TrimMaterials.AMETHYST))))
                ))
                .build();
    }

    public static void onInitialize() {
    }
}
