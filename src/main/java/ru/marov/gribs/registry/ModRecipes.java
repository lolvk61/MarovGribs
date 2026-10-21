package ru.marov.gribs.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import ru.marov.gribs.MarovGribs;
import ru.marov.gribs.recipe.MortarGrindingRecipe;

public final class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, MarovGribs.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<MortarGrindingRecipe>> MORTAR_GRINDING =
            SERIALIZERS.register("mortar_grinding", () -> new SimpleCraftingRecipeSerializer<>(MortarGrindingRecipe::new));

    private ModRecipes() {
    }
}
