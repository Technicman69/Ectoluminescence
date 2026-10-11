package com.technicman.integration.jei;

import com.technicman.ectolum.Ectoluminescence;
import com.technicman.ectolum.recipe.SmithingEchoingFadeRecipe;
import com.technicman.ectolum.recipe.SmithingEchoingPigmentRecipe;
import com.technicman.ectolum.recipe.SmithingEchoingTwinkleRecipe;
import com.technicman.ectolum.recipe.SmithingGlowingRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class Plugin implements IModPlugin {
    private static final Identifier ID = Ectoluminescence.id("jei_plugin");

    @Override
    public @NotNull Identifier getPluginUid() {
        return ID;
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        SmithingEchoingExtension.reload();

//        RecipeSynchronization.synchronizeRecipeSerializer(ModRecipes.SMITHING_ECHOING_FADE_RECIPE);
//        RecipeSynchronization.synchronizeRecipeSerializer(ModRecipes.SMITHING_ECHOING_PIGMENT_RECIPE);
//        RecipeSynchronization.synchronizeRecipeSerializer(ModRecipes.SMITHING_ECHOING_TWINKLE_RECIPE);
//        RecipeSynchronization.synchronizeRecipeSerializer(ModRecipes.SMITHING_GLOWING_RECIPE);

        registration.getSmithingCategory().addExtension(SmithingEchoingFadeRecipe.class, new SmithingEchoingExtension<>());
        registration.getSmithingCategory().addExtension(SmithingEchoingPigmentRecipe.class, new SmithingEchoingExtension<>());
        registration.getSmithingCategory().addExtension(SmithingEchoingTwinkleRecipe.class, new SmithingEchoingExtension<>());
        registration.getSmithingCategory().addExtension(SmithingGlowingRecipe.class, new SmithingEchoingExtension<>());
    }
}
