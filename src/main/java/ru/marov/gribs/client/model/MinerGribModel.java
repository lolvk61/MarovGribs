package ru.marov.gribs.client.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import ru.marov.gribs.MarovGribs;
import ru.marov.gribs.entity.MinerGrib;

public class MinerGribModel extends HierarchicalModel<MinerGrib> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(MarovGribs.id("miner_grib"), "main");
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart cap;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public MinerGribModel(ModelPart root) {
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
    public void setupAnim(MinerGrib entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        root().getAllParts().forEach(ModelPart::resetPose);
        float swing = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        rightLeg.xRot = swing;
        leftLeg.xRot = -swing;
        leftArm.xRot = swing * 0.7F;
        rightArm.xRot = -0.3F - swing * 0.5F;
        cap.yRot = netHeadYaw * Mth.DEG_TO_RAD * 0.5F;
        if (attackTime > 0.0F) {
            // Pickaxe swing: up over the shoulder, then down into the rock.
            float strike = Mth.sin(attackTime * Mth.PI);
            rightArm.xRot = -2.4F + strike * 2.2F;
            body.xRot = strike * 0.15F;
        }
        if (entity.isHiding()) {
            // Hunkers down under its cap and trembles.
            float shiver = Mth.sin(ageInTicks * 1.7F) * 0.04F;
            body.y += 1.0F;
            cap.xRot = 0.25F;
            body.zRot = shiver;
            rightArm.xRot = -2.6F;
            leftArm.xRot = -2.6F;
            rightArm.zRot = 0.4F;
            leftArm.zRot = -0.4F;
        }
    }
}
