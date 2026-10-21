package ru.marov.gribs.client.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import ru.marov.gribs.MarovGribs;
import ru.marov.gribs.entity.Russula;

/**
 * The cap is drawn in a separate, tinted pass (RussulaCapLayer), so the base pass hides it and the
 * cap pass hides everything else.
 */
public class RussulaModel extends HierarchicalModel<Russula> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(MarovGribs.id("russula"), "main");
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart cap;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftFrontLeg;
    private final ModelPart rightHindLeg;
    private final ModelPart leftHindLeg;
    private float eat;

    public RussulaModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.cap = body.getChild("cap");
        this.rightFrontLeg = root.getChild("right_front_leg");
        this.leftFrontLeg = root.getChild("left_front_leg");
        this.rightHindLeg = root.getChild("right_hind_leg");
        this.leftHindLeg = root.getChild("left_hind_leg");
    }

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void prepareMobModel(Russula entity, float limbSwing, float limbSwingAmount, float partialTick) {
        eat = entity.getEatProgress(partialTick);
    }

    @Override
    public void setupAnim(Russula entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        root().getAllParts().forEach(ModelPart::resetPose);
        float swing = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        rightFrontLeg.xRot = swing;
        leftHindLeg.xRot = swing;
        leftFrontLeg.xRot = -swing;
        rightHindLeg.xRot = -swing;
        // Bends forward from the base to nibble grass, otherwise sways a little.
        body.xRot = eat * 0.55F;
        body.zRot = Mth.sin(ageInTicks * 0.06F) * 0.03F + Mth.sin(limbSwing * 0.66F) * 0.06F * limbSwingAmount;
        body.yRot = netHeadYaw * Mth.DEG_TO_RAD * 0.3F;
        showCapOnly(false);
    }

    /** Base pass: everything but the cap. Cap pass: only the cap (body cubes skipped, its children kept). */
    public void showCapOnly(boolean capOnly) {
        cap.visible = capOnly;
        body.skipDraw = capOnly;
        rightFrontLeg.visible = !capOnly;
        leftFrontLeg.visible = !capOnly;
        rightHindLeg.visible = !capOnly;
        leftHindLeg.visible = !capOnly;
    }
}
