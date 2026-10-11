package com.technicman.ectolum.item;

import com.technicman.ectolum.Ectoluminescence;
import com.technicman.ectolum.block.ModBlocks;
import com.technicman.ectolum.entity.ModEntities;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.item.VerticallyAttachableBlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.function.Function;

public class ModItems {
    public static final Item GLOW_INK_TORCH_ITEM = register("glow_ink_torch",
            settings -> new VerticallyAttachableBlockItem(ModBlocks.GLOW_INK_TORCH, ModBlocks.GLOW_INK_WALL_TORCH, Direction.DOWN, settings));
    public static final Item CLEAR_ITEM_FRAME = register("clear_item_frame",
            settings -> new ClearItemFrameItem(ModEntities.CLEAR_ITEM_FRAME, settings));


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

    }
}
