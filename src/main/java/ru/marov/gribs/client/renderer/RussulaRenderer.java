package ru.marov.gribs.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import ru.marov.gribs.client.model.RussulaModel;
import ru.marov.gribs.entity.Russula;

/** Draws the russula, then its cap again tinted with its dye colour (the cap is grey in the texture). */
public class RussulaRenderer extends GribRenderer<Russula, RussulaModel> {
    private static final ResourceLocation TEXTURE = texture("russula");

    public RussulaRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new RussulaModel(ctx.bakeLayer(RussulaModel.LAYER)), 0.5F, TEXTURE, 1.0F);
        addLayer(new CapLayer(this));
    }

    static class CapLayer extends RenderLayer<Russula, RussulaModel> {
        CapLayer(RenderLayerParent<Russula, RussulaModel> parent) {
            super(parent);
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource buffer, int light, Russula russula, float limbSwing,
                           float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
            if (russula.isSheared() || russula.isInvisible()) {
                return;
            }
            RussulaModel model = getParentModel();
            int color = FastColor.ARGB32.opaque(russula.getColor().getTextureDiffuseColor());
            model.showCapOnly(true);
            model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE)), light,
                    LivingEntityRenderer.getOverlayCoords(russula, 0.0F), color);
            model.showCapOnly(false);
        }
    }
}
