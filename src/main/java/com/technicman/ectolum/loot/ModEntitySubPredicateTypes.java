package com.technicman.ectolum.loot;

import com.mojang.serialization.MapCodec;
import com.technicman.ectolum.Ectoluminescence;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModEntitySubPredicateTypes {
    public static final MapCodec<SheepPredicate> SHEEP = Registry.register(
            Registries.ENTITY_SUB_PREDICATE_TYPE,
            Ectoluminescence.id("sheep"),
            SheepPredicate.CODEC);

    public static void onInitialize() {

    }
}
