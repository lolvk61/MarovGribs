package ru.marov.gribs.registry;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.DyeColor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import ru.marov.gribs.MarovGribs;

public final class ModDataComponents {
    public static final DeferredRegister.DataComponents COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MarovGribs.MODID);

    /** Colour of a russula cap: the item remembers which colour its russula was dyed. */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<DyeColor>> RUSSULA_COLOR =
            COMPONENTS.registerComponentType("russula_color",
                    builder -> builder.persistent(DyeColor.CODEC).networkSynchronized(DyeColor.STREAM_CODEC));

    private ModDataComponents() {
    }
}
