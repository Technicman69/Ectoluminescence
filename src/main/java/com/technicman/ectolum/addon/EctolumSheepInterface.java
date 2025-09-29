package com.technicman.ectolum.addon;

import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.SheepEntity;

public interface EctolumSheepInterface {
    TrackedData<Boolean> GLOWING =
            DataTracker.registerData(SheepEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    boolean ectolum$isGlowing();
    void ectolum$setGlowing(boolean glowing);
}
