package ru.marov.gribs.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
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
import ru.marov.gribs.entity.CordycepsZombie;

/**
 * A vanilla zombie with cordyceps stalks sprouting from its face, head, body, arms and legs, in one
 * of {@link CordycepsZombie#GROWTH_VARIANTS} patterns. Each growth part (v&lt;n&gt;_&lt;limb&gt; in the
 * exported geometry) is drawn attached to its limb, so it moves with the zombie.
 */
public class CordycepsZombieRenderer extends ZombieRenderer {
    public static final ModelLayerLocation GROWTH = new ModelLayerLocation(MarovGribs.id("cordyceps_growth"), "main");
    private static final ResourceLocation GROWTH_TEXTURE = GribRenderer.texture("cordyceps_growth");
    private static final String[] LIMBS = {"head", "body", "right_arm", "left_arm", "right_leg", "left_leg"};

    public CordycepsZombieRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        addLayer(new GrowthLayer(this, ctx.bakeLayer(GROWTH)));
    }

    static class GrowthLayer extends RenderLayer<Zombie, ZombieModel<Zombie>> {
        /** For each variant: (limb name, growth part) pairs. */
        private final List<List<Growth>> variants = new ArrayList<>();

        GrowthLayer(RenderLayerParent<Zombie, ZombieModel<Zombie>> parent, ModelPart root) {
            super(parent);
            for (int v = 0; v < CordycepsZombie.GROWTH_VARIANTS; v++) {
                List<Growth> growths = new ArrayList<>();
                for (String limb : LIMBS) {
                    String name = "v" + v + "_" + limb;
                    if (root.hasChild(name)) {
                        growths.add(new Growth(limb, root.getChild(name)));
                    }
                }
                variants.add(growths);
            }
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource buffer, int light, Zombie zombie, float limbSwing,
                           float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
            if (zombie.isInvisible() || !(zombie instanceof CordycepsZombie infected)) {
                return;
            }
            ZombieModel<Zombie> model = getParentModel();
            Function<String, ModelPart> limbs = limb -> switch (limb) {
                case "head" -> model.head;
                case "body" -> model.body;
                case "right_arm" -> model.rightArm;
                case "left_arm" -> model.leftArm;
                case "right_leg" -> model.rightLeg;
                default -> model.leftLeg;
            };
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(GROWTH_TEXTURE));
            int overlay = LivingEntityRenderer.getOverlayCoords(zombie, 0.0F);
            for (Growth growth : variants.get(infected.getGrowthVariant())) {
                poseStack.pushPose();
                if (model.young) {
                    // Same scaling HumanoidModel applies to a baby's head and body.
                    if (growth.limb().equals("head")) {
                        poseStack.scale(0.75F, 0.75F, 0.75F);
                        poseStack.translate(0.0F, 1.0F, 0.0F);
                    } else {
                        poseStack.scale(0.5F, 0.5F, 0.5F);
                        poseStack.translate(0.0F, 1.5F, 0.0F);
                    }
                }
                limbs.apply(growth.limb()).translateAndRotate(poseStack);
                poseStack.translate(0.0F, -1.5F, 0.0F); // the exported growth parts sit 24 px below their root
                growth.part().render(poseStack, consumer, light, overlay);
                poseStack.popPose();
            }
        }

        private record Growth(String limb, ModelPart part) {
        }
    }
}
