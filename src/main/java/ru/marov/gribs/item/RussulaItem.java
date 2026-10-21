package ru.marov.gribs.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import ru.marov.gribs.registry.ModDataComponents;
import ru.marov.gribs.registry.ModItems;

/** Сыроежка: a russula cap in the colour its russula was dyed. Ground in a mortar it makes dye. */
public class RussulaItem extends Item {
    public RussulaItem(Properties properties) {
        super(properties);
    }

    public static ItemStack of(DyeColor color, int count) {
        ItemStack stack = new ItemStack(ModItems.RUSSULA.get(), count);
        stack.set(ModDataComponents.RUSSULA_COLOR.get(), color);
        return stack;
    }

    public static DyeColor colorOf(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.RUSSULA_COLOR.get(), DyeColor.RED);
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable(getDescriptionId() + "." + colorOf(stack).getName());
    }
}
