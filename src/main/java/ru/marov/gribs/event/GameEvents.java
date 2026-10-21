package ru.marov.gribs.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import ru.marov.gribs.MarovGribs;
import ru.marov.gribs.entity.AgaricGuard;
import ru.marov.gribs.registry.ModBlocks;

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
}
