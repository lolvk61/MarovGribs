package ru.marov.gribs.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import ru.marov.gribs.MarovGribs;
import ru.marov.gribs.client.model.AgaricGuardModel;
import ru.marov.gribs.client.model.ChanterelleGribModel;
import ru.marov.gribs.client.model.ParasolGribModel;
import ru.marov.gribs.client.model.geo.ChanterelleGribGeo;
import ru.marov.gribs.client.model.geo.ParasolGribGeo;
import ru.marov.gribs.entity.ChanterelleGrib;
import ru.marov.gribs.entity.ParasolGrib;
import ru.marov.gribs.client.model.DeathCapModel;
import ru.marov.gribs.client.model.HoplingModel;
import ru.marov.gribs.client.model.MinerGribModel;
import ru.marov.gribs.client.model.PufferModel;
import ru.marov.gribs.client.model.RussulaModel;
import ru.marov.gribs.client.model.geo.AgaricGuardGeo;
import ru.marov.gribs.client.model.geo.CordycepsGeo;
import ru.marov.gribs.client.model.geo.DeathCapGeo;
import ru.marov.gribs.client.model.geo.FlynnHelmetGeo;
import ru.marov.gribs.client.model.geo.HoplingGeo;
import ru.marov.gribs.client.model.geo.MinerGribGeo;
import ru.marov.gribs.client.model.geo.PufferGeo;
import ru.marov.gribs.client.model.geo.RussulaGeo;
import ru.marov.gribs.client.renderer.CordycepsZombieRenderer;
import ru.marov.gribs.client.renderer.FlynnFoxRenderer;
import ru.marov.gribs.client.renderer.GribRenderer;
import ru.marov.gribs.client.renderer.RussulaRenderer;
import ru.marov.gribs.entity.AgaricGuard;
import ru.marov.gribs.entity.DeathCap;
import ru.marov.gribs.entity.Hopling;
import ru.marov.gribs.entity.MinerGrib;
import ru.marov.gribs.entity.Puffer;
import ru.marov.gribs.item.RussulaItem;
import ru.marov.gribs.registry.ModEntities;
import ru.marov.gribs.registry.ModItems;

@EventBusSubscriber(modid = MarovGribs.MODID, value = Dist.CLIENT)
public final class MarovGribsClient {
    private static final int TOOLTIP_WIDTH = 38;
    private static final ResourceLocation AGARIC_GUARD = GribRenderer.texture("agaric_guard");
    private static final ResourceLocation AGARIC_GUARD_SMILEY = GribRenderer.texture("agaric_guard_smiley");

    private MarovGribsClient() {
    }

    @SubscribeEvent
    static void onLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(HoplingModel.LAYER, HoplingGeo::create);
        event.registerLayerDefinition(PufferModel.LAYER, PufferGeo::create);
        event.registerLayerDefinition(AgaricGuardModel.LAYER, AgaricGuardGeo::create);
        event.registerLayerDefinition(RussulaModel.LAYER, RussulaGeo::create);
        event.registerLayerDefinition(MinerGribModel.LAYER, MinerGribGeo::create);
        event.registerLayerDefinition(DeathCapModel.LAYER, DeathCapGeo::create);
        event.registerLayerDefinition(ParasolGribModel.LAYER, ParasolGribGeo::create);
        event.registerLayerDefinition(ChanterelleGribModel.LAYER, ChanterelleGribGeo::create);
        event.registerLayerDefinition(FlynnFoxRenderer.HELMET, FlynnHelmetGeo::create);
        event.registerLayerDefinition(CordycepsZombieRenderer.GROWTH, CordycepsGeo::create);
    }

    @SubscribeEvent
    static void onRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.HOPLING.get(),
                ctx -> new GribRenderer<Hopling, HoplingModel>(ctx, new HoplingModel(ctx.bakeLayer(HoplingModel.LAYER)), 0.3F, "hopling"));
        event.registerEntityRenderer(ModEntities.PUFFER.get(),
                ctx -> new GribRenderer<Puffer, PufferModel>(ctx, new PufferModel(ctx.bakeLayer(PufferModel.LAYER)), 0.45F, "puffer"));
        event.registerEntityRenderer(ModEntities.AGARIC_GUARD.get(),
                ctx -> new GribRenderer<AgaricGuard, AgaricGuardModel>(ctx, new AgaricGuardModel(ctx.bakeLayer(AgaricGuardModel.LAYER)),
                        0.55F, guard -> guard.isSmiley() ? AGARIC_GUARD_SMILEY : AGARIC_GUARD, 1.0F));
        event.registerEntityRenderer(ModEntities.RUSSULA.get(), RussulaRenderer::new);
        event.registerEntityRenderer(ModEntities.MINER_GRIB.get(),
                ctx -> new GribRenderer<MinerGrib, MinerGribModel>(ctx, new MinerGribModel(ctx.bakeLayer(MinerGribModel.LAYER)), 0.3F,
                        GribRenderer.texture("miner_grib"), MinerGrib.SCALE).withGlow("miner_grib"));
        event.registerEntityRenderer(ModEntities.DEATH_CAP.get(),
                ctx -> new GribRenderer<DeathCap, DeathCapModel>(ctx, new DeathCapModel(ctx.bakeLayer(DeathCapModel.LAYER)), 1.2F,
                        GribRenderer.texture("death_cap"), DeathCap.SCALE).withGlow("death_cap"));
        event.registerEntityRenderer(ModEntities.PARASOL_GRIB.get(),
                ctx -> new GribRenderer<ParasolGrib, ParasolGribModel>(ctx, new ParasolGribModel(ctx.bakeLayer(ParasolGribModel.LAYER)), 1.4F,
                        GribRenderer.texture("parasol_grib"), ParasolGrib.SCALE));
        event.registerEntityRenderer(ModEntities.CHANTERELLE_GRIB.get(),
                ctx -> new GribRenderer<ChanterelleGrib, ChanterelleGribModel>(ctx,
                        new ChanterelleGribModel(ctx.bakeLayer(ChanterelleGribModel.LAYER)), 0.3F, "chanterelle_grib"));
        event.registerEntityRenderer(ModEntities.CORDYCEPS_ZOMBIE.get(), CordycepsZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.FLYNN_FOX.get(), FlynnFoxRenderer::new);
        event.registerEntityRenderer(ModEntities.SPORE_BOMB.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntities.SPORE_CLOUD.get(), SporeCloudRenderer::new);
    }

    /** A russula cap item is tinted with the colour of the russula it came from. */
    @SubscribeEvent
    static void onItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> tintIndex == 0
                ? FastColor.ARGB32.opaque(RussulaItem.colorOf(stack).getTextureDiffuseColor()) : -1, ModItems.RUSSULA.get());
    }

    /** Shows what a mushroom does: "<item key>.desc" from the lang file, wrapped to short gray lines. */
    @SubscribeEvent
    static void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!MarovGribs.MODID.equals(BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace())) {
            return;
        }
        String key = stack.getItem().getDescriptionId() + ".desc";
        if (!I18n.exists(key)) {
            return;
        }
        StringBuilder line = new StringBuilder();
        for (String word : I18n.get(key).split(" ")) {
            if (line.length() + word.length() > TOOLTIP_WIDTH && !line.isEmpty()) {
                event.getToolTip().add(Component.literal(line.toString()).withStyle(ChatFormatting.GRAY));
                line.setLength(0);
            }
            line.append(line.isEmpty() ? "" : " ").append(word);
        }
        event.getToolTip().add(Component.literal(line.toString()).withStyle(ChatFormatting.GRAY));
    }
}
