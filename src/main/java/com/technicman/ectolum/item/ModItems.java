package com.technicman.ectolum.item;

import com.technicman.ectolum.Ectoluminescence;
import com.technicman.ectolum.block.ModBlocks;
import com.technicman.ectolum.entity.ModEntities;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class ModItems {
    public static final Item GLOW_INK_TORCH_ITEM = registerBlockItem("glow_ink_torch",
            new VerticallyAttachableBlockItem(ModBlocks.GLOW_INK_TORCH, ModBlocks.GLOW_INK_WALL_TORCH, new Item.Settings(), Direction.DOWN));
    public static final Item CLEAR_ITEM_FRAME = register("clear_item_frame", new ClearItemFrameItem(ModEntities.CLEAR_ITEM_FRAME, new Item.Settings()));

    private static Item registerBlockItem(String name, BlockItem blockItem) {
        Identifier id = new Identifier(Ectoluminescence.MOD_ID, name);
        return Registry.register(Registries.ITEM, id, blockItem);
    }

    private static Item register(String id, Item item) {
        Identifier identifier = new Identifier(Ectoluminescence.MOD_ID, id);
        return Registry.register(Registries.ITEM, identifier, item);
    }

    public static void onInitialize() {
        //Ectoluminescence.LOGGER.info("Registering items for " + Ectoluminescence.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> {
            content.addAfter(Items.SOUL_TORCH, GLOW_INK_TORCH_ITEM);
            content.addAfter(Items.GLOW_ITEM_FRAME, CLEAR_ITEM_FRAME);
        });

    }
}
