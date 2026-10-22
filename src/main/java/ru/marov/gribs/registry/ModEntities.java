package ru.marov.gribs.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
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
import ru.marov.gribs.entity.SporeCloud;
import ru.marov.gribs.entity.ThrownSporeBomb;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, MarovGribs.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<Hopling>> HOPLING = ENTITY_TYPES.register("hopling",
            () -> EntityType.Builder.of(Hopling::new, MobCategory.CREATURE)
                    .sized(0.5F, 0.7F)
                    .clientTrackingRange(8)
                    .build("hopling"));
    public static final DeferredHolder<EntityType<?>, EntityType<Puffer>> PUFFER = ENTITY_TYPES.register("puffer",
            () -> EntityType.Builder.of(Puffer::new, MobCategory.MONSTER)
                    .sized(0.75F, 0.9F)
                    .clientTrackingRange(8)
                    .build("puffer"));
    public static final DeferredHolder<EntityType<?>, EntityType<AgaricGuard>> AGARIC_GUARD = ENTITY_TYPES.register("agaric_guard",
            () -> EntityType.Builder.of(AgaricGuard::new, MobCategory.CREATURE)
                    .sized(0.9F, 1.35F)
                    .clientTrackingRange(10)
                    .build("agaric_guard"));
    public static final DeferredHolder<EntityType<?>, EntityType<ThrownSporeBomb>> SPORE_BOMB = ENTITY_TYPES.register("spore_bomb",
            () -> EntityType.Builder.<ThrownSporeBomb>of(ThrownSporeBomb::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("spore_bomb"));
    public static final DeferredHolder<EntityType<?>, EntityType<SporeCloud>> SPORE_CLOUD = ENTITY_TYPES.register("spore_cloud",
            () -> EntityType.Builder.<SporeCloud>of(SporeCloud::new, MobCategory.MISC)
                    .sized(1.0F, 1.0F)
                    .fireImmune()
                    .noSave()
                    .noSummon()
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build("spore_cloud"));

    public static final DeferredHolder<EntityType<?>, EntityType<Russula>> RUSSULA = ENTITY_TYPES.register("russula",
            () -> EntityType.Builder.of(Russula::new, MobCategory.CREATURE)
                    .sized(0.8F, 0.95F)
                    .clientTrackingRange(10)
                    .build("russula"));
    public static final DeferredHolder<EntityType<?>, EntityType<MinerGrib>> MINER_GRIB = ENTITY_TYPES.register("miner_grib",
            () -> EntityType.Builder.of(MinerGrib::new, MobCategory.MONSTER)
                    .sized(0.5F, MinerGrib.HEIGHT)
                    .clientTrackingRange(8)
                    .build("miner_grib"));
    public static final DeferredHolder<EntityType<?>, EntityType<CordycepsZombie>> CORDYCEPS_ZOMBIE = ENTITY_TYPES.register("cordyceps_zombie",
            () -> EntityType.Builder.<CordycepsZombie>of(CordycepsZombie::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .eyeHeight(1.74F)
                    .passengerAttachments(2.0125F)
                    .ridingOffset(-0.7F)
                    .clientTrackingRange(8)
                    .build("cordyceps_zombie"));
    public static final DeferredHolder<EntityType<?>, EntityType<FlynnFox>> FLYNN_FOX = ENTITY_TYPES.register("flynn_fox",
            () -> EntityType.Builder.<FlynnFox>of(FlynnFox::new, MobCategory.CREATURE)
                    .sized(0.6F, 0.7F)
                    .eyeHeight(0.4F)
                    .clientTrackingRange(10)
                    .immuneTo(Blocks.SWEET_BERRY_BUSH)
                    .build("flynn_fox"));
    public static final DeferredHolder<EntityType<?>, EntityType<DeathCap>> DEATH_CAP = ENTITY_TYPES.register("death_cap",
            () -> EntityType.Builder.of(DeathCap::new, MobCategory.MONSTER)
                    .sized(1.6F, 2.9F)
                    .clientTrackingRange(10)
                    .build("death_cap"));

    public static final DeferredHolder<EntityType<?>, EntityType<ParasolGrib>> PARASOL_GRIB = ENTITY_TYPES.register("parasol_grib",
            () -> EntityType.Builder.of(ParasolGrib::new, MobCategory.CREATURE)
                    .sized(1.2F, 4.6F)
                    .eyeHeight(3.6F)
                    .clientTrackingRange(10)
                    .build("parasol_grib"));
    public static final DeferredHolder<EntityType<?>, EntityType<ChanterelleGrib>> CHANTERELLE_GRIB = ENTITY_TYPES.register("chanterelle_grib",
            () -> EntityType.Builder.of(ChanterelleGrib::new, MobCategory.CREATURE)
                    .sized(0.5F, 0.85F)
                    .clientTrackingRange(8)
                    .build("chanterelle_grib"));

    private ModEntities() {
    }
}
