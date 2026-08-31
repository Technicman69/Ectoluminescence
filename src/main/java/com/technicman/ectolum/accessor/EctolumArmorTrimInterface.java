package com.technicman.ectolum.accessor;

import com.mojang.datafixers.util.Pair;
import com.technicman.ectolum.util.EchoingLayer;
import net.minecraft.item.trim.ArmorTrim;

public interface EctolumArmorTrimInterface {

    boolean ectolum$isGlowing();
    void ectolum$setGlowing(boolean glowing);
    boolean ectolum$hidden();
    void ectolum$setHidden(boolean glowing);

    EchoingLayer[] ectolum$getEchoingLayers();
    void ectolum$setEchoingLayers(EchoingLayer[] layers);
    boolean ectolum$hasEchoingLayers();
    Pair<ArmorTrim, ArmorTrim> ectolum$calculateTrimsAtInterval(long time);
}
