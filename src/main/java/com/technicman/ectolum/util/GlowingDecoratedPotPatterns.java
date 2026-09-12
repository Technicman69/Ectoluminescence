package com.technicman.ectolum.util;

import net.minecraft.block.DecoratedPotPattern;
import net.minecraft.block.DecoratedPotPatterns;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Hashtable;
import java.util.Map;

public class GlowingDecoratedPotPatterns {
    private static final Map<Item, RegistryKey<DecoratedPotPattern>> SHERD_TO_PATTERN = new Hashtable<>(21);
    private static final Item[] VANILLA_SHERDS = new Item[] {
        Items.ANGLER_POTTERY_SHERD,
        Items.ARCHER_POTTERY_SHERD,
        Items.ARMS_UP_POTTERY_SHERD,
        Items.BLADE_POTTERY_SHERD,
        Items.BREWER_POTTERY_SHERD,
        Items.BURN_POTTERY_SHERD,
        Items.DANGER_POTTERY_SHERD,
        Items.EXPLORER_POTTERY_SHERD,
        Items.FLOW_POTTERY_SHERD,
        Items.FRIEND_POTTERY_SHERD,
        Items.GUSTER_POTTERY_SHERD,
        Items.HEART_POTTERY_SHERD,
        Items.HEARTBREAK_POTTERY_SHERD,
        Items.HOWL_POTTERY_SHERD,
        Items.MINER_POTTERY_SHERD,
        Items.MOURNER_POTTERY_SHERD,
        Items.PLENTY_POTTERY_SHERD,
        Items.PRIZE_POTTERY_SHERD,
        Items.SCRAPE_POTTERY_SHERD,
        Items.SHEAF_POTTERY_SHERD,
        Items.SHELTER_POTTERY_SHERD,
        Items.SKULL_POTTERY_SHERD,
        Items.SNORT_POTTERY_SHERD,
    };

    static {
        for (Item sherd : VANILLA_SHERDS) {
            SHERD_TO_PATTERN.put(sherd, registerFromItem(sherd));
        }
    }

    public static void mapSherdToPatternKey(Item sherd, RegistryKey<DecoratedPotPattern> key) {
        SHERD_TO_PATTERN.put(sherd, key);
    }

    private static RegistryKey<DecoratedPotPattern> registerFromItem(Item item) {
        RegistryKey<DecoratedPotPattern> baseKey = DecoratedPotPatterns.fromSherd(item);
        return registerFromBaseKey(baseKey);
    }

    private static RegistryKey<DecoratedPotPattern> registerFromBaseKey(RegistryKey<DecoratedPotPattern> basePatternKey) {
        RegistryKey<DecoratedPotPattern> glowingPatternKey = getKey(basePatternKey);
        DecoratedPotPattern pattern = new DecoratedPotPattern(glowingPatternKey.getValue());
        Registry.register(Registries.DECORATED_POT_PATTERN, glowingPatternKey, pattern);
        return glowingPatternKey;
    }

    public static RegistryKey<DecoratedPotPattern> getKey(RegistryKey<DecoratedPotPattern> basePatternKey) {
        Identifier id = basePatternKey.getValue().withSuffixedPath("_glowing");
        return RegistryKey.of(RegistryKeys.DECORATED_POT_PATTERN, id);
    }

    /**
     * Registers a sherd as such, that has a glowing override texture (thus, it requires such under `namespace:entity/decorated_pot/` path)
     * @param sherd Sherd sherd, whose decorated pot has a glowing texture override on face, that is this sherd.
     * @param textureID path after `namespace:entity/decorated_pot/` in which the override texture should reside.
     * @return some string, idk
     */
    public static DecoratedPotPattern register(Item sherd, Identifier textureID) {
        return register(sherd, textureID, new DecoratedPotPattern(textureID));
    }

    public static DecoratedPotPattern register(Item sherd, Identifier textureID, DecoratedPotPattern entry) {
        RegistryKey<DecoratedPotPattern> key = RegistryKey.of(RegistryKeys.DECORATED_POT_PATTERN, textureID);
        SHERD_TO_PATTERN.put(sherd, key);
        return Registry.register(Registries.DECORATED_POT_PATTERN, key, entry);
    }

    public static RegistryKey<DecoratedPotPattern> registerFromBaseKey(Item item, RegistryKey<DecoratedPotPattern> basePatternKey) {
        RegistryKey<DecoratedPotPattern> glowingPatternKey = getKey(basePatternKey);
        DecoratedPotPattern pattern = new DecoratedPotPattern(glowingPatternKey.getValue());
        Registry.register(Registries.DECORATED_POT_PATTERN, glowingPatternKey, pattern);
        SHERD_TO_PATTERN.put(item, glowingPatternKey);
        return glowingPatternKey;
    }

    @Nullable
    public static RegistryKey<DecoratedPotPattern> fromSherd(Item sherd) {
        return SHERD_TO_PATTERN.get(sherd);
    }

    public static void onInitialize() {}
}
