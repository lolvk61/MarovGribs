package ru.marov.gribs.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.FoxModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.FoxRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Fox;
import ru.marov.gribs.MarovGribs;

/** A regular fox with a green marine helmet and an orange visor on its head. */
public class FlynnFoxRenderer extends FoxRenderer {
    public static final ModelLayerLocation HELMET = new ModelLayerLocation(MarovGribs.id("flynn_helmet"), "main");
    private static final ResourceLocation HELMET_TEXTURE = GribRenderer.texture("flynn_helmet");

    public FlynnFoxRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        addLayer(new HelmetLayer(this, ctx.bakeLayer(HELMET)));
    }

    static class HelmetLayer extends RenderLayer<Fox, FoxModel<Fox>> {
        private final ModelPart helmet;

        HelmetLayer(RenderLayerParent<Fox, FoxModel<Fox>> parent, ModelPart helmet) {
            super(parent);
            this.helmet = helmet;
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource buffer, int light, Fox fox, float limbSwing,
                           float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
            if (fox.isInvisible() || fox.isBaby()) {
                return;
            }
            poseStack.pushPose();
            getParentModel().head.translateAndRotate(poseStack);
            poseStack.translate(0.0F, -1.5F, 0.0F); // the exported layer sits 24 px below its root
            helmet.render(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(HELMET_TEXTURE)), light,
                    LivingEntityRenderer.getOverlayCoords(fox, 0.0F));
            poseStack.popPose();
        }
    }
}
