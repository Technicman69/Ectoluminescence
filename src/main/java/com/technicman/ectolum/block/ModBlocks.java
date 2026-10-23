package com.technicman.ectolum.block;

import com.google.common.collect.Sets;
import com.technicman.ectolum.Ectoluminescence;
import com.technicman.ectolum.util.BlockDyeMap;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.BedPart;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;

import java.util.HashSet;
import java.util.function.Function;

public class ModBlocks {
    public static final Block GLOW_INK_TORCH = register(
            "glow_ink_torch",
            GlowInkTorchBlock::new,
            AbstractBlock.Settings.create()
                    .noCollision()
                    .breakInstantly()
                    .luminance((state) -> state.get(Properties.WATERLOGGED) ? 12 : 8)
                    .sounds(BlockSoundGroup.WOOD)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block GLOW_INK_WALL_TORCH = register(
            "glow_ink_wall_torch",
            GlowInkWallTorchBlock::new,
            copyLootTable(GLOW_INK_TORCH, true)
                    .noCollision()
                    .breakInstantly()
                    .luminance((state) -> state.get(Properties.WATERLOGGED) ? 12 : 8)
                    .sounds(BlockSoundGroup.WOOD)
                    .pistonBehavior(PistonBehavior.DESTROY));

    public static final Block GLOWING_WHITE_WOOL = registerGlowingWool("glowing_white_wool", DyeColor.WHITE);
    public static final Block GLOWING_LIGHT_GRAY_WOOL = registerGlowingWool("glowing_light_gray_wool", DyeColor.LIGHT_GRAY);
    public static final Block GLOWING_GRAY_WOOL = registerGlowingWool("glowing_gray_wool", DyeColor.GRAY);
    public static final Block GLOWING_BLACK_WOOL = registerGlowingWool("glowing_black_wool", DyeColor.BLACK);
    public static final Block GLOWING_BROWN_WOOL = registerGlowingWool("glowing_brown_wool", DyeColor.BROWN);
    public static final Block GLOWING_RED_WOOL = registerGlowingWool("glowing_red_wool", DyeColor.RED);
    public static final Block GLOWING_ORANGE_WOOL = registerGlowingWool("glowing_orange_wool", DyeColor.ORANGE);
    public static final Block GLOWING_YELLOW_WOOL = registerGlowingWool("glowing_yellow_wool", DyeColor.YELLOW);
    public static final Block GLOWING_LIME_WOOL = registerGlowingWool("glowing_lime_wool", DyeColor.LIME);
    public static final Block GLOWING_GREEN_WOOL = registerGlowingWool("glowing_green_wool", DyeColor.GREEN);
    public static final Block GLOWING_CYAN_WOOL = registerGlowingWool("glowing_cyan_wool", DyeColor.CYAN);
    public static final Block GLOWING_LIGHT_BLUE_WOOL = registerGlowingWool("glowing_light_blue_wool", DyeColor.LIGHT_BLUE);
    public static final Block GLOWING_BLUE_WOOL = registerGlowingWool("glowing_blue_wool", DyeColor.BLUE);
    public static final Block GLOWING_PURPLE_WOOL = registerGlowingWool("glowing_purple_wool", DyeColor.PURPLE);
    public static final Block GLOWING_MAGENTA_WOOL = registerGlowingWool("glowing_magenta_wool", DyeColor.MAGENTA);
    public static final Block GLOWING_PINK_WOOL = registerGlowingWool("glowing_pink_wool", DyeColor.PINK);

    public static final Block GLOWING_WHITE_CARPET = registerGlowingCarpet("glowing_white_carpet", DyeColor.WHITE);
    public static final Block GLOWING_LIGHT_GRAY_CARPET = registerGlowingCarpet("glowing_light_gray_carpet", DyeColor.LIGHT_GRAY);
    public static final Block GLOWING_GRAY_CARPET = registerGlowingCarpet("glowing_gray_carpet", DyeColor.GRAY);
    public static final Block GLOWING_BLACK_CARPET = registerGlowingCarpet("glowing_black_carpet", DyeColor.BLACK);
    public static final Block GLOWING_BROWN_CARPET = registerGlowingCarpet("glowing_brown_carpet", DyeColor.BROWN);
    public static final Block GLOWING_RED_CARPET = registerGlowingCarpet("glowing_red_carpet", DyeColor.RED);
    public static final Block GLOWING_ORANGE_CARPET = registerGlowingCarpet("glowing_orange_carpet", DyeColor.ORANGE);
    public static final Block GLOWING_YELLOW_CARPET = registerGlowingCarpet("glowing_yellow_carpet", DyeColor.YELLOW);
    public static final Block GLOWING_LIME_CARPET = registerGlowingCarpet("glowing_lime_carpet", DyeColor.LIME);
    public static final Block GLOWING_GREEN_CARPET = registerGlowingCarpet("glowing_green_carpet", DyeColor.GREEN);
    public static final Block GLOWING_CYAN_CARPET = registerGlowingCarpet("glowing_cyan_carpet", DyeColor.CYAN);
    public static final Block GLOWING_LIGHT_BLUE_CARPET = registerGlowingCarpet("glowing_light_blue_carpet", DyeColor.LIGHT_BLUE);
    public static final Block GLOWING_BLUE_CARPET = registerGlowingCarpet("glowing_blue_carpet", DyeColor.BLUE);
    public static final Block GLOWING_PURPLE_CARPET = registerGlowingCarpet("glowing_purple_carpet", DyeColor.PURPLE);
    public static final Block GLOWING_MAGENTA_CARPET = registerGlowingCarpet("glowing_magenta_carpet", DyeColor.MAGENTA);
    public static final Block GLOWING_PINK_CARPET = registerGlowingCarpet("glowing_pink_carpet", DyeColor.PINK);

    public static final Block GLOWING_WHITE_BED = registerGlowingBed("glowing_white_bed", DyeColor.WHITE);
    public static final Block GLOWING_LIGHT_GRAY_BED = registerGlowingBed("glowing_light_gray_bed", DyeColor.LIGHT_GRAY);
    public static final Block GLOWING_GRAY_BED = registerGlowingBed("glowing_gray_bed", DyeColor.GRAY);
    public static final Block GLOWING_BLACK_BED = registerGlowingBed("glowing_black_bed", DyeColor.BLACK);
    public static final Block GLOWING_BROWN_BED = registerGlowingBed("glowing_brown_bed", DyeColor.BROWN);
    public static final Block GLOWING_RED_BED = registerGlowingBed("glowing_red_bed", DyeColor.RED);
    public static final Block GLOWING_ORANGE_BED = registerGlowingBed("glowing_orange_bed", DyeColor.ORANGE);
    public static final Block GLOWING_YELLOW_BED = registerGlowingBed("glowing_yellow_bed", DyeColor.YELLOW);
    public static final Block GLOWING_LIME_BED = registerGlowingBed("glowing_lime_bed", DyeColor.LIME);
    public static final Block GLOWING_GREEN_BED = registerGlowingBed("glowing_green_bed", DyeColor.GREEN);
    public static final Block GLOWING_CYAN_BED = registerGlowingBed("glowing_cyan_bed", DyeColor.CYAN);
    public static final Block GLOWING_LIGHT_BLUE_BED = registerGlowingBed("glowing_light_blue_bed", DyeColor.LIGHT_BLUE);
    public static final Block GLOWING_BLUE_BED = registerGlowingBed("glowing_blue_bed", DyeColor.BLUE);
    public static final Block GLOWING_PURPLE_BED = registerGlowingBed("glowing_purple_bed", DyeColor.PURPLE);
    public static final Block GLOWING_MAGENTA_BED = registerGlowingBed("glowing_magenta_bed", DyeColor.MAGENTA);
    public static final Block GLOWING_PINK_BED = registerGlowingBed("glowing_pink_bed", DyeColor.PINK);

    public static final Block GLOWING_WHITE_BANNER = registerGlowingBanner("glowing_white_banner", DyeColor.WHITE);
    public static final Block GLOWING_LIGHT_GRAY_BANNER = registerGlowingBanner("glowing_light_gray_banner", DyeColor.LIGHT_GRAY);
    public static final Block GLOWING_GRAY_BANNER = registerGlowingBanner("glowing_gray_banner", DyeColor.GRAY);
    public static final Block GLOWING_BLACK_BANNER = registerGlowingBanner("glowing_black_banner", DyeColor.BLACK);
    public static final Block GLOWING_BROWN_BANNER = registerGlowingBanner("glowing_brown_banner", DyeColor.BROWN);
    public static final Block GLOWING_RED_BANNER = registerGlowingBanner("glowing_red_banner", DyeColor.RED);
    public static final Block GLOWING_ORANGE_BANNER = registerGlowingBanner("glowing_orange_banner", DyeColor.ORANGE);
    public static final Block GLOWING_YELLOW_BANNER = registerGlowingBanner("glowing_yellow_banner", DyeColor.YELLOW);
    public static final Block GLOWING_LIME_BANNER = registerGlowingBanner("glowing_lime_banner", DyeColor.LIME);
    public static final Block GLOWING_GREEN_BANNER = registerGlowingBanner("glowing_green_banner", DyeColor.GREEN);
    public static final Block GLOWING_CYAN_BANNER = registerGlowingBanner("glowing_cyan_banner", DyeColor.CYAN);
    public static final Block GLOWING_LIGHT_BLUE_BANNER = registerGlowingBanner("glowing_light_blue_banner", DyeColor.LIGHT_BLUE);
    public static final Block GLOWING_BLUE_BANNER = registerGlowingBanner("glowing_blue_banner", DyeColor.BLUE);
    public static final Block GLOWING_PURPLE_BANNER = registerGlowingBanner("glowing_purple_banner", DyeColor.PURPLE);
    public static final Block GLOWING_MAGENTA_BANNER = registerGlowingBanner("glowing_magenta_banner", DyeColor.MAGENTA);
    public static final Block GLOWING_PINK_BANNER = registerGlowingBanner("glowing_pink_banner", DyeColor.PINK);

    public static final Block GLOWING_WHITE_WALL_BANNER = registerGlowingWallBanner("glowing_white_wall_banner", GLOWING_WHITE_BANNER);
    public static final Block GLOWING_LIGHT_GRAY_WALL_BANNER = registerGlowingWallBanner("glowing_light_gray_wall_banner", GLOWING_LIGHT_GRAY_BANNER);
    public static final Block GLOWING_GRAY_WALL_BANNER = registerGlowingWallBanner("glowing_gray_wall_banner", GLOWING_GRAY_BANNER);
    public static final Block GLOWING_BLACK_WALL_BANNER = registerGlowingWallBanner("glowing_black_wall_banner", GLOWING_BLACK_BANNER);
    public static final Block GLOWING_BROWN_WALL_BANNER = registerGlowingWallBanner("glowing_brown_wall_banner", GLOWING_BROWN_BANNER);
    public static final Block GLOWING_RED_WALL_BANNER = registerGlowingWallBanner("glowing_red_wall_banner", GLOWING_RED_BANNER);
    public static final Block GLOWING_ORANGE_WALL_BANNER = registerGlowingWallBanner("glowing_orange_wall_banner", GLOWING_ORANGE_BANNER);
    public static final Block GLOWING_YELLOW_WALL_BANNER = registerGlowingWallBanner("glowing_yellow_wall_banner", GLOWING_YELLOW_BANNER);
    public static final Block GLOWING_LIME_WALL_BANNER = registerGlowingWallBanner("glowing_lime_wall_banner", GLOWING_LIME_BANNER);
    public static final Block GLOWING_GREEN_WALL_BANNER = registerGlowingWallBanner("glowing_green_wall_banner", GLOWING_GREEN_BANNER);
    public static final Block GLOWING_CYAN_WALL_BANNER = registerGlowingWallBanner("glowing_cyan_wall_banner", GLOWING_CYAN_BANNER);
    public static final Block GLOWING_LIGHT_BLUE_WALL_BANNER = registerGlowingWallBanner("glowing_light_blue_wall_banner", GLOWING_LIGHT_BLUE_BANNER);
    public static final Block GLOWING_BLUE_WALL_BANNER = registerGlowingWallBanner("glowing_blue_wall_banner", GLOWING_BLUE_BANNER);
    public static final Block GLOWING_PURPLE_WALL_BANNER = registerGlowingWallBanner("glowing_purple_wall_banner", GLOWING_PURPLE_BANNER);
    public static final Block GLOWING_MAGENTA_WALL_BANNER = registerGlowingWallBanner("glowing_magenta_wall_banner", GLOWING_MAGENTA_BANNER);
    public static final Block GLOWING_PINK_WALL_BANNER = registerGlowingWallBanner("glowing_pink_wall_banner", GLOWING_PINK_BANNER);


    private static Block registerGlowingWool(String id, DyeColor color) {
        Block block = register(id, AbstractBlock.Settings.create()
                .luminance(state -> 2).emissiveLighting(Blocks::always).postProcess(Blocks::always).mapColor(color)
                .instrument(NoteBlockInstrument.GUITAR).strength(0.8F).sounds(BlockSoundGroup.WOOL).burnable());
        BlockDyeMap.GLOWING_WOOL.put(color, block);
        return block;
    }
    private static Block registerGlowingCarpet(String id, DyeColor color) {
        Block block = register(id,
                (settings) -> new DyedCarpetBlock(color, settings),
                AbstractBlock.Settings.create().mapColor(color).emissiveLighting(Blocks::always).luminance(state -> 2)
                        .strength(0.1F).sounds(BlockSoundGroup.WOOL).burnable());
        BlockDyeMap.GLOWING_CARPET.put(color, block);
        return block;
    }
    private static Block registerGlowingBed(String id, DyeColor color) {
        Block block = register(id,
                (settings) -> new BedBlock(color, settings),
                AbstractBlock.Settings.create()
                        .mapColor((state) ->
                                state.get(BedBlock.PART) == BedPart.FOOT ? color.getMapColor() : MapColor.WHITE_GRAY
                        ).emissiveLighting(Blocks::always).luminance(state -> 2).sounds(BlockSoundGroup.WOOD)
                        .strength(0.2F).nonOpaque().burnable().pistonBehavior(PistonBehavior.DESTROY));
        BlockDyeMap.GLOWING_BED.put(color, block);
        return block;
    }
    private static Block registerGlowingBanner(String id, DyeColor color) {
        Block block = register(id,
                (settings) -> new GlowingBannerBlock(color, settings),
                AbstractBlock.Settings.create().mapColor(MapColor.OAK_TAN).solid().instrument(NoteBlockInstrument.BASS)
                        .emissiveLighting(Blocks::always).luminance(state -> 2)
                        .noCollision().strength(1.0F).sounds(BlockSoundGroup.WOOD).burnable());
        BlockDyeMap.GLOWING_BANNER.put(color, block);
        return block;
    }
    private static Block registerGlowingWallBanner(String id, Block baseBanner) {
        if (baseBanner instanceof BannerBlock banner) {
            Block block = register(id,
                    (settings) -> new WallBannerBlock(banner.getColor(), settings),
                    copyLootTable(baseBanner, true).mapColor(MapColor.OAK_TAN).solid().instrument(NoteBlockInstrument.BASS)
                            .emissiveLighting(Blocks::always).luminance(state -> 2)
                            .noCollision().strength(1.0F).sounds(BlockSoundGroup.WOOD).burnable());
            BlockDyeMap.GLOWING_WALL_BANNER.put(banner.getColor(), block);
            return block;
        }
        return register(id, AbstractBlock.Settings.create());
    }


    private static Block register(String id, AbstractBlock.Settings settings) {
        return register(id, Block::new, settings);
    }
    private static Block register(String id, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        return register(keyOf(id), factory, settings);
    }
    private static RegistryKey<Block> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.BLOCK, Ectoluminescence.id(id));
    }
    public static Block register(RegistryKey<Block> key, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        Block block = factory.apply(settings.registryKey(key));
        return Registry.register(Registries.BLOCK, key, block);
    }

    private static AbstractBlock.Settings copyLootTable(Block block, boolean copyTranslationKey) {
        AbstractBlock.Settings settings = AbstractBlock.Settings.create().lootTable(block.getLootTableKey());
        if (copyTranslationKey) {
            settings = settings.overrideTranslationKey(block.getTranslationKey());
        }

        return settings;
    }

    public static void onInitialize() {
        //Ectoluminescence.LOGGER.info("Registering blocks for " + Ectoluminescence.MOD_ID);
        BlockEntityType.BANNER.blocks = Sets.union(BlockEntityType.BANNER.blocks,
                Sets.union(new HashSet<>(BlockDyeMap.GLOWING_BANNER.values()), new HashSet<>(BlockDyeMap.GLOWING_WALL_BANNER.values())));
        BlockEntityType.BED.blocks = Sets.union(BlockEntityType.BANNER.blocks, new HashSet<>(BlockDyeMap.GLOWING_BED.values()));
    }
}
