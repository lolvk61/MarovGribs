package ru.marov.gribs.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import ru.marov.gribs.MarovGribs;

public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MarovGribs.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN = TABS.register("main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.marovgribs"))
                    .icon(() -> new ItemStack(ModItems.FLY_AGARIC.get()))
                    .displayItems((params, output) -> ModItems.ITEMS.getEntries().forEach(item -> output.accept(item.get())))
                    .build());

    private ModCreativeTabs() {
    }
}
