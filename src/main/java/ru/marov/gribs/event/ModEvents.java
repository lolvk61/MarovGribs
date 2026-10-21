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
import ru.marov.gribs.entity.ChanterelleGrib;
import ru.marov.gribs.entity.ParasolGrib;
import ru.marov.gribs.entity.CordycepsZombie;
import ru.marov.gribs.entity.DeathCap;
import ru.marov.gribs.entity.FlynnFox;
import ru.marov.gribs.entity.Hopling;
import ru.marov.gribs.entity.MinerGrib;
import ru.marov.gribs.entity.Puffer;
import ru.marov.gribs.entity.Russula;
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
        event.put(ModEntities.RUSSULA.get(), Russula.createAttributes().build());
        event.put(ModEntities.MINER_GRIB.get(), MinerGrib.createAttributes().build());
        event.put(ModEntities.CORDYCEPS_ZOMBIE.get(), CordycepsZombie.createAttributes().build());
        event.put(ModEntities.FLYNN_FOX.get(), FlynnFox.createAttributes().build());
        event.put(ModEntities.DEATH_CAP.get(), DeathCap.createAttributes().build());
        event.put(ModEntities.PARASOL_GRIB.get(), ParasolGrib.createAttributes().build());
        event.put(ModEntities.CHANTERELLE_GRIB.get(), ChanterelleGrib.createAttributes().build());
    }

    @SubscribeEvent
    static void onSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(ModEntities.HOPLING.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Hopling::checkSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.PUFFER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.AGARIC_GUARD.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                AgaricGuard::checkSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.RUSSULA.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Russula::checkSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.MINER_GRIB.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MinerGrib::checkSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.CORDYCEPS_ZOMBIE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.DEATH_CAP.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                DeathCap::checkSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.PARASOL_GRIB.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                ParasolGrib::checkSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.CHANTERELLE_GRIB.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                ChanterelleGrib::checkSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }
}
