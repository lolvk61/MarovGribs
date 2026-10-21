package ru.marov.gribs.client.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import ru.marov.gribs.MarovGribs;
import ru.marov.gribs.entity.Puffer;

public class PufferModel extends HierarchicalModel<Puffer> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(MarovGribs.id("puffer"), "main");
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart mouth;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;
    private float swell;

    public PufferModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.mouth = body.getChild("mouth");
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
    }

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void prepareMobModel(Puffer entity, float limbSwing, float limbSwingAmount, float partialTick) {
        swell = entity.getSwelling(partialTick);
    }

    @Override
    public void setupAnim(Puffer entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        root().getAllParts().forEach(ModelPart::resetPose);
        float swing = Mth.cos(limbSwing * 0.9F) * 1.2F * limbSwingAmount;
        rightLeg.xRot = swing;
        leftLeg.xRot = -swing;
        // Waddle from side to side while walking, breathe while idle.
        body.zRot = Mth.sin(limbSwing * 0.45F) * 0.12F * limbSwingAmount;
        float breath = Mth.sin(ageInTicks * 0.1F) * 0.02F;
        float scale = 1.0F + breath + swell * 0.3F;
        body.xScale = body.zScale = scale;
        body.yScale = 1.0F + breath * 0.5F + swell * 0.22F;
        mouth.yScale = 1.0F + swell * 2.0F;
        body.yRot = netHeadYaw * Mth.DEG_TO_RAD * 0.25F;
    }
}
