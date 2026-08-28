package com.technicman.ectolum.integration.jei;

import com.technicman.ectolum.Ectoluminescence;
import com.technicman.ectolum.recipe.SmithingEchoingMatterialRecipe;
import com.technicman.ectolum.recipe.SmithingModifyNbtRecipe;
import com.technicman.ectolum.recipe.SmithingToggleEchoingFlagRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class Plugin implements IModPlugin {
    private static final Identifier ID = new Identifier(Ectoluminescence.MOD_ID, "jei_plugin");

    @Override
    public @NotNull Identifier getPluginUid() {
        return ID;
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        registration.getSmithingCategory().addExtension(SmithingEchoingMatterialRecipe.class, new SmithingEchoingMaterialRecipeExtension());
        registration.getSmithingCategory().addExtension(SmithingToggleEchoingFlagRecipe.class, new SmithingToggleEchoingFlagRecipeExtension());
        registration.getSmithingCategory().addExtension(SmithingModifyNbtRecipe.class, new SmithingModifyNbtRecipeExtension());
    }
}
