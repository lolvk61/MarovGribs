package ru.marov.gribs.gametest;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import ru.marov.gribs.MarovGribs;
import ru.marov.gribs.block.GribAwakening;
import ru.marov.gribs.block.PuffballBlock;
import ru.marov.gribs.entity.AgaricGuard;
import ru.marov.gribs.entity.ChanterelleGrib;
import ru.marov.gribs.entity.ParasolGrib;
import ru.marov.gribs.entity.CordycepsZombie;
import ru.marov.gribs.entity.DeathCap;
import ru.marov.gribs.entity.MinerGrib;
import ru.marov.gribs.entity.Puffer;
import ru.marov.gribs.entity.Russula;
import ru.marov.gribs.entity.SporeClouds;
import ru.marov.gribs.entity.ai.SeekParasolShadeGoal;
import ru.marov.gribs.item.RussulaItem;
import ru.marov.gribs.registry.ModBlocks;
import ru.marov.gribs.registry.ModEntities;
import ru.marov.gribs.registry.ModItems;
import ru.marov.gribs.registry.ModPotions;
import ru.marov.gribs.worldgen.HugeParasolFeature;

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

    @GameTest(template = ARENA)
    public static void myceliumSpreadsOverDirt(GameTestHelper helper) {
        for (int x = 0; x < SIZE; x++) {
            for (int z = 0; z < SIZE; z++) {
                helper.setBlock(new BlockPos(x, 0, z), Blocks.DIRT);
            }
        }
        BlockPos center = new BlockPos(3, 0, 3);
        helper.setBlock(center, ModBlocks.MAROV_MYCELIUM.get());
        // Like mycelium it only spreads in light 9+, so make it day and let the light settle first.
        helper.getLevel().setDayTime(6000);
        helper.runAfterDelay(10, () -> {
            BlockState mycelium = helper.getBlockState(center);
            for (int i = 0; i < 300; i++) {
                mycelium.randomTick(helper.getLevel(), helper.absolutePos(center), helper.getLevel().random);
            }
        });
        helper.succeedWhen(() -> {
            long spread = BlockPos.betweenClosedStream(new BlockPos(0, 0, 0), new BlockPos(SIZE - 1, 0, SIZE - 1))
                    .filter(pos -> helper.getBlockState(pos).is(ModBlocks.MAROV_MYCELIUM.get()))
                    .count();
            helper.assertTrue(spread >= 2, "mycelium did not spread");
        });
    }

    @GameTest(template = ARENA)
    public static void densePatchAwakensItsKeeper(GameTestHelper helper) {
        grassFloor(helper);
        for (int x = 1; x <= 5; x++) {
            for (int z = 1; z <= 5; z++) {
                if ((x + z) % 2 == 0) {
                    helper.setBlock(new BlockPos(x, 1, z), ModBlocks.FLY_AGARIC.get());
                }
            }
        }
        Mob keeper = GribAwakening.tryAwaken(helper.getLevel(), helper.absolutePos(CENTER), helper.getBlockState(CENTER));
        helper.assertTrue(keeper instanceof AgaricGuard, "a patch of 13 fly agarics did not wake a guard");
        helper.assertBlockNotPresent(ModBlocks.FLY_AGARIC.get(), CENTER);
        helper.succeed();
    }

    @GameTest(template = ARENA)
    public static void smallPatchStaysAsleep(GameTestHelper helper) {
        grassFloor(helper);
        helper.setBlock(CENTER, ModBlocks.FLY_AGARIC.get());
        helper.setBlock(CENTER.east(), ModBlocks.FLY_AGARIC.get());
        Mob keeper = GribAwakening.tryAwaken(helper.getLevel(), helper.absolutePos(CENTER), helper.getBlockState(CENTER));
        helper.assertTrue(keeper == null, "two fly agarics should not be enough");
        helper.succeed();
    }

    @GameTest(template = ARENA)
    public static void parasolGrowsAndGivesShade(GameTestHelper helper) {
        grassFloor(helper);
        helper.setBlock(CENTER, ModBlocks.PARASOL_SPROUT.get());
        boolean grown = HugeParasolFeature.grow(helper.getLevel(), helper.absolutePos(CENTER), helper.getLevel().random);
        helper.assertTrue(grown, "parasol did not grow");
        helper.assertBlockPresent(ModBlocks.PARASOL_STEM.get(), CENTER);
        BlockPos shadeSpot = helper.absolutePos(CENTER.east(2));
        helper.succeedWhen(() -> {
            helper.assertFalse(helper.getLevel().canSeeSky(shadeSpot), "no shade under the cap");
            BlockPos found = SeekParasolShadeGoal.findShade(helper.getLevel(), helper.absolutePos(new BlockPos(6, 1, 6)));
            helper.assertTrue(found != null && !helper.getLevel().canSeeSky(found), "undead would not find the shade");
        });
    }

    @GameTest(template = ARENA)
    public static void parasolMobShieldsUndeadFromTheSun(GameTestHelper helper) {
        grassFloor(helper);
        ParasolGrib parasol = helper.spawn(ModEntities.PARASOL_GRIB.get(), CENTER);
        parasol.setNoAi(true);
        Zombie zombie = helper.spawn(EntityType.ZOMBIE, CENTER.east());
        zombie.igniteForSeconds(8.0F);
        helper.succeedWhen(() -> {
            helper.assertFalse(zombie.isOnFire(), "the zombie under the cap is still burning");
            helper.assertTrue(zombie.hasEffect(MobEffects.FIRE_RESISTANCE), "the zombie under the cap is not protected");
        });
    }

    @GameTest(template = ARENA)
    public static void undeadFindTheWalkingParasol(GameTestHelper helper) {
        grassFloor(helper);
        ParasolGrib parasol = helper.spawn(ModEntities.PARASOL_GRIB.get(), new BlockPos(5, 1, 5));
        parasol.setNoAi(true);
        BlockPos found = SeekParasolShadeGoal.findShade(helper.getLevel(), helper.absolutePos(new BlockPos(1, 1, 1)));
        helper.assertTrue(parasol.blockPosition().equals(found), "shade search did not lead to the parasol mushroom");
        helper.succeed();
    }

    @GameTest(template = ARENA)
    public static void chanterelleAndParasolBedsAwaken(GameTestHelper helper) {
        grassFloor(helper);
        for (int x = 0; x < SIZE; x++) {
            for (int z = 0; z < 3; z++) {
                helper.setBlock(new BlockPos(x, 1, z), ModBlocks.CHANTERELLE.get());
                helper.setBlock(new BlockPos(x, 1, z + 4), ModBlocks.PARASOL_SPROUT.get());
            }
        }
        BlockPos chanterelle = new BlockPos(3, 1, 1);
        BlockPos sprout = new BlockPos(3, 1, 5);
        Mob fox = GribAwakening.tryAwaken(helper.getLevel(), helper.absolutePos(chanterelle), helper.getBlockState(chanterelle));
        Mob parasol = GribAwakening.tryAwaken(helper.getLevel(), helper.absolutePos(sprout), helper.getBlockState(sprout));
        helper.assertTrue(fox instanceof ChanterelleGrib, "a bed of chanterelles did not wake a chanterelle");
        helper.assertTrue(parasol instanceof ParasolGrib, "a bed of parasol sprouts did not wake a parasol mushroom");
        helper.succeed();
    }

    @GameTest(template = ARENA)
    public static void russulaDyesAndShearsInColour(GameTestHelper helper) {
        grassFloor(helper);
        Russula russula = helper.spawn(ModEntities.RUSSULA.get(), CENTER);
        russula.setColor(DyeColor.RED);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BLUE_DYE));
        russula.mobInteract(player, InteractionHand.MAIN_HAND);
        helper.assertTrue(russula.getColor() == DyeColor.BLUE, "dye did not recolour the russula");
        List<ItemStack> caps = russula.onSheared(null, new ItemStack(Items.SHEARS), helper.getLevel(), russula.blockPosition());
        helper.assertTrue(russula.isSheared(), "russula was not sheared");
        helper.assertTrue(!caps.isEmpty() && caps.stream().allMatch(cap -> RussulaItem.colorOf(cap) == DyeColor.BLUE),
                "sheared caps are not blue");
        helper.assertFalse(russula.isShearable(null, new ItemStack(Items.SHEARS), helper.getLevel(), russula.blockPosition()),
                "a sheared russula can be sheared again");
        helper.succeed();
    }

    @GameTest(template = ARENA)
    public static void mortarGrindsRussulaIntoDye(GameTestHelper helper) {
        ItemStack mortar = new ItemStack(ModItems.MORTAR.get());
        CraftingInput input = CraftingInput.of(2, 1, List.of(mortar, RussulaItem.of(DyeColor.PURPLE, 1)));
        var recipe = helper.getLevel().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, input, helper.getLevel());
        helper.assertTrue(recipe.isPresent(), "no recipe for mortar + russula");
        ItemStack result = recipe.get().value().assemble(input, helper.getLevel().registryAccess());
        helper.assertTrue(result.is(Items.PURPLE_DYE) && result.getCount() == 2, "expected 2 purple dye, got " + result);
        ItemStack worn = recipe.get().value().getRemainingItems(input).get(0);
        helper.assertTrue(worn.is(ModItems.MORTAR.get()) && worn.getDamageValue() == 1, "mortar was not kept with 1 wear");
        helper.succeed();
    }

    @GameTest(template = ARENA)
    public static void mushroomsBrewIntoExtracts(GameTestHelper helper) {
        PotionBrewing brewing = helper.getLevel().potionBrewing();
        ItemStack awkward = PotionContents.createItemStack(Items.POTION, Potions.AWKWARD);
        ItemStack extract = brewing.mix(new ItemStack(ModItems.FLY_AGARIC.get()), awkward);
        helper.assertTrue(extract.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).is(ModPotions.FLY_AGARIC_EXTRACT),
                "awkward potion + fly agaric did not brew a fly agaric extract");
        ItemStack longExtract = brewing.mix(new ItemStack(Items.REDSTONE), extract);
        helper.assertTrue(longExtract.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).is(ModPotions.LONG_FLY_AGARIC_EXTRACT),
                "redstone did not lengthen the extract");
        ItemStack chanterelle = brewing.mix(new ItemStack(ModItems.CHANTERELLE.get()), awkward);
        helper.assertTrue(chanterelle.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).is(ModPotions.CHANTERELLE_EXTRACT),
                "awkward potion + chanterelle did not brew a chanterelle extract");
        helper.succeed();
    }

    @GameTest(template = ARENA)
    public static void eatenMushroomsOnlyFeed(GameTestHelper helper) {
        for (var item : List.of(ModItems.HOPCAP, ModItems.GLOWCAP, ModItems.PUFFBALL, ModItems.CHANTERELLE,
                ModItems.PARASOL_SPROUT, ModItems.RUSSULA, ModItems.CORDYCEPS)) {
            FoodProperties food = new ItemStack(item.get()).get(DataComponents.FOOD);
            helper.assertTrue(food != null && food.nutrition() > 0 && food.effects().isEmpty(),
                    item.getId() + " should only feed, without effects");
        }
        FoodProperties agaric = new ItemStack(ModItems.FLY_AGARIC.get()).get(DataComponents.FOOD);
        helper.assertTrue(agaric != null && agaric.effects().stream().anyMatch(e -> e.effect().is(MobEffects.POISON))
                        && agaric.effects().stream().anyMatch(e -> e.effect().is(MobEffects.CONFUSION))
                        && agaric.effects().stream().noneMatch(e -> e.effect().is(MobEffects.DAMAGE_BOOST)),
                "a raw fly agaric should poison and sicken, not strengthen");
        helper.succeed();
    }

    @GameTest(template = ARENA)
    public static void mortarIsCraftable(GameTestHelper helper) {
        ItemStack stick = new ItemStack(Items.STICK);
        ItemStack stone = new ItemStack(Items.COBBLESTONE);
        CraftingInput input = CraftingInput.of(3, 3, List.of(
                ItemStack.EMPTY, ItemStack.EMPTY, stick,
                stone, ItemStack.EMPTY, stone,
                ItemStack.EMPTY, stone, ItemStack.EMPTY));
        var recipe = helper.getLevel().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, input, helper.getLevel());
        helper.assertTrue(recipe.isPresent(), "no recipe for the mortar");
        helper.assertTrue(recipe.get().value().assemble(input, helper.getLevel().registryAccess()).is(ModItems.MORTAR.get()),
                "the recipe does not make a mortar");
        helper.succeed();
    }

    @GameTest(template = ARENA)
    public static void foxWithChanterelleBecomesFlynn(GameTestHelper helper) {
        grassFloor(helper);
        Fox fox = helper.spawn(EntityType.FOX, CENTER);
        fox.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.CHANTERELLE.get()));
        helper.succeedWhen(() -> {
            helper.assertEntityPresent(ModEntities.FLYNN_FOX.get());
            helper.assertTrue(fox.isRemoved(), "the old fox is still there");
        });
    }

    @GameTest(template = ARENA)
    public static void cordycepsZombieIsTougher(GameTestHelper helper) {
        grassFloor(helper);
        CordycepsZombie infected = helper.spawn(ModEntities.CORDYCEPS_ZOMBIE.get(), CENTER);
        Zombie plain = helper.spawn(EntityType.ZOMBIE, CENTER.east());
        helper.assertTrue(infected.getMaxHealth() > plain.getMaxHealth(), "cordyceps zombie is not tougher");
        helper.assertTrue(infected.getAttributeValue(Attributes.ATTACK_DAMAGE) > plain.getAttributeValue(Attributes.ATTACK_DAMAGE),
                "cordyceps zombie does not hit harder");
        infected.setGrowthVariant(7);
        CompoundTag saved = new CompoundTag();
        infected.addAdditionalSaveData(saved);
        CordycepsZombie loaded = ModEntities.CORDYCEPS_ZOMBIE.get().create(helper.getLevel());
        loaded.readAdditionalSaveData(saved);
        helper.assertTrue(loaded.getGrowthVariant() == 7, "growth pattern was not saved");
        helper.succeed();
    }

    @GameTest(template = ARENA, timeoutTicks = 600)
    public static void minerDigsATunnel(GameTestHelper helper) {
        for (int x = 0; x < SIZE; x++) {
            for (int z = 0; z < SIZE; z++) {
                for (int y = 0; y <= 3; y++) {
                    helper.setBlock(new BlockPos(x, y, z), Blocks.STONE);
                }
            }
        }
        helper.setBlock(CENTER, Blocks.AIR);
        helper.setBlock(CENTER.above(), Blocks.AIR);
        helper.spawn(ModEntities.MINER_GRIB.get(), CENTER);
        helper.succeedWhen(() -> {
            long dug = BlockPos.betweenClosedStream(new BlockPos(0, 1, 0), new BlockPos(SIZE - 1, 2, SIZE - 1))
                    .filter(pos -> helper.getBlockState(pos).isAir())
                    .count();
            helper.assertTrue(dug >= 4, "miner dug only " + (dug - 2) + " blocks");
        });
    }

    @GameTest(template = ARENA)
    public static void minerWallsItselfInWhenHit(GameTestHelper helper) {
        grassFloor(helper);
        MinerGrib miner = helper.spawn(ModEntities.MINER_GRIB.get(), CENTER);
        Pig attacker = helper.spawn(EntityType.PIG, new BlockPos(0, 1, 0));
        miner.hurt(helper.getLevel().damageSources().mobAttack(attacker), 1.0F);
        helper.succeedWhen(() -> {
            helper.assertTrue(miner.isHiding(), "miner is not hiding");
            BlockPos at = miner.blockPosition();
            long walls = BlockPos.betweenClosedStream(at.offset(-1, 0, -1), at.offset(1, 2, 1))
                    .filter(pos -> helper.getLevel().getBlockState(pos).is(Blocks.COBBLESTONE)
                            || helper.getLevel().getBlockState(pos).is(Blocks.COBBLED_DEEPSLATE))
                    .count();
            helper.assertTrue(walls >= 6, "miner placed only " + walls + " wall blocks");
        });
    }

    @GameTest(template = ARENA)
    public static void deathCapPoisonsRootsAndSummons(GameTestHelper helper) {
        grassFloor(helper);
        DeathCap boss = helper.spawn(ModEntities.DEATH_CAP.get(), CENTER);
        Pig victim = helper.spawn(EntityType.PIG, CENTER.east(2));
        boss.sporeNova();
        boss.root(victim);
        boss.setHealth(boss.getMaxHealth() * 0.6F);
        helper.succeedWhen(() -> {
            helper.assertTrue(victim.hasEffect(MobEffects.POISON), "spore nova did not poison the pig");
            helper.assertTrue(victim.hasEffect(MobEffects.MOVEMENT_SLOWDOWN), "roots did not hold the pig");
            helper.assertFalse(boss.hasEffect(MobEffects.POISON), "the boss poisoned itself");
            helper.assertTrue(helper.getLevel().getEntitiesOfClass(Puffer.class, boss.getBoundingBox().inflate(8.0)).size() >= 3,
                    "boss did not call puffers at 2/3 health");
        });
    }

    @GameTest(template = ARENA)
    public static void deathCapHatKeepsSporesAndPoisonOff(GameTestHelper helper) {
        grassFloor(helper);
        Pig pig = helper.spawn(EntityType.PIG, CENTER);
        pig.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ModItems.DEATH_CAP_HAT.get()));
        SporeClouds.spawn(helper.getLevel(), helper.absoluteVec(Vec3.atBottomCenterOf(CENTER)), 2.5F, 60,
                SporeClouds.PUFFBALL_COLOR, null, new MobEffectInstance(MobEffects.BLINDNESS, 60));
        pig.addEffect(new MobEffectInstance(MobEffects.POISON, 100));
        helper.runAfterDelay(20, () -> {
            helper.assertFalse(pig.hasEffect(MobEffects.BLINDNESS), "spore cloud got through the hat");
            helper.assertFalse(pig.hasEffect(MobEffects.POISON), "poison got through the hat");
            helper.succeed();
        });
    }

    @GameTest(template = ARENA, timeoutTicks = 120)
    public static void mushroomMobsLiveAndTick(GameTestHelper helper) {
        grassFloor(helper);
        List<Mob> mobs = List.of(
                helper.spawn(ModEntities.HOPLING.get(), new BlockPos(1, 1, 1)),
                helper.spawn(ModEntities.PUFFER.get(), new BlockPos(5, 1, 1)),
                helper.spawn(ModEntities.AGARIC_GUARD.get(), new BlockPos(3, 1, 5)),
                helper.spawn(ModEntities.RUSSULA.get(), new BlockPos(1, 1, 5)),
                helper.spawn(ModEntities.FLYNN_FOX.get(), new BlockPos(5, 1, 5)),
                helper.spawn(ModEntities.CORDYCEPS_ZOMBIE.get(), new BlockPos(3, 1, 1)),
                helper.spawn(ModEntities.CHANTERELLE_GRIB.get(), new BlockPos(1, 1, 3)),
                helper.spawn(ModEntities.PARASOL_GRIB.get(), new BlockPos(5, 1, 3)));
        helper.runAfterDelay(60, () -> {
            for (Mob mob : mobs) {
                helper.assertTrue(mob.isAlive(), mob.getType().getDescriptionId() + " died on its own");
            }
            helper.succeed();
        });
    }
}
