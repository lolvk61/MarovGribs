package ru.marov.gribs.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import ru.marov.gribs.MarovGribs;
import ru.marov.gribs.client.model.AgaricGuardModel;
import ru.marov.gribs.client.model.HoplingModel;
import ru.marov.gribs.client.model.PufferModel;
import ru.marov.gribs.client.model.geo.AgaricGuardGeo;
import ru.marov.gribs.client.model.geo.HoplingGeo;
import ru.marov.gribs.client.model.geo.PufferGeo;
import ru.marov.gribs.entity.AgaricGuard;
import ru.marov.gribs.entity.Hopling;
import ru.marov.gribs.entity.Puffer;
import ru.marov.gribs.registry.ModEntities;

@EventBusSubscriber(modid = MarovGribs.MODID, value = Dist.CLIENT)
public final class MarovGribsClient {
    private static final int TOOLTIP_WIDTH = 38;

    private MarovGribsClient() {
    }

    @SubscribeEvent
    static void onLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(HoplingModel.LAYER, HoplingGeo::create);
        event.registerLayerDefinition(PufferModel.LAYER, PufferGeo::create);
        event.registerLayerDefinition(AgaricGuardModel.LAYER, AgaricGuardGeo::create);
    }

    @SubscribeEvent
    static void onRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.HOPLING.get(),
                ctx -> new GribRenderer<Hopling, HoplingModel>(ctx, new HoplingModel(ctx.bakeLayer(HoplingModel.LAYER)), 0.3F, "hopling"));
        event.registerEntityRenderer(ModEntities.PUFFER.get(),
                ctx -> new GribRenderer<Puffer, PufferModel>(ctx, new PufferModel(ctx.bakeLayer(PufferModel.LAYER)), 0.45F, "puffer"));
        event.registerEntityRenderer(ModEntities.AGARIC_GUARD.get(),
                ctx -> new GribRenderer<AgaricGuard, AgaricGuardModel>(ctx, new AgaricGuardModel(ctx.bakeLayer(AgaricGuardModel.LAYER)), 0.55F, "agaric_guard"));
        event.registerEntityRenderer(ModEntities.SPORE_BOMB.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntities.SPORE_CLOUD.get(), SporeCloudRenderer::new);
    }

    /** Shows what a mushroom does: "<item key>.desc" from the lang file, wrapped to short gray lines. */
    @SubscribeEvent
    static void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!MarovGribs.MODID.equals(BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace())) {
            return;
        }
        String key = stack.getDescriptionId() + ".desc";
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

    /** One renderer for every mushroom mob: texture by name, babies at half size. */
    static class GribRenderer<T extends Mob, M extends EntityModel<T>> extends MobRenderer<T, M> {
        private final ResourceLocation texture;

        GribRenderer(EntityRendererProvider.Context ctx, M model, float shadow, String name) {
            super(ctx, model, shadow);
            this.texture = MarovGribs.id("textures/entity/" + name + ".png");
        }

        @Override
        public ResourceLocation getTextureLocation(T entity) {
            return texture;
        }

        @Override
        protected void scale(T entity, PoseStack poseStack, float partialTick) {
            if (entity.isBaby()) {
                poseStack.scale(0.5F, 0.5F, 0.5F);
            }
        }
    }
}
