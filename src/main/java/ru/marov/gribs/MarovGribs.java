package ru.marov.gribs;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import ru.marov.gribs.registry.ModArmorMaterials;
import ru.marov.gribs.registry.ModBlocks;
import ru.marov.gribs.registry.ModCreativeTabs;
import ru.marov.gribs.registry.ModDataComponents;
import ru.marov.gribs.registry.ModEntities;
import ru.marov.gribs.registry.ModItems;
import ru.marov.gribs.registry.ModPotions;
import ru.marov.gribs.registry.ModRecipes;
import ru.marov.gribs.registry.ModWorldgen;

@Mod(MarovGribs.MODID)
public class MarovGribs {
    public static final String MODID = "marovgribs";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MarovGribs(IEventBus modBus, ModContainer container) {
        ModBlocks.BLOCKS.register(modBus);
        ModDataComponents.COMPONENTS.register(modBus);
        ModArmorMaterials.MATERIALS.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModEntities.ENTITY_TYPES.register(modBus);
        ModPotions.POTIONS.register(modBus);
        ModRecipes.SERIALIZERS.register(modBus);
        ModWorldgen.FEATURES.register(modBus);
        ModCreativeTabs.TABS.register(modBus);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
