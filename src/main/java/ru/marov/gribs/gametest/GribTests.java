package ru.marov.gribs.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import ru.marov.gribs.MarovGribs;
import ru.marov.gribs.block.PuffballBlock;
import ru.marov.gribs.entity.AgaricGuard;
import ru.marov.gribs.entity.Hopling;
import ru.marov.gribs.entity.Puffer;
import ru.marov.gribs.entity.SporeClouds;
import ru.marov.gribs.registry.ModBlocks;
import ru.marov.gribs.registry.ModEntities;

/** In-world checks of every mushroom ability. Run with: gradlew runGameTestServer */
@GameTestHolder(MarovGribs.MODID)
@PrefixGameTestTemplate(false)
public class GribTests {
    private static final String ARENA = "gametest/arena";
    private static final int SIZE = 7;
    private static final BlockPos CENTER = new BlockPos(3, 1, 3);

    /** Grass floor on y=0 so mobs have ground to stand on and mushrooms can grow. */
    private static void grassFloor(GameTestHelper helper) {
        for (int x = 0; x < SIZE; x++) {
            for (int z = 0; z < SIZE; z++) {
                helper.setBlock(new BlockPos(x, 0, z), Blocks.GRASS_BLOCK);
            }
        }
    }

    @GameTest(template = ARENA)
    public static void hopcapLaunchesWhateverStepsOnIt(GameTestHelper helper) {
        grassFloor(helper);
        helper.setBlock(CENTER, ModBlocks.HOPCAP.get());
        Pig pig = helper.spawn(EntityType.PIG, CENTER);
        double startY = pig.getY();
        helper.succeedWhen(() -> helper.assertTrue(pig.getY() > startY + 1.0, "pig was not launched"));
    }

    @GameTest(template = ARENA)
    public static void puffballBlindsWhoeverWalksIn(GameTestHelper helper) {
        grassFloor(helper);
        helper.setBlock(CENTER, ModBlocks.PUFFBALL.get());
        Zombie zombie = helper.spawn(EntityType.ZOMBIE, CENTER);
        helper.succeedWhen(() -> {
            helper.assertTrue(zombie.hasEffect(MobEffects.BLINDNESS), "zombie was not blinded");
            helper.assertBlockProperty(CENTER, PuffballBlock.PUFFED, true);
        });
    }

    @GameTest(template = ARENA)
    public static void pickingFlyAgaricAngersGuards(GameTestHelper helper) {
        grassFloor(helper);
        BlockPos agaric = new BlockPos(1, 1, 1);
        helper.setBlock(agaric, ModBlocks.FLY_AGARIC.get());
        AgaricGuard guard = helper.spawn(ModEntities.AGARIC_GUARD.get(), new BlockPos(5, 1, 5));
        // The vanilla mock ServerPlayer always reports creative mode, which guards ignore on purpose,
        // so the break event is posted for a survival mock player instead.
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        BlockPos abs = helper.absolutePos(agaric);
        NeoForge.EVENT_BUS.post(new BlockEvent.BreakEvent(helper.getLevel(), abs, helper.getBlockState(agaric), player));
        helper.destroyBlock(agaric);
        helper.succeedWhen(() -> helper.assertTrue(guard.getTarget() == player, "guard did not turn on the player"));
    }

    @GameTest(template = ARENA)
    public static void creativePlayersMayPickFlyAgarics(GameTestHelper helper) {
        grassFloor(helper);
        BlockPos agaric = new BlockPos(1, 1, 1);
        helper.setBlock(agaric, ModBlocks.FLY_AGARIC.get());
        AgaricGuard guard = helper.spawn(ModEntities.AGARIC_GUARD.get(), new BlockPos(5, 1, 5));
        Player player = helper.makeMockPlayer(GameType.CREATIVE);
        NeoForge.EVENT_BUS.post(new BlockEvent.BreakEvent(helper.getLevel(), helper.absolutePos(agaric), helper.getBlockState(agaric), player));
        helper.runAfterDelay(5, () -> {
            helper.assertTrue(guard.getTarget() == null, "guard attacked a creative player");
            helper.succeed();
        });
    }

    @GameTest(template = ARENA)
    public static void pufferPuffsBackWhenHit(GameTestHelper helper) {
        grassFloor(helper);
        Puffer puffer = helper.spawn(ModEntities.PUFFER.get(), CENTER);
        Pig attacker = helper.spawn(EntityType.PIG, CENTER.east());
        puffer.hurt(helper.getLevel().damageSources().mobAttack(attacker), 1.0F);
        helper.succeedWhen(() -> {
            helper.assertTrue(puffer.getPuffCooldown() > 0, "puffer did not puff");
            helper.assertTrue(attacker.hasEffect(MobEffects.POISON), "attacker was not poisoned");
            helper.assertFalse(puffer.hasEffect(MobEffects.POISON), "puffer poisoned itself");
        });
    }

    @GameTest(template = ARENA)
    public static void bonemealSproutsANeighbour(GameTestHelper helper) {
        grassFloor(helper);
        helper.setBlock(CENTER, ModBlocks.GLOWCAP.get());
        BlockState glowcap = helper.getBlockState(CENTER);
        for (int i = 0; i < 20; i++) {
            ModBlocks.GLOWCAP.get().performBonemeal(helper.getLevel(), helper.getLevel().random, helper.absolutePos(CENTER), glowcap);
        }
        helper.succeedWhen(() -> {
            long count = BlockPos.betweenClosedStream(new BlockPos(0, 1, 0), new BlockPos(SIZE - 1, 2, SIZE - 1))
                    .filter(pos -> helper.getBlockState(pos).is(ModBlocks.GLOWCAP.get()))
                    .count();
            helper.assertTrue(count >= 2, "bone meal did not sprout a new glowcap");
        });
    }

    @GameTest(template = ARENA)
    public static void sporeCloudReachesAboveTheGround(GameTestHelper helper) {
        grassFloor(helper);
        BlockPos pillar = CENTER.east();
        helper.setBlock(pillar, Blocks.STONE);
        Zombie zombie = helper.spawn(EntityType.ZOMBIE, pillar.above());
        SporeClouds.spawn(helper.getLevel(), helper.absoluteVec(Vec3.atBottomCenterOf(CENTER)), 2.5F, 60,
                SporeClouds.PUFFBALL_COLOR, null, new MobEffectInstance(MobEffects.BLINDNESS, 60));
        helper.succeedWhen(() -> helper.assertTrue(zombie.hasEffect(MobEffects.BLINDNESS), "cloud missed a mob standing on a block"));
    }

    @GameTest(template = ARENA)
    public static void sporeCloudFadesAway(GameTestHelper helper) {
        grassFloor(helper);
        SporeClouds.spawn(helper.getLevel(), helper.absoluteVec(Vec3.atBottomCenterOf(CENTER)), 2.5F, 40,
                SporeClouds.BOMB_COLOR, null, new MobEffectInstance(MobEffects.BLINDNESS, 60));
        helper.assertEntityPresent(ModEntities.SPORE_CLOUD.get());
        helper.runAfterDelay(45, () -> {
            helper.assertEntityNotPresent(ModEntities.SPORE_CLOUD.get());
            helper.succeed();
        });
    }

    @GameTest(template = ARENA, timeoutTicks = 120)
    public static void mushroomMobsLiveAndTick(GameTestHelper helper) {
        grassFloor(helper);
        Hopling hopling = helper.spawn(ModEntities.HOPLING.get(), new BlockPos(1, 1, 1));
        Puffer puffer = helper.spawn(ModEntities.PUFFER.get(), new BlockPos(5, 1, 1));
        AgaricGuard guard = helper.spawn(ModEntities.AGARIC_GUARD.get(), new BlockPos(3, 1, 5));
        helper.runAfterDelay(60, () -> {
            helper.assertTrue(hopling.isAlive() && puffer.isAlive() && guard.isAlive(), "a mushroom mob died on its own");
            helper.succeed();
        });
    }
}
