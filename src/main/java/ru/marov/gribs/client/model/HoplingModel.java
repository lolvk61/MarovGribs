package ru.marov.gribs.client.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import ru.marov.gribs.MarovGribs;
import ru.marov.gribs.entity.Hopling;

public class HoplingModel extends HierarchicalModel<Hopling> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(MarovGribs.id("hopling"), "main");
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart cap;

    public HoplingModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.cap = body.getChild("cap");
    }

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void setupAnim(Hopling entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        root().getAllParts().forEach(ModelPart::resetPose);
        // Walking is a string of little hops: bounce up, squash on landing.
        float hop = Math.abs(Mth.sin(limbSwing * 0.8F)) * Math.min(limbSwingAmount * 2.0F, 1.0F);
        body.y -= hop * 3.0F;
        body.yScale = 1.0F + hop * 0.12F;
        body.xScale = body.zScale = 1.0F - hop * 0.06F;
        if (!entity.onGround()) {
            body.yScale = 1.15F;
            body.xScale = body.zScale = 0.92F;
        }
        cap.zRot = Mth.sin(ageInTicks * 0.09F) * 0.06F;
        cap.xRot = Mth.cos(ageInTicks * 0.07F) * 0.04F + headPitch * Mth.DEG_TO_RAD * 0.3F;
        body.yRot = netHeadYaw * Mth.DEG_TO_RAD * 0.4F;
    }
}
