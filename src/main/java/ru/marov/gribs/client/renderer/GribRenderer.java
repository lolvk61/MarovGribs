package ru.marov.gribs.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.Function;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import ru.marov.gribs.MarovGribs;

/** One renderer for the mushroom mobs: texture by name (or picked per entity), size, babies at half size. */
public class GribRenderer<T extends Mob, M extends EntityModel<T>> extends MobRenderer<T, M> {
    private final Function<T, ResourceLocation> texture;
    private final float size;

    public GribRenderer(EntityRendererProvider.Context ctx, M model, float shadow, String name) {
        this(ctx, model, shadow, texture(name), 1.0F);
    }

    public GribRenderer(EntityRendererProvider.Context ctx, M model, float shadow, ResourceLocation texture, float size) {
        this(ctx, model, shadow, entity -> texture, size);
    }

    public GribRenderer(EntityRendererProvider.Context ctx, M model, float shadow, Function<T, ResourceLocation> texture, float size) {
        super(ctx, model, shadow);
        this.texture = texture;
        this.size = size;
    }

    public static ResourceLocation texture(String name) {
        return MarovGribs.id("textures/entity/" + name + ".png");
    }

    /** Adds a layer that glows in the dark from {@code textures/entity/<name>_glow.png}. */
    public GribRenderer<T, M> withGlow(String name) {
        addLayer(new GlowLayer<>(this, texture(name + "_glow")));
        return this;
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return texture.apply(entity);
    }

    @Override
    protected void scale(T entity, PoseStack poseStack, float partialTick) {
        float s = size * (entity.isBaby() ? 0.5F : 1.0F);
        if (s != 1.0F) {
            poseStack.scale(s, s, s);
        }
    }

    static class GlowLayer<T extends Mob, M extends EntityModel<T>> extends EyesLayer<T, M> {
        private final RenderType renderType;

        GlowLayer(RenderLayerParent<T, M> parent, ResourceLocation texture) {
            super(parent);
            this.renderType = RenderType.eyes(texture);
        }

        @Override
        public RenderType renderType() {
            return renderType;
        }
    }
}
