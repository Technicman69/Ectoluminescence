package com.technicman.ectolum.block;

import com.technicman.ectolum.Ectoluminescence;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;

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
