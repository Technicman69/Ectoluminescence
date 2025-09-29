package com.technicman.ectolum.entity;

import com.technicman.ectolum.Ectoluminescence;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<ClearItemFrameEntity> CLEAR_ITEM_FRAME = registerEntity("clear_item_frame",
            EntityType.Builder.<ClearItemFrameEntity>create(ClearItemFrameEntity::new, SpawnGroup.MISC).setDimensions(0.5F, 0.5F).maxTrackingRange(10).trackingTickInterval(Integer.MAX_VALUE)
    );
    private static <T extends Entity> EntityType<T> registerEntity(String name, EntityType.Builder<T> entityType) {
        Identifier id = new Identifier(Ectoluminescence.MOD_ID, name);
        return Registry.register(Registries.ENTITY_TYPE, id, entityType.build(name));
    }

    public static void onInitialize() {
        //Ectoluminescence.LOGGER.info("Registering entities for " + Ectoluminescence.MOD_ID);
    }
}
