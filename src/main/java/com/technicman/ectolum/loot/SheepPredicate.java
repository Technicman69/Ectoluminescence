package com.technicman.ectolum.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.technicman.ectolum.target_access.SheepAccess;
import net.minecraft.advancements.criterion.EntitySubPredicate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public record SheepPredicate(Optional<Boolean> sheared, Optional<Boolean> glowing) implements EntitySubPredicate {
    public static final MapCodec<SheepPredicate> CODEC = RecordCodecBuilder.mapCodec((instance) ->
            instance.group(
                    Codec.BOOL.optionalFieldOf("sheared").forGetter(SheepPredicate::sheared),
                    Codec.BOOL.optionalFieldOf("glowing").forGetter(SheepPredicate::glowing)
                    ).apply(instance, SheepPredicate::new));

    @Override
    public MapCodec<SheepPredicate> codec() {
        return CODEC;
    }

    @Override
    public boolean matches(Entity entity, ServerLevel world, @Nullable Vec3 pos) {
        if (entity instanceof Sheep sheepEntity) {
            return (sheared.isEmpty() || sheepEntity.isSheared() == sheared.get()) &&
                    (glowing.isEmpty() || ((SheepAccess)sheepEntity).ectolum$isGlowing() == glowing.get());
        } else {
            return false;
        }
    }

    public static SheepPredicate glowing(boolean glowing) {
        return new SheepPredicate(Optional.empty(), Optional.of(glowing));
    }
}
