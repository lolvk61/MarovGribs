package ru.marov.gribs.registry;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import ru.marov.gribs.MarovGribs;
import ru.marov.gribs.block.GlowcapBlock;
import ru.marov.gribs.block.GribBlock;
import ru.marov.gribs.block.HopcapBlock;
import ru.marov.gribs.block.PuffballBlock;

public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MarovGribs.MODID);

    public static final DeferredBlock<HopcapBlock> HOPCAP = BLOCKS.register("hopcap",
            () -> new HopcapBlock(gribProperties(MapColor.COLOR_LIGHT_GREEN)));
    public static final DeferredBlock<GlowcapBlock> GLOWCAP = BLOCKS.register("glowcap",
            () -> new GlowcapBlock(gribProperties(MapColor.COLOR_CYAN).lightLevel(state -> 12)));
    public static final DeferredBlock<PuffballBlock> PUFFBALL = BLOCKS.register("puffball",
            () -> new PuffballBlock(gribProperties(MapColor.SAND)));
    public static final DeferredBlock<GribBlock> FLY_AGARIC = BLOCKS.register("fly_agaric",
            () -> new GribBlock(gribProperties(MapColor.COLOR_RED)));

    private ModBlocks() {
    }

    private static BlockBehaviour.Properties gribProperties(MapColor color) {
        return BlockBehaviour.Properties.of()
                .mapColor(color)
                .noCollission()
                .randomTicks()
                .instabreak()
                .sound(SoundType.FUNGUS)
                .pushReaction(PushReaction.DESTROY);
    }
}
