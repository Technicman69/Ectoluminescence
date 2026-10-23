package com.technicman.ectolum.item;

import com.technicman.ectolum.Ectoluminescence;
import com.technicman.ectolum.block.ModBlocks;
import com.technicman.ectolum.entity.ModEntities;
import net.minecraft.block.Block;
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
    public static final Item GLOWING_LIGHT_GRAY_WOOL = Items.register(ModBlocks.GLOWING_LIGHT_GRAY_WOOL);
    public static final Item GLOWING_GRAY_WOOL = Items.register(ModBlocks.GLOWING_GRAY_WOOL);
    public static final Item GLOWING_BLACK_WOOL = Items.register(ModBlocks.GLOWING_BLACK_WOOL);
    public static final Item GLOWING_BROWN_WOOL = Items.register(ModBlocks.GLOWING_BROWN_WOOL);
    public static final Item GLOWING_RED_WOOL = Items.register(ModBlocks.GLOWING_RED_WOOL);
    public static final Item GLOWING_ORANGE_WOOL = Items.register(ModBlocks.GLOWING_ORANGE_WOOL);
    public static final Item GLOWING_YELLOW_WOOL = Items.register(ModBlocks.GLOWING_YELLOW_WOOL);
    public static final Item GLOWING_LIME_WOOL = Items.register(ModBlocks.GLOWING_LIME_WOOL);
    public static final Item GLOWING_GREEN_WOOL = Items.register(ModBlocks.GLOWING_GREEN_WOOL);
    public static final Item GLOWING_CYAN_WOOL = Items.register(ModBlocks.GLOWING_CYAN_WOOL);
    public static final Item GLOWING_LIGHT_BLUE_WOOL = Items.register(ModBlocks.GLOWING_LIGHT_BLUE_WOOL);
    public static final Item GLOWING_BLUE_WOOL = Items.register(ModBlocks.GLOWING_BLUE_WOOL);
    public static final Item GLOWING_PURPLE_WOOL = Items.register(ModBlocks.GLOWING_PURPLE_WOOL);
    public static final Item GLOWING_MAGENTA_WOOL = Items.register(ModBlocks.GLOWING_MAGENTA_WOOL);
    public static final Item GLOWING_PINK_WOOL = Items.register(ModBlocks.GLOWING_PINK_WOOL);

    public static final Item GLOWING_WHITE_CARPET = registerCarpet(ModBlocks.GLOWING_WHITE_CARPET, DyeColor.WHITE);
    public static final Item GLOWING_LIGHT_GRAY_CARPET = registerCarpet(ModBlocks.GLOWING_LIGHT_GRAY_CARPET, DyeColor.LIGHT_GRAY);
    public static final Item GLOWING_GRAY_CARPET = registerCarpet(ModBlocks.GLOWING_GRAY_CARPET, DyeColor.GRAY);
    public static final Item GLOWING_BLACK_CARPET = registerCarpet(ModBlocks.GLOWING_BLACK_CARPET, DyeColor.BLACK);
    public static final Item GLOWING_BROWN_CARPET = registerCarpet(ModBlocks.GLOWING_BROWN_CARPET, DyeColor.BROWN);
    public static final Item GLOWING_RED_CARPET = registerCarpet(ModBlocks.GLOWING_RED_CARPET, DyeColor.RED);
    public static final Item GLOWING_ORANGE_CARPET = registerCarpet(ModBlocks.GLOWING_ORANGE_CARPET, DyeColor.ORANGE);
    public static final Item GLOWING_YELLOW_CARPET = registerCarpet(ModBlocks.GLOWING_YELLOW_CARPET, DyeColor.YELLOW);
    public static final Item GLOWING_LIME_CARPET = registerCarpet(ModBlocks.GLOWING_LIME_CARPET, DyeColor.LIME);
    public static final Item GLOWING_GREEN_CARPET = registerCarpet(ModBlocks.GLOWING_GREEN_CARPET, DyeColor.GREEN);
    public static final Item GLOWING_CYAN_CARPET = registerCarpet(ModBlocks.GLOWING_CYAN_CARPET, DyeColor.CYAN);
    public static final Item GLOWING_LIGHT_BLUE_CARPET = registerCarpet(ModBlocks.GLOWING_LIGHT_BLUE_CARPET, DyeColor.LIGHT_BLUE);
    public static final Item GLOWING_BLUE_CARPET = registerCarpet(ModBlocks.GLOWING_BLUE_CARPET, DyeColor.BLUE);
    public static final Item GLOWING_PURPLE_CARPET = registerCarpet(ModBlocks.GLOWING_PURPLE_CARPET, DyeColor.PURPLE);
    public static final Item GLOWING_MAGENTA_CARPET = registerCarpet(ModBlocks.GLOWING_MAGENTA_CARPET, DyeColor.MAGENTA);
    public static final Item GLOWING_PINK_CARPET = registerCarpet(ModBlocks.GLOWING_PINK_CARPET, DyeColor.PINK);

    public static final Item GLOWING_WHITE_BED = registerBed(ModBlocks.GLOWING_WHITE_BED);
    public static final Item GLOWING_LIGHT_GRAY_BED = registerBed(ModBlocks.GLOWING_LIGHT_GRAY_BED);
    public static final Item GLOWING_GRAY_BED = registerBed(ModBlocks.GLOWING_GRAY_BED);
    public static final Item GLOWING_BLACK_BED = registerBed(ModBlocks.GLOWING_BLACK_BED);
    public static final Item GLOWING_BROWN_BED = registerBed(ModBlocks.GLOWING_BROWN_BED);
    public static final Item GLOWING_RED_BED = registerBed(ModBlocks.GLOWING_RED_BED);
    public static final Item GLOWING_ORANGE_BED = registerBed(ModBlocks.GLOWING_ORANGE_BED);
    public static final Item GLOWING_YELLOW_BED = registerBed(ModBlocks.GLOWING_YELLOW_BED);
    public static final Item GLOWING_LIME_BED = registerBed(ModBlocks.GLOWING_LIME_BED);
    public static final Item GLOWING_GREEN_BED = registerBed(ModBlocks.GLOWING_GREEN_BED);
    public static final Item GLOWING_CYAN_BED = registerBed(ModBlocks.GLOWING_CYAN_BED);
    public static final Item GLOWING_LIGHT_BLUE_BED = registerBed(ModBlocks.GLOWING_LIGHT_BLUE_BED);
    public static final Item GLOWING_BLUE_BED = registerBed(ModBlocks.GLOWING_BLUE_BED);
    public static final Item GLOWING_PURPLE_BED = registerBed(ModBlocks.GLOWING_PURPLE_BED);
    public static final Item GLOWING_MAGENTA_BED = registerBed(ModBlocks.GLOWING_MAGENTA_BED);
    public static final Item GLOWING_PINK_BED = registerBed(ModBlocks.GLOWING_PINK_BED);

    public static final Item GLOWING_WHITE_BANNER = registerBanner(ModBlocks.GLOWING_WHITE_BANNER, ModBlocks.GLOWING_WHITE_WALL_BANNER);
    public static final Item GLOWING_LIGHT_GRAY_BANNER = registerBanner(ModBlocks.GLOWING_LIGHT_GRAY_BANNER, ModBlocks.GLOWING_LIGHT_GRAY_WALL_BANNER);
    public static final Item GLOWING_GRAY_BANNER = registerBanner(ModBlocks.GLOWING_GRAY_BANNER, ModBlocks.GLOWING_GRAY_WALL_BANNER);
    public static final Item GLOWING_BLACK_BANNER = registerBanner(ModBlocks.GLOWING_BLACK_BANNER, ModBlocks.GLOWING_BLACK_WALL_BANNER);
    public static final Item GLOWING_BROWN_BANNER = registerBanner(ModBlocks.GLOWING_BROWN_BANNER, ModBlocks.GLOWING_BROWN_WALL_BANNER);
    public static final Item GLOWING_RED_BANNER = registerBanner(ModBlocks.GLOWING_RED_BANNER, ModBlocks.GLOWING_RED_WALL_BANNER);
    public static final Item GLOWING_ORANGE_BANNER = registerBanner(ModBlocks.GLOWING_ORANGE_BANNER, ModBlocks.GLOWING_ORANGE_WALL_BANNER);
    public static final Item GLOWING_YELLOW_BANNER = registerBanner(ModBlocks.GLOWING_YELLOW_BANNER, ModBlocks.GLOWING_YELLOW_WALL_BANNER);
    public static final Item GLOWING_LIME_BANNER = registerBanner(ModBlocks.GLOWING_LIME_BANNER, ModBlocks.GLOWING_LIME_WALL_BANNER);
    public static final Item GLOWING_GREEN_BANNER = registerBanner(ModBlocks.GLOWING_GREEN_BANNER, ModBlocks.GLOWING_GREEN_WALL_BANNER);
    public static final Item GLOWING_CYAN_BANNER = registerBanner(ModBlocks.GLOWING_CYAN_BANNER, ModBlocks.GLOWING_CYAN_WALL_BANNER);
    public static final Item GLOWING_LIGHT_BLUE_BANNER = registerBanner(ModBlocks.GLOWING_LIGHT_BLUE_BANNER, ModBlocks.GLOWING_LIGHT_BLUE_WALL_BANNER);
    public static final Item GLOWING_BLUE_BANNER = registerBanner(ModBlocks.GLOWING_BLUE_BANNER, ModBlocks.GLOWING_BLUE_WALL_BANNER);
    public static final Item GLOWING_PURPLE_BANNER = registerBanner(ModBlocks.GLOWING_PURPLE_BANNER, ModBlocks.GLOWING_PURPLE_WALL_BANNER);
    public static final Item GLOWING_MAGENTA_BANNER = registerBanner(ModBlocks.GLOWING_MAGENTA_BANNER, ModBlocks.GLOWING_MAGENTA_WALL_BANNER);
    public static final Item GLOWING_PINK_BANNER = registerBanner(ModBlocks.GLOWING_PINK_BANNER, ModBlocks.GLOWING_PINK_WALL_BANNER);


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
    }
}
