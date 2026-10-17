package com.technicman.ectolum.item;

import com.technicman.ectolum.Ectoluminescence;
import com.technicman.ectolum.block.ModBlocks;
import com.technicman.ectolum.entity.ModEntities;
import com.technicman.ectolum.util.BlockDyeMap;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.function.Function;

public class ModItems {
    public static final Item GLOW_INK_TORCH_ITEM = register("glow_ink_torch",
            settings -> new VerticallyAttachableBlockItem(ModBlocks.GLOW_INK_TORCH, ModBlocks.GLOW_INK_WALL_TORCH, Direction.DOWN, settings));
    public static final Item CLEAR_ITEM_FRAME = register("clear_item_frame",
            settings -> new ClearItemFrameItem(ModEntities.CLEAR_ITEM_FRAME, settings));

    public static final Item GLOWING_WHITE_WOOL = Items.register(ModBlocks.GLOWING_WHITE_WOOL);

    public static final Item GLOWING_WHITE_CARPET = registerCarpet(ModBlocks.GLOWING_WHITE_CARPET, DyeColor.WHITE);

    public static final Item GLOWING_WHITE_BED = registerBed(ModBlocks.GLOWING_WHITE_BED);

    public static final Item GLOWING_WHITE_BANNER = registerBanner(ModBlocks.GLOWING_WHITE_BANNER, ModBlocks.GLOWING_WHITE_WALL_BANNER);


    private static Item registerCarpet(Block block, DyeColor color) {
        return Items.register(block, (settings) -> settings.component(DataComponentTypes.EQUIPPABLE, EquippableComponent.ofCarpet(color)));
    }
    private static Item registerBed(Block block) {
        return Items.register(block, BedItem::new, (new Item.Settings()).maxCount(1));
    }
    private static Item registerBanner(Block banner, Block wallBanner) {
        return Items.register(banner,
                (block, settings) -> new BannerItem(block, wallBanner, settings),
                (new Item.Settings()).maxCount(16)
                        .component(DataComponentTypes.BANNER_PATTERNS, BannerPatternsComponent.DEFAULT));

    }

    private static Item register(String id, Function<Item.Settings, Item> function) {
        Identifier identifier = Ectoluminescence.id(id);
        Item item = function.apply(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, identifier)));
        return Registry.register(Registries.ITEM, identifier, item);
    }

    public static void onInitialize() {
        //Ectoluminescence.LOGGER.info("Registering items for " + Ectoluminescence.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> {
            content.addAfter(Items.SOUL_TORCH, GLOW_INK_TORCH_ITEM);
            content.addAfter(Items.GLOW_ITEM_FRAME, CLEAR_ITEM_FRAME);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COLORED_BLOCKS).register(content -> {
            content.addAll(BlockDyeMap.GLOWING_WOOL.values().stream().map(ItemStack::new).toList());
            content.addAll(BlockDyeMap.GLOWING_BED.values().stream().map(ItemStack::new).toList());
        });
    }
}
