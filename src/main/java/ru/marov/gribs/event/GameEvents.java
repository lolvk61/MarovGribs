package ru.marov.gribs.event;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import ru.marov.gribs.MarovGribs;
import ru.marov.gribs.entity.AgaricGuard;
import ru.marov.gribs.entity.CordycepsZombie;
import ru.marov.gribs.entity.FlynnFox;
import ru.marov.gribs.entity.ai.SeekParasolShadeGoal;
import ru.marov.gribs.registry.ModBlocks;
import ru.marov.gribs.registry.ModEntities;
import ru.marov.gribs.registry.ModItems;
import ru.marov.gribs.registry.ModTags;

@EventBusSubscriber(modid = MarovGribs.MODID)
public final class GameEvents {
    private GameEvents() {
    }

    /** Picking a fly agaric angers the guards that look after the patch. */
    @SubscribeEvent
    static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (event.getState().is(ModBlocks.FLY_AGARIC.get()) && event.getLevel() instanceof Level level
                && !level.isClientSide && !player.isCreative() && !player.isSpectator()) {
            AgaricGuard.alertGuards(level, event.getPos(), player);
        }
    }

    /** A hopcap cushions the landing: fall damage is computed before the block gets to bounce you. */
    @SubscribeEvent
    static void onFall(LivingFallEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().getBlockState(entity.blockPosition()).is(ModBlocks.HOPCAP.get())) {
            event.setCanceled(true);
        }
    }

    /** A few zombies in the wild come out overgrown with cordyceps. */
    @SubscribeEvent
    static void onFinalizeSpawn(FinalizeSpawnEvent event) {
        Mob mob = event.getEntity();
        MobSpawnType type = event.getSpawnType();
        if (mob.getType() != EntityType.ZOMBIE || type != MobSpawnType.NATURAL && type != MobSpawnType.CHUNK_GENERATION
                || mob.getRandom().nextFloat() >= CordycepsZombie.INFECTION_CHANCE) {
            return;
        }
        CordycepsZombie infected = ModEntities.CORDYCEPS_ZOMBIE.get().create(event.getLevel().getLevel());
        if (infected == null) {
            return;
        }
        event.setSpawnCancelled(true);
        infected.moveTo(event.getX(), event.getY(), event.getZ(), mob.getYRot(), 0.0F);
        infected.finalizeSpawn(event.getLevel(), event.getDifficulty(), type, null);
        event.getLevel().addFreshEntityWithPassengers(infected);
    }

    /** A fox that takes a chanterelle into its mouth turns into FlynnFlyTaggart. */
    @SubscribeEvent
    static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof Fox fox && !(fox instanceof FlynnFox) && !fox.level().isClientSide
                && event.getSlot() == EquipmentSlot.MAINHAND && event.getTo().is(ModItems.CHANTERELLE.get())) {
            FlynnFox.transform(fox);
        }
    }

    /** Sun-burning undead learn to hide under huge parasols. */
    @SubscribeEvent
    static void onJoinLevel(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide && event.getEntity() instanceof PathfinderMob mob
                && mob.getType().is(ModTags.SEEKS_PARASOL_SHADE)) {
            mob.goalSelector.addGoal(1, new SeekParasolShadeGoal(mob));
        }
    }

    /** The death cap's hat keeps poison off its wearer. */
    @SubscribeEvent
    static void onEffectApplicable(MobEffectEvent.Applicable event) {
        if (event.getEffectInstance().is(MobEffects.POISON)
                && event.getEntity().getItemBySlot(EquipmentSlot.HEAD).is(ModItems.DEATH_CAP_HAT.get())) {
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
        }
    }
}
