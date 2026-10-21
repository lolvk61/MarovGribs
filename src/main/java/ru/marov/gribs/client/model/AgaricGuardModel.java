package ru.marov.gribs.client.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import ru.marov.gribs.MarovGribs;
import ru.marov.gribs.entity.AgaricGuard;

public class AgaricGuardModel extends HierarchicalModel<AgaricGuard> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(MarovGribs.id("agaric_guard"), "main");
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart cap;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public AgaricGuardModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.cap = body.getChild("cap");
        this.rightArm = root.getChild("right_arm");
        this.leftArm = root.getChild("left_arm");
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
    }

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void setupAnim(AgaricGuard entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        root().getAllParts().forEach(ModelPart::resetPose);
        float swing = Mth.cos(limbSwing * 0.6662F) * 1.3F * limbSwingAmount;
        rightLeg.xRot = swing;
        leftLeg.xRot = -swing;
        rightArm.xRot = -swing * 0.8F;
        leftArm.xRot = swing * 0.8F;
        rightArm.zRot = 0.08F + Mth.cos(ageInTicks * 0.09F) * 0.04F;
        leftArm.zRot = -rightArm.zRot;

        // The cap is the head: it turns to look, and tilts down like a frown when angry.
        cap.yRot = netHeadYaw * Mth.DEG_TO_RAD * 0.7F;
        cap.xRot = headPitch * Mth.DEG_TO_RAD * 0.5F;
        if (entity.isAggressive()) {
            cap.xRot += 0.18F;
            rightArm.xRot = -1.2F + Mth.cos(ageInTicks * 0.4F) * 0.15F;
            leftArm.xRot = -1.2F - Mth.cos(ageInTicks * 0.4F) * 0.15F;
        }
        if (attackTime > 0.0F) {
            // Overhead double-fisted slam.
            float slam = Mth.sin(attackTime * Mth.PI);
            rightArm.xRot = -2.6F + slam * 2.2F;
            leftArm.xRot = rightArm.xRot;
            body.xRot = slam * 0.25F;
        }
    }
}
