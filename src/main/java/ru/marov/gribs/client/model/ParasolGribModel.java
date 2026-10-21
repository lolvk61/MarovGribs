package ru.marov.gribs.client.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import ru.marov.gribs.MarovGribs;
import ru.marov.gribs.entity.ParasolGrib;

public class ParasolGribModel extends HierarchicalModel<ParasolGrib> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(MarovGribs.id("parasol_grib"), "main");
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart cap;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public ParasolGribModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.cap = body.getChild("cap");
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
    }

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void setupAnim(ParasolGrib entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        root().getAllParts().forEach(ModelPart::resetPose);
        // Slow, heavy steps; the tall stem leans into each one and the cap sways behind.
        float swing = Mth.cos(limbSwing * 0.4F) * 0.9F * limbSwingAmount;
        rightLeg.xRot = swing;
        leftLeg.xRot = -swing;
        body.zRot = Mth.sin(limbSwing * 0.2F) * 0.06F * limbSwingAmount + Mth.sin(ageInTicks * 0.03F) * 0.015F;
        cap.zRot = Mth.sin(ageInTicks * 0.05F) * 0.03F - body.zRot * 0.5F;
        cap.xRot = Mth.cos(ageInTicks * 0.04F) * 0.02F;
        cap.yRot = netHeadYaw * Mth.DEG_TO_RAD * 0.2F;
    }
}
