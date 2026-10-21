package ru.marov.gribs.client.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import ru.marov.gribs.MarovGribs;
import ru.marov.gribs.entity.ChanterelleGrib;

public class ChanterelleGribModel extends HierarchicalModel<ChanterelleGrib> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(MarovGribs.id("chanterelle_grib"), "main");
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart cap;
    private final ModelPart tail;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public ChanterelleGribModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.cap = body.getChild("cap");
        this.tail = body.getChild("tail");
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
    }

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void setupAnim(ChanterelleGrib entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        root().getAllParts().forEach(ModelPart::resetPose);
        float swing = Mth.cos(limbSwing * 1.2F) * 1.4F * limbSwingAmount;
        rightLeg.xRot = swing;
        leftLeg.xRot = -swing;
        // Quick little trot with a bob; the tail wags, wider and faster when it runs.
        body.y -= Math.abs(Mth.sin(limbSwing * 0.6F)) * 1.5F * limbSwingAmount;
        float wag = 0.25F + 0.5F * Math.min(limbSwingAmount * 2.0F, 1.0F);
        tail.yRot = Mth.sin(ageInTicks * (0.25F + 0.5F * limbSwingAmount)) * wag;
        tail.xRot += Mth.sin(ageInTicks * 0.1F) * 0.08F;
        cap.zRot = Mth.sin(ageInTicks * 0.08F) * 0.05F;
        body.yRot = netHeadYaw * Mth.DEG_TO_RAD * 0.4F;
    }
}
