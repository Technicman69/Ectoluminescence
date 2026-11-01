package com.technicman.ectolum.entity;

import com.technicman.ectolum.Ectoluminescence;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {
    public static final EntityType<ClearItemFrameEntity> CLEAR_ITEM_FRAME = registerEntity("clear_item_frame",
            EntityType.Builder.<ClearItemFrameEntity>of(ClearItemFrameEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(10).updateInterval(Integer.MAX_VALUE)
    );
    private static <T extends Entity> EntityType<T> registerEntity(String name, EntityType.Builder<T> entityType) {
        Identifier id = Ectoluminescence.id(name);
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, id);
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, id, entityType.build(key));
    }

    public static void onInitialize() {
        //Ectoluminescence.LOGGER.info("Registering entities for " + Ectoluminescence.MOD_ID);
    }
}
