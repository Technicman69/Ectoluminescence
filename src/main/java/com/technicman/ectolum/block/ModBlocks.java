package com.technicman.ectolum.block;

import com.technicman.ectolum.Ectoluminescence;
import com.technicman.ectolum.util.BlockDyeMap;
import net.minecraft.block.*;
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

    public static final Block GLOWING_WHITE_CARPET = registerGlowingCarpet("glowing_white_carpet", DyeColor.WHITE);

    public static final Block GLOWING_WHITE_BED = registerGlowingBed("glowing_white_bed", DyeColor.WHITE);

    public static final Block GLOWING_WHITE_BANNER = registerGlowingBanner("white_banner", DyeColor.WHITE);

    public static final Block GLOWING_WHITE_WALL_BANNER = registerGlowingWallBanner("white_wall_banner", GLOWING_WHITE_BANNER);


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
    }
}
