package ru.marov.gribs.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import ru.marov.gribs.item.RussulaItem;
import ru.marov.gribs.registry.ModItems;
import ru.marov.gribs.registry.ModRecipes;

/**
 * Mortar + a coloured mushroom in the crafting grid = two dyes of its colour. A russula gives the
 * colour it was dyed; the other mushrooms give their own. The mortar stays and wears down by one.
 */
public class MortarGrindingRecipe extends CustomRecipe {
    public static final int DYES_PER_GRIND = 2;

    public MortarGrindingRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Nullable
    public static DyeColor dyeFor(ItemStack stack) {
        if (stack.is(ModItems.RUSSULA.get())) {
            return RussulaItem.colorOf(stack);
        }
        if (stack.is(ModItems.FLY_AGARIC.get())) {
            return DyeColor.RED;
        }
        if (stack.is(ModItems.CHANTERELLE.get())) {
            return DyeColor.ORANGE;
        }
        if (stack.is(ModItems.HOPCAP.get())) {
            return DyeColor.LIME;
        }
        if (stack.is(ModItems.GLOWCAP.get())) {
            return DyeColor.CYAN;
        }
        if (stack.is(ModItems.PUFFBALL.get())) {
            return DyeColor.WHITE;
        }
        return null;
    }

    @Nullable
    private static DyeColor find(CraftingInput input) {
        boolean mortar = false;
        DyeColor color = null;
        int items = 0;
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            items++;
            if (stack.is(ModItems.MORTAR.get())) {
                mortar = true;
            } else {
                color = dyeFor(stack);
            }
        }
        return items == 2 && mortar ? color : null;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return find(input) != null;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        DyeColor color = find(input);
        return color == null ? ItemStack.EMPTY : new ItemStack(DyeItem.byColor(color), DYES_PER_GRIND);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.is(ModItems.MORTAR.get())) {
                ItemStack worn = stack.copyWithCount(1);
                worn.setDamageValue(worn.getDamageValue() + 1);
                if (worn.getDamageValue() < worn.getMaxDamage()) {
                    remaining.set(i, worn);
                }
            }
        }
        return remaining;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.MORTAR_GRINDING.get();
    }
}
