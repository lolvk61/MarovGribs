package ru.marov.gribs.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Zombie;
import ru.marov.gribs.MarovGribs;

/** A vanilla zombie with orange cordyceps stalks sprouting from its head. */
public class CordycepsZombieRenderer extends ZombieRenderer {
    public static final ModelLayerLocation GROWTH = new ModelLayerLocation(MarovGribs.id("cordyceps_growth"), "main");
    private static final ResourceLocation GROWTH_TEXTURE = GribRenderer.texture("cordyceps_growth");

    public CordycepsZombieRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        addLayer(new GrowthLayer(this, ctx.bakeLayer(GROWTH)));
    }

    static class GrowthLayer extends RenderLayer<Zombie, ZombieModel<Zombie>> {
        private final ModelPart growth;

        GrowthLayer(RenderLayerParent<Zombie, ZombieModel<Zombie>> parent, ModelPart growth) {
            super(parent);
            this.growth = growth;
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource buffer, int light, Zombie zombie, float limbSwing,
                           float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
            if (zombie.isInvisible()) {
                return;
            }
            poseStack.pushPose();
            if (getParentModel().young) {
                // Same head scaling HumanoidModel applies to babies.
                poseStack.scale(0.75F, 0.75F, 0.75F);
                poseStack.translate(0.0F, 1.0F, 0.0F);
            }
            getParentModel().head.translateAndRotate(poseStack);
            poseStack.translate(0.0F, -1.5F, 0.0F); // the exported layer sits 24 px below its root
            growth.render(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(GROWTH_TEXTURE)), light,
                    LivingEntityRenderer.getOverlayCoords(zombie, 0.0F));
            poseStack.popPose();
        }
    }
}
