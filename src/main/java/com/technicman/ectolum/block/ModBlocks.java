package com.technicman.ectolum.block;

import com.technicman.ectolum.Ectoluminescence;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block GLOW_INK_TORCH = registerBlock("glow_ink_torch",
            new GlowInkTorchBlock(FabricBlockSettings.copyOf(Blocks.TORCH).luminance((state) -> state.get(Properties.WATERLOGGED) ? 12 : 8)));
    public static final Block GLOW_INK_WALL_TORCH = registerBlock("glow_ink_wall_torch",
            new GlowInkWallTorchBlock(FabricBlockSettings.copyOf(ModBlocks.GLOW_INK_TORCH).dropsLike(GLOW_INK_TORCH)));


    private static Block registerBlock(String name, Block block) {
        Identifier id = new Identifier(Ectoluminescence.MOD_ID, name);
        return Registry.register(Registries.BLOCK, id, block);

    }

    public static void onInitialize() {
        //Ectoluminescence.LOGGER.info("Registering blocks for " + Ectoluminescence.MOD_ID);
    }
}
