package ru.marov.gribs.client.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import ru.marov.gribs.MarovGribs;
import ru.marov.gribs.entity.DeathCap;

public class DeathCapModel extends HierarchicalModel<DeathCap> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(MarovGribs.id("death_cap"), "main");
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart cap;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart[] roots;

    public DeathCapModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.cap = body.getChild("cap");
        this.rightArm = root.getChild("right_arm");
        this.leftArm = root.getChild("left_arm");
        this.roots = new ModelPart[]{root.getChild("root_front"), root.getChild("root_right"),
                root.getChild("root_back"), root.getChild("root_left")};
    }

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void setupAnim(DeathCap entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        root().getAllParts().forEach(ModelPart::resetPose);
        float rage = entity.isEnraged() ? 1.0F : 0.0F;
        // Glides on its roots: they writhe, faster when it moves or rages.
        for (int i = 0; i < roots.length; i++) {
            float wave = Mth.sin(ageInTicks * (0.15F + 0.2F * rage) + i * 1.6F + limbSwing * 0.8F);
            if (i % 2 == 0) {
                roots[i].xRot = wave * 0.25F;
            } else {
                roots[i].zRot = wave * 0.25F;
            }
        }
        body.zRot = Mth.sin(ageInTicks * 0.05F) * 0.04F + Mth.sin(ageInTicks * 1.3F) * 0.02F * rage;
        cap.yRot = netHeadYaw * Mth.DEG_TO_RAD * 0.4F;
        cap.xRot = headPitch * Mth.DEG_TO_RAD * 0.3F + 0.1F * rage;
        float idle = Mth.sin(ageInTicks * 0.08F) * 0.08F;
        rightArm.zRot = 0.15F + idle;
        leftArm.zRot = -0.15F - idle;
        rightArm.xRot = Mth.cos(limbSwing * 0.6F) * 0.4F * limbSwingAmount;
        leftArm.xRot = -rightArm.xRot;
        if (attackTime > 0.0F) {
            // Both tendril arms come down together.
            float slam = Mth.sin(attackTime * Mth.PI);
            rightArm.xRot = -2.8F + slam * 2.6F;
            leftArm.xRot = rightArm.xRot;
            body.xRot = slam * 0.2F;
        }
    }
}
