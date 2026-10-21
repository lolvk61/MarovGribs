package ru.marov.gribs.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import ru.marov.gribs.MarovGribs;
import ru.marov.gribs.worldgen.HugeParasolFeature;

public final class ModWorldgen {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, MarovGribs.MODID);

    public static final DeferredHolder<Feature<?>, HugeParasolFeature> HUGE_PARASOL = FEATURES.register("huge_parasol",
            () -> new HugeParasolFeature(NoneFeatureConfiguration.CODEC));

    private ModWorldgen() {
    }
}
