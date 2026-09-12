package com.technicman.integration.jei;

import com.technicman.ectolum.Ectoluminescence;
import com.technicman.ectolum.recipe.*;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class Plugin implements IModPlugin {
    private static final Identifier ID = Ectoluminescence.identifier("jei_plugin");

    @Override
    public @NotNull Identifier getPluginUid() {
        return ID;
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        registration.getSmithingCategory().addExtension(SmithingEchoingFadeRecipe.class, new SmithingEchoingExtension<>());
        registration.getSmithingCategory().addExtension(SmithingEchoingPigmentRecipe.class, new SmithingEchoingExtension<>());
        registration.getSmithingCategory().addExtension(SmithingEchoingTwinkleRecipe.class, new SmithingEchoingExtension<>());
        registration.getSmithingCategory().addExtension(SmithingGlowingRecipe.class, new SmithingEchoingExtension<>());
    }
}
