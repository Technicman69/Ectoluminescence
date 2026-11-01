package com.technicman.ectolum.loot;

import com.mojang.serialization.MapCodec;
import com.technicman.ectolum.Ectoluminescence;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class ModEntitySubPredicateTypes {
    public static final MapCodec<SheepPredicate> SHEEP = Registry.register(
            BuiltInRegistries.ENTITY_SUB_PREDICATE_TYPE,
            Ectoluminescence.id("sheep"),
            SheepPredicate.CODEC);

    public static void onInitialize() {

    }
}
