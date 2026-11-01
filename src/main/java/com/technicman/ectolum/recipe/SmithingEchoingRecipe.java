package com.technicman.ectolum.recipe;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.technicman.ectolum.Ectoluminescence;
import com.technicman.ectolum.component.EchoingLayer;
import com.technicman.ectolum.component.ModComponents;
import net.minecraft.world.item.crafting.*;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class SmithingEchoingRecipe extends SimpleSmithingRecipe {
    final Ingredient template;
    final Ingredient base;
    final Ingredient addition;
    private @Nullable PlacementInfo ingredientPlacement;

    public List<EchoingLayer> copyEchoingLayers(ItemStack item) {
        List<EchoingLayer> echoingLayers = item.get(ModComponents.ECHOING_LAYERS);
        if (echoingLayers == null) return EchoingLayer.emptyList();
            // If the echoing layer limit reached, refuse to craft
        else if (echoingLayers.size() >= Ectoluminescence.ECHOING_LAYER_LIMIT) {
            return null;
        }
        // If not, then copy layers from original item stack
        else {
            return new ArrayList<>(echoingLayers);
        }
    }

    public SmithingEchoingRecipe(Recipe.CommonInfo commonInfo, Ingredient template, Ingredient base, Ingredient addition) {
        super(commonInfo);
        this.template = template;
        this.base = base;
        this.addition = addition;
    }

    @Override
    protected @NonNull PlacementInfo createPlacementInfo() {
        return PlacementInfo.create(List.of(this.template, this.base, this.addition));
    }

    @Override
    public @NonNull Optional<Ingredient> templateIngredient() {
        return Optional.of(template);
    }

    @Override
    public @NonNull Ingredient baseIngredient() {
        return base;
    }


    @Override
    public @NonNull Optional<Ingredient> additionIngredient() {
        return Optional.of(addition);
    }

    @Override
    public @NonNull PlacementInfo placementInfo() {
        if (this.ingredientPlacement == null) {
            this.ingredientPlacement = PlacementInfo.create(List.of(this.template, this.base, this.addition));
        }

        return this.ingredientPlacement;
    }

    @Override
    public boolean matches(SmithingRecipeInput smithingRecipeInput, @NonNull Level world) {
        ItemStack armor = smithingRecipeInput.base();
        return Ingredient.testOptionalIngredient(this.templateIngredient(), smithingRecipeInput.template()) &&
                Ingredient.testOptionalIngredient(this.additionIngredient(), smithingRecipeInput.addition()) &&
                baseIngredient().test(armor) &&
                armor.has(DataComponents.TRIM);
    }

    public static <T extends SmithingEchoingRecipe> RecipeSerializer<T> serializer(RecipeFactory<T> factory) {
        return new RecipeSerializer<>(RecordCodecBuilder.mapCodec(
                (instance) -> instance.group(
                        CommonInfo.MAP_CODEC.forGetter((recipe) -> recipe.commonInfo),
                        Ingredient.CODEC.fieldOf("template").forGetter((recipe) -> recipe.template),
                        Ingredient.CODEC.fieldOf("base").forGetter((recipe) -> recipe.base),
                        Ingredient.CODEC.fieldOf("addition").forGetter((recipe) -> recipe.addition)
                ).apply(instance, factory::create)
        ),
        StreamCodec.composite(
                CommonInfo.STREAM_CODEC, recipe -> recipe.commonInfo,
                Ingredient.CONTENTS_STREAM_CODEC, (recipe) -> recipe.template,
                Ingredient.CONTENTS_STREAM_CODEC, (recipe) -> recipe.base,
                Ingredient.CONTENTS_STREAM_CODEC, (recipe) -> recipe.addition,
                factory::create
        ));
    }

    @FunctionalInterface
    public interface RecipeFactory<T extends SmithingEchoingRecipe> {
        T create(Recipe.CommonInfo commonInfo, Ingredient template, Ingredient base, Ingredient addition);
    }
}
