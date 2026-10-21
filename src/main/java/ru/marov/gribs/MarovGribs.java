package ru.marov.gribs;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import ru.marov.gribs.registry.ModBlocks;
import ru.marov.gribs.registry.ModCreativeTabs;
import ru.marov.gribs.registry.ModEntities;
import ru.marov.gribs.registry.ModItems;

@Mod(MarovGribs.MODID)
public class MarovGribs {
    public static final String MODID = "marovgribs";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MarovGribs(IEventBus modBus, ModContainer container) {
        ModBlocks.BLOCKS.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModEntities.ENTITY_TYPES.register(modBus);
        ModCreativeTabs.TABS.register(modBus);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
