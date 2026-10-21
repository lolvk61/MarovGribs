package ru.marov.gribs.entity.ai;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import ru.marov.gribs.entity.ParasolGrib;
import ru.marov.gribs.registry.ModBlocks;

/**
 * Sun-burning undead caught in daylight run for the shade of a nearby huge parasol mushroom,
 * unless they are busy chasing someone close by. Added to the mobs in the
 * {@code marovgribs:seeks_parasol_shade} entity tag.
 */
public class SeekParasolShadeGoal extends Goal {
    public static final int SEARCH_RADIUS = 12;
    private static final int CAP_SEARCH_HEIGHT = 8;
    private final PathfinderMob mob;
    @Nullable
    private BlockPos shade;
    private int cooldown;

    public SeekParasolShadeGoal(PathfinderMob mob) {
        this.mob = mob;
        setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (cooldown > 0) {
            cooldown--;
            return false;
        }
        cooldown = 20;
        if (!exposedToSun()) {
            return false;
        }
        LivingEntity target = mob.getTarget();
        if (target != null && mob.distanceToSqr(target) < 64.0) {
            return false;
        }
        shade = findShade(mob.level(), mob.blockPosition());
        return shade != null;
    }

    @Override
    public void start() {
        if (shade != null) {
            mob.getNavigation().moveTo(shade.getX() + 0.5, shade.getY(), shade.getZ() + 0.5, 1.25);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return shade != null && !mob.getNavigation().isDone() && mob.level().isDay();
    }

    @Override
    public void stop() {
        shade = null;
    }

    private boolean exposedToSun() {
        Level level = mob.level();
        return level.isDay() && !level.isRaining() && !mob.isInWaterOrBubble()
                && mob.getItemBySlot(EquipmentSlot.HEAD).isEmpty()
                && level.canSeeSky(BlockPos.containing(mob.getX(), mob.getEyeY(), mob.getZ()))
                && level.getEntitiesOfClass(ParasolGrib.class, mob.getBoundingBox().inflate(ParasolGrib.SHADE_RADIUS * 2.0),
                        parasol -> parasol.getShadeArea().intersects(mob.getBoundingBox())).isEmpty();
    }

    /**
     * The closest spot out of the sun: under the cap of a huge parasol mushroom, or next to the stem
     * of a walking parasol mushroom.
     */
    @Nullable
    public static BlockPos findShade(Level level, BlockPos origin) {
        BlockPos best = null;
        double bestDist = Double.MAX_VALUE;
        AABB area = new AABB(origin).inflate(SEARCH_RADIUS, CAP_SEARCH_HEIGHT, SEARCH_RADIUS);
        for (ParasolGrib parasol : level.getEntitiesOfClass(ParasolGrib.class, area)) {
            BlockPos spot = parasol.blockPosition();
            if (spot.distSqr(origin) < bestDist) {
                best = spot;
                bestDist = spot.distSqr(origin);
            }
        }
        BlockPos.MutableBlockPos cap = new BlockPos.MutableBlockPos();
        for (int dx = -SEARCH_RADIUS; dx <= SEARCH_RADIUS; dx++) {
            for (int dz = -SEARCH_RADIUS; dz <= SEARCH_RADIUS; dz++) {
                for (int dy = 1; dy <= CAP_SEARCH_HEIGHT; dy++) {
                    cap.set(origin.getX() + dx, origin.getY() + dy, origin.getZ() + dz);
                    if (!level.getBlockState(cap).is(ModBlocks.PARASOL_CAP.get())) {
                        continue;
                    }
                    BlockPos spot = groundUnder(level, cap);
                    if (spot != null && spot.distSqr(origin) < bestDist) {
                        best = spot;
                        bestDist = spot.distSqr(origin);
                    }
                    break;
                }
            }
        }
        return best;
    }

    @Nullable
    private static BlockPos groundUnder(Level level, BlockPos cap) {
        for (int y = 2; y <= CAP_SEARCH_HEIGHT; y++) {
            BlockPos feet = cap.below(y);
            BlockPos floor = feet.below();
            if (level.getBlockState(feet).isAir() && level.getBlockState(feet.above()).isAir()
                    && level.getBlockState(floor).isFaceSturdy(level, floor, Direction.UP)) {
                return level.canSeeSky(feet) ? null : feet.immutable();
            }
        }
        return null;
    }
}
