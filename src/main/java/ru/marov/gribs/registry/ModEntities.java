package ru.marov.gribs.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import ru.marov.gribs.MarovGribs;
import ru.marov.gribs.entity.AgaricGuard;
import ru.marov.gribs.entity.Hopling;
import ru.marov.gribs.entity.Puffer;
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

    private ModEntities() {
    }
}
