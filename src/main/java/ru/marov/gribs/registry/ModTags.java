package ru.marov.gribs.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import ru.marov.gribs.MarovGribs;

public final class ModTags {
    /** Sun-burning mobs that run for the shade of a huge parasol in daylight. */
    public static final TagKey<EntityType<?>> SEEKS_PARASOL_SHADE = TagKey.create(Registries.ENTITY_TYPE, MarovGribs.id("seeks_parasol_shade"));

    private ModTags() {
    }
}
