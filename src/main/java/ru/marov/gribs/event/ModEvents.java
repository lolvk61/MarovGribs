package ru.marov.gribs.event;

import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import ru.marov.gribs.MarovGribs;
import ru.marov.gribs.entity.AgaricGuard;
import ru.marov.gribs.entity.Hopling;
import ru.marov.gribs.entity.Puffer;
import ru.marov.gribs.registry.ModEntities;

@EventBusSubscriber(modid = MarovGribs.MODID)
public final class ModEvents {
    private ModEvents() {
    }

    @SubscribeEvent
    static void onAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.HOPLING.get(), Hopling.createAttributes().build());
        event.put(ModEntities.PUFFER.get(), Puffer.createAttributes().build());
        event.put(ModEntities.AGARIC_GUARD.get(), AgaricGuard.createAttributes().build());
    }

    @SubscribeEvent
    static void onSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(ModEntities.HOPLING.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Hopling::checkSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.PUFFER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.AGARIC_GUARD.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                AgaricGuard::checkSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }
}
