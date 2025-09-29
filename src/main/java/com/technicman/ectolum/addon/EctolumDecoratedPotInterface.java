package com.technicman.ectolum.addon;

public interface EctolumDecoratedPotInterface {
    String SHERD_GLOW_OVERRIDES_KEY = "ectolum.sherd_glow_overrides";
    int BACK = 0;
    int LEFT = 1;
    int RIGHT = 2;
    int FRONT = 3;
    boolean ectolum$getSherdGlow(int index);
    void ectolum$setSherdGlow(int index, boolean glowing);
}
