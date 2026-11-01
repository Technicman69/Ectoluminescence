package com.technicman.ectolum.sound;

import com.technicman.ectolum.Ectoluminescence;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {
    public static final SoundEvent PHANTOM_MEMBRANE_USE = registerSoundEvent("item.phantom_membrane.use");
    public static final SoundEvent ENTITY_CLEAR_ITEM_FRAME_ADD_ITEM = registerSoundEvent("entity.clear_item_frame.add_item");
    public static final SoundEvent ENTITY_CLEAR_ITEM_FRAME_BREAK = registerSoundEvent("entity.clear_item_frame.break");
    public static final SoundEvent ENTITY_CLEAR_ITEM_FRAME_PLACE = registerSoundEvent("entity.clear_item_frame.place");
    public static final SoundEvent ENTITY_CLEAR_ITEM_FRAME_REMOVE_ITEM = registerSoundEvent("entity.clear_item_frame.remove_item");
    public static final SoundEvent ENTITY_CLEAR_ITEM_FRAME_ROTATE_ITEM = registerSoundEvent("entity.clear_item_frame.rotate_item");
    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Ectoluminescence.id(name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    public static void onInitialize() {
        //Ectoluminescence.LOGGER.info("Registering sounds for " + Ectoluminescence.MOD_ID);
    }
}
