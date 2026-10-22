// Сгенерировано art/gribs_art.py из art/marovgribs.blend. Руками не править: перезапишется при экспорте.
package ru.marov.gribs.client.model.geo;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class CordycepsGeo {
    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 128;

    private CordycepsGeo() {
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p_v0_head = root.addOrReplaceChild("v0_head", CubeListBuilder.create()
                .texOffs(24, 6).addBox(-4.0F, -9.0F, -3.0F, 3.0F, 1.0F, 3.0F)
                .texOffs(54, 63).addBox(-3.0F, -9.0F, -2.0F, 1.0F, 1.0F, 1.0F)
                .texOffs(24, 12).addBox(-3.5F, -11.0F, -2.5F, 2.0F, 2.0F, 2.0F)
                .texOffs(32, 12).addBox(-1.0F, -4.0F, -5.0F, 3.0F, 3.0F, 1.0F)
                .texOffs(8, 56).addBox(0.0F, -3.0F, -6.0F, 1.0F, 1.0F, 2.0F)
                .texOffs(40, 12).addBox(-0.5F, -3.5F, -8.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v0_body = root.addOrReplaceChild("v0_body", CubeListBuilder.create()
                .texOffs(48, 12).addBox(-2.0F, 9.0F, 2.0F, 3.0F, 3.0F, 1.0F)
                .texOffs(14, 56).addBox(-1.0F, 10.0F, 2.0F, 1.0F, 1.0F, 2.0F)
                .texOffs(56, 12).addBox(-1.5F, 9.5F, 4.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v0_right_arm = root.addOrReplaceChild("v0_right_arm", CubeListBuilder.create()
                .texOffs(58, 63).addBox(-1.0F, 5.0F, 2.0F, 1.0F, 1.0F, 1.0F)
                .texOffs(0, 16).addBox(-1.5F, 4.5F, 3.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v0_left_leg = root.addOrReplaceChild("v0_left_leg", CubeListBuilder.create()
                .texOffs(0, 0).addBox(2.0F, 4.0F, -1.0F, 1.0F, 3.0F, 3.0F)
                .texOffs(0, 66).addBox(2.0F, 5.0F, 0.0F, 1.0F, 1.0F, 1.0F)
                .texOffs(8, 16).addBox(3.0F, 4.5F, -0.5F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v1_head = root.addOrReplaceChild("v1_head", CubeListBuilder.create()
                .texOffs(16, 16).addBox(0.0F, -6.0F, 4.0F, 3.0F, 3.0F, 1.0F)
                .texOffs(20, 56).addBox(1.0F, -5.0F, 4.0F, 1.0F, 1.0F, 2.0F)
                .texOffs(24, 16).addBox(0.5F, -5.5F, 6.0F, 2.0F, 2.0F, 2.0F)
                .texOffs(32, 16).addBox(-4.0F, -6.0F, -5.0F, 3.0F, 3.0F, 1.0F)
                .texOffs(26, 56).addBox(-3.0F, -5.0F, -6.0F, 1.0F, 1.0F, 2.0F)
                .texOffs(40, 16).addBox(-3.5F, -5.5F, -8.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v1_left_arm = root.addOrReplaceChild("v1_left_arm", CubeListBuilder.create()
                .texOffs(4, 66).addBox(3.0F, 6.0F, -1.0F, 1.0F, 1.0F, 1.0F)
                .texOffs(48, 16).addBox(4.0F, 5.5F, -1.5F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v1_right_leg = root.addOrReplaceChild("v1_right_leg", CubeListBuilder.create()
                .texOffs(8, 0).addBox(-3.0F, 2.0F, -1.0F, 1.0F, 3.0F, 3.0F)
                .texOffs(8, 66).addBox(-3.0F, 3.0F, 0.0F, 1.0F, 1.0F, 1.0F)
                .texOffs(56, 16).addBox(-5.0F, 2.5F, -0.5F, 2.0F, 2.0F, 2.0F)
                .texOffs(0, 20).addBox(-2.0F, 1.0F, -3.0F, 3.0F, 3.0F, 1.0F)
                .texOffs(32, 56).addBox(-1.0F, 2.0F, -4.0F, 1.0F, 1.0F, 2.0F)
                .texOffs(8, 20).addBox(-1.5F, 1.5F, -6.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v1_left_leg = root.addOrReplaceChild("v1_left_leg", CubeListBuilder.create()
                .texOffs(16, 20).addBox(-2.0F, 1.0F, -3.0F, 3.0F, 3.0F, 1.0F)
                .texOffs(38, 56).addBox(-1.0F, 2.0F, -4.0F, 1.0F, 1.0F, 2.0F)
                .texOffs(24, 20).addBox(-1.5F, 1.5F, -6.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v2_head = root.addOrReplaceChild("v2_head", CubeListBuilder.create()
                .texOffs(32, 20).addBox(-4.0F, -6.0F, 4.0F, 3.0F, 3.0F, 1.0F)
                .texOffs(44, 56).addBox(-3.0F, -5.0F, 4.0F, 1.0F, 1.0F, 2.0F)
                .texOffs(40, 20).addBox(-3.5F, -5.5F, 6.0F, 2.0F, 2.0F, 2.0F)
                .texOffs(48, 20).addBox(-3.0F, -5.0F, 4.0F, 3.0F, 3.0F, 1.0F)
                .texOffs(12, 66).addBox(-2.0F, -4.0F, 4.0F, 1.0F, 1.0F, 1.0F)
                .texOffs(56, 20).addBox(-2.5F, -4.5F, 5.0F, 2.0F, 2.0F, 2.0F)
                .texOffs(0, 24).addBox(1.0F, -4.0F, -5.0F, 3.0F, 3.0F, 1.0F)
                .texOffs(50, 56).addBox(2.0F, -3.0F, -6.0F, 1.0F, 1.0F, 2.0F)
                .texOffs(8, 24).addBox(1.5F, -3.5F, -8.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v2_left_arm = root.addOrReplaceChild("v2_left_arm", CubeListBuilder.create()
                .texOffs(16, 0).addBox(3.0F, 5.0F, -2.0F, 1.0F, 3.0F, 3.0F)
                .texOffs(16, 66).addBox(3.0F, 6.0F, -1.0F, 1.0F, 1.0F, 1.0F)
                .texOffs(16, 24).addBox(4.0F, 5.5F, -1.5F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v2_right_leg = root.addOrReplaceChild("v2_right_leg", CubeListBuilder.create()
                .texOffs(24, 24).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 3.0F, 1.0F)
                .texOffs(20, 66).addBox(0.0F, 1.0F, -3.0F, 1.0F, 1.0F, 1.0F)
                .texOffs(32, 24).addBox(-0.5F, 0.5F, -5.0F, 2.0F, 2.0F, 2.0F)
                .texOffs(40, 24).addBox(-1.0F, 3.0F, 2.0F, 3.0F, 3.0F, 1.0F)
                .texOffs(24, 66).addBox(0.0F, 4.0F, 2.0F, 1.0F, 1.0F, 1.0F)
                .texOffs(48, 24).addBox(-0.5F, 3.5F, 3.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v3_head = root.addOrReplaceChild("v3_head", CubeListBuilder.create()
                .texOffs(24, 0).addBox(-5.0F, -3.0F, 1.0F, 1.0F, 3.0F, 3.0F)
                .texOffs(28, 66).addBox(-5.0F, -2.0F, 2.0F, 1.0F, 1.0F, 1.0F)
                .texOffs(56, 24).addBox(-7.0F, -2.5F, 1.5F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v3_body = root.addOrReplaceChild("v3_body", CubeListBuilder.create()
                .texOffs(0, 28).addBox(-2.0F, 2.0F, -3.0F, 3.0F, 3.0F, 1.0F)
                .texOffs(56, 56).addBox(-1.0F, 3.0F, -4.0F, 1.0F, 1.0F, 2.0F)
                .texOffs(8, 28).addBox(-1.5F, 2.5F, -6.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v3_right_arm = root.addOrReplaceChild("v3_right_arm", CubeListBuilder.create()
                .texOffs(32, 0).addBox(-4.0F, 5.0F, -1.0F, 1.0F, 3.0F, 3.0F)
                .texOffs(32, 66).addBox(-4.0F, 6.0F, 0.0F, 1.0F, 1.0F, 1.0F)
                .texOffs(16, 28).addBox(-6.0F, 5.5F, -0.5F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v3_left_arm = root.addOrReplaceChild("v3_left_arm", CubeListBuilder.create()
                .texOffs(36, 66).addBox(1.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F)
                .texOffs(24, 28).addBox(0.5F, -5.0F, -1.5F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v3_right_leg = root.addOrReplaceChild("v3_right_leg", CubeListBuilder.create()
                .texOffs(32, 28).addBox(-2.0F, 9.0F, -3.0F, 3.0F, 3.0F, 1.0F)
                .texOffs(40, 66).addBox(-1.0F, 10.0F, -3.0F, 1.0F, 1.0F, 1.0F)
                .texOffs(40, 28).addBox(-1.5F, 9.5F, -5.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v3_left_leg = root.addOrReplaceChild("v3_left_leg", CubeListBuilder.create()
                .texOffs(0, 60).addBox(-1.0F, 3.0F, -4.0F, 1.0F, 1.0F, 2.0F)
                .texOffs(48, 28).addBox(-1.5F, 2.5F, -6.0F, 2.0F, 2.0F, 2.0F)
                .texOffs(56, 28).addBox(-2.0F, 1.0F, 2.0F, 3.0F, 3.0F, 1.0F)
                .texOffs(6, 60).addBox(-1.0F, 2.0F, 2.0F, 1.0F, 1.0F, 2.0F)
                .texOffs(0, 32).addBox(-1.5F, 1.5F, 4.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v4_head = root.addOrReplaceChild("v4_head", CubeListBuilder.create()
                .texOffs(44, 66).addBox(-5.0F, -2.0F, -3.0F, 1.0F, 1.0F, 1.0F)
                .texOffs(8, 32).addBox(-7.0F, -2.5F, -3.5F, 2.0F, 2.0F, 2.0F)
                .texOffs(16, 32).addBox(1.0F, -7.0F, -5.0F, 3.0F, 3.0F, 1.0F)
                .texOffs(12, 60).addBox(2.0F, -6.0F, -6.0F, 1.0F, 1.0F, 2.0F)
                .texOffs(24, 32).addBox(1.5F, -6.5F, -8.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v4_body = root.addOrReplaceChild("v4_body", CubeListBuilder.create()
                .texOffs(32, 32).addBox(1.0F, 2.0F, 2.0F, 3.0F, 3.0F, 1.0F)
                .texOffs(48, 66).addBox(2.0F, 3.0F, 2.0F, 1.0F, 1.0F, 1.0F)
                .texOffs(40, 32).addBox(1.5F, 2.5F, 3.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v4_right_arm = root.addOrReplaceChild("v4_right_arm", CubeListBuilder.create()
                .texOffs(36, 6).addBox(-2.0F, -3.0F, -1.0F, 3.0F, 1.0F, 3.0F)
                .texOffs(52, 66).addBox(-1.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F)
                .texOffs(48, 32).addBox(-1.5F, -5.0F, -0.5F, 2.0F, 2.0F, 2.0F)
                .texOffs(6, 63).addBox(-2.0F, -4.0F, 0.0F, 1.0F, 2.0F, 1.0F)
                .texOffs(56, 32).addBox(-2.5F, -6.0F, -0.5F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v4_right_leg = root.addOrReplaceChild("v4_right_leg", CubeListBuilder.create()
                .texOffs(0, 36).addBox(-1.0F, 4.0F, 2.0F, 3.0F, 3.0F, 1.0F)
                .texOffs(18, 60).addBox(0.0F, 5.0F, 2.0F, 1.0F, 1.0F, 2.0F)
                .texOffs(8, 36).addBox(-0.5F, 4.5F, 4.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v5_head = root.addOrReplaceChild("v5_head", CubeListBuilder.create()
                .texOffs(16, 36).addBox(1.0F, -3.0F, -5.0F, 3.0F, 3.0F, 1.0F)
                .texOffs(24, 60).addBox(2.0F, -2.0F, -6.0F, 1.0F, 1.0F, 2.0F)
                .texOffs(24, 36).addBox(1.5F, -2.5F, -8.0F, 2.0F, 2.0F, 2.0F)
                .texOffs(56, 66).addBox(1.0F, -9.0F, 2.0F, 1.0F, 1.0F, 1.0F)
                .texOffs(32, 36).addBox(0.5F, -11.0F, 1.5F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v5_body = root.addOrReplaceChild("v5_body", CubeListBuilder.create()
                .texOffs(40, 36).addBox(0.0F, 3.0F, -3.0F, 3.0F, 3.0F, 1.0F)
                .texOffs(30, 60).addBox(1.0F, 4.0F, -4.0F, 1.0F, 1.0F, 2.0F)
                .texOffs(48, 36).addBox(0.5F, 3.5F, -6.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v5_right_arm = root.addOrReplaceChild("v5_right_arm", CubeListBuilder.create()
                .texOffs(18, 63).addBox(-5.0F, 5.0F, -1.0F, 2.0F, 1.0F, 1.0F)
                .texOffs(56, 36).addBox(-7.0F, 4.5F, -1.5F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v5_right_leg = root.addOrReplaceChild("v5_right_leg", CubeListBuilder.create()
                .texOffs(24, 63).addBox(-4.0F, 7.0F, -1.0F, 2.0F, 1.0F, 1.0F)
                .texOffs(0, 40).addBox(-6.0F, 6.5F, -1.5F, 2.0F, 2.0F, 2.0F)
                .texOffs(36, 60).addBox(-1.0F, 7.0F, -4.0F, 1.0F, 1.0F, 2.0F)
                .texOffs(8, 40).addBox(-1.5F, 6.5F, -6.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v6_head = root.addOrReplaceChild("v6_head", CubeListBuilder.create()
                .texOffs(48, 6).addBox(0.0F, -9.0F, -3.0F, 3.0F, 1.0F, 3.0F)
                .texOffs(60, 66).addBox(1.0F, -9.0F, -2.0F, 1.0F, 1.0F, 1.0F)
                .texOffs(16, 40).addBox(0.5F, -11.0F, -2.5F, 2.0F, 2.0F, 2.0F)
                .texOffs(0, 12).addBox(-1.0F, -9.0F, 0.0F, 3.0F, 1.0F, 3.0F)
                .texOffs(10, 63).addBox(0.0F, -10.0F, 1.0F, 1.0F, 2.0F, 1.0F)
                .texOffs(24, 40).addBox(-0.5F, -12.0F, 0.5F, 2.0F, 2.0F, 2.0F)
                .texOffs(32, 40).addBox(1.0F, -4.0F, -5.0F, 3.0F, 3.0F, 1.0F)
                .texOffs(0, 68).addBox(2.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F)
                .texOffs(40, 40).addBox(1.5F, -3.5F, -7.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v6_body = root.addOrReplaceChild("v6_body", CubeListBuilder.create()
                .texOffs(48, 40).addBox(0.0F, 9.0F, 2.0F, 3.0F, 3.0F, 1.0F)
                .texOffs(4, 68).addBox(1.0F, 10.0F, 2.0F, 1.0F, 1.0F, 1.0F)
                .texOffs(56, 40).addBox(0.5F, 9.5F, 3.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v6_right_arm = root.addOrReplaceChild("v6_right_arm", CubeListBuilder.create()
                .texOffs(40, 0).addBox(-4.0F, 1.0F, -1.0F, 1.0F, 3.0F, 3.0F)
                .texOffs(8, 68).addBox(-4.0F, 2.0F, 0.0F, 1.0F, 1.0F, 1.0F)
                .texOffs(0, 44).addBox(-6.0F, 1.5F, -0.5F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v6_left_arm = root.addOrReplaceChild("v6_left_arm", CubeListBuilder.create()
                .texOffs(48, 0).addBox(3.0F, 0.0F, -1.0F, 1.0F, 3.0F, 3.0F)
                .texOffs(30, 63).addBox(3.0F, 1.0F, 0.0F, 2.0F, 1.0F, 1.0F)
                .texOffs(8, 44).addBox(5.0F, 0.5F, -0.5F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v7_head = root.addOrReplaceChild("v7_head", CubeListBuilder.create()
                .texOffs(36, 63).addBox(4.0F, -4.0F, 0.0F, 2.0F, 1.0F, 1.0F)
                .texOffs(16, 44).addBox(6.0F, -4.5F, -0.5F, 2.0F, 2.0F, 2.0F)
                .texOffs(56, 0).addBox(-5.0F, -5.0F, 1.0F, 1.0F, 3.0F, 3.0F)
                .texOffs(12, 68).addBox(-5.0F, -4.0F, 2.0F, 1.0F, 1.0F, 1.0F)
                .texOffs(24, 44).addBox(-7.0F, -4.5F, 1.5F, 2.0F, 2.0F, 2.0F)
                .texOffs(32, 44).addBox(-1.0F, -7.0F, -5.0F, 3.0F, 3.0F, 1.0F)
                .texOffs(16, 68).addBox(0.0F, -6.0F, -5.0F, 1.0F, 1.0F, 1.0F)
                .texOffs(40, 44).addBox(-0.5F, -6.5F, -7.0F, 2.0F, 2.0F, 2.0F)
                .texOffs(20, 68).addBox(1.0F, -5.0F, -5.0F, 1.0F, 1.0F, 1.0F)
                .texOffs(48, 44).addBox(0.5F, -5.5F, -7.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v7_right_arm = root.addOrReplaceChild("v7_right_arm", CubeListBuilder.create()
                .texOffs(0, 6).addBox(-4.0F, 5.0F, -2.0F, 1.0F, 3.0F, 3.0F)
                .texOffs(42, 63).addBox(-5.0F, 6.0F, -1.0F, 2.0F, 1.0F, 1.0F)
                .texOffs(56, 44).addBox(-7.0F, 5.5F, -1.5F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v7_left_leg = root.addOrReplaceChild("v7_left_leg", CubeListBuilder.create()
                .texOffs(42, 60).addBox(0.0F, 5.0F, -4.0F, 1.0F, 1.0F, 2.0F)
                .texOffs(0, 48).addBox(-0.5F, 4.5F, -6.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v8_head = root.addOrReplaceChild("v8_head", CubeListBuilder.create()
                .texOffs(12, 12).addBox(-3.0F, -9.0F, 0.0F, 3.0F, 1.0F, 3.0F)
                .texOffs(14, 63).addBox(-2.0F, -10.0F, 1.0F, 1.0F, 2.0F, 1.0F)
                .texOffs(8, 48).addBox(-2.5F, -12.0F, 0.5F, 2.0F, 2.0F, 2.0F)
                .texOffs(16, 48).addBox(-3.0F, -6.0F, -5.0F, 3.0F, 3.0F, 1.0F)
                .texOffs(24, 68).addBox(-2.0F, -5.0F, -5.0F, 1.0F, 1.0F, 1.0F)
                .texOffs(24, 48).addBox(-2.5F, -5.5F, -7.0F, 2.0F, 2.0F, 2.0F)
                .texOffs(32, 48).addBox(-2.0F, -5.0F, 4.0F, 3.0F, 3.0F, 1.0F)
                .texOffs(28, 68).addBox(-1.0F, -4.0F, 4.0F, 1.0F, 1.0F, 1.0F)
                .texOffs(40, 48).addBox(-1.5F, -4.5F, 5.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v8_body = root.addOrReplaceChild("v8_body", CubeListBuilder.create()
                .texOffs(48, 48).addBox(1.0F, 1.0F, -3.0F, 3.0F, 3.0F, 1.0F)
                .texOffs(32, 68).addBox(2.0F, 2.0F, -3.0F, 1.0F, 1.0F, 1.0F)
                .texOffs(56, 48).addBox(1.5F, 1.5F, -5.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v8_left_arm = root.addOrReplaceChild("v8_left_arm", CubeListBuilder.create()
                .texOffs(8, 6).addBox(3.0F, 1.0F, -1.0F, 1.0F, 3.0F, 3.0F)
                .texOffs(36, 68).addBox(3.0F, 2.0F, 0.0F, 1.0F, 1.0F, 1.0F)
                .texOffs(0, 52).addBox(4.0F, 1.5F, -0.5F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v9_head = root.addOrReplaceChild("v9_head", CubeListBuilder.create()
                .texOffs(8, 52).addBox(-4.0F, -6.0F, 4.0F, 3.0F, 3.0F, 1.0F)
                .texOffs(48, 60).addBox(-3.0F, -5.0F, 4.0F, 1.0F, 1.0F, 2.0F)
                .texOffs(16, 52).addBox(-3.5F, -5.5F, 6.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v9_body = root.addOrReplaceChild("v9_body", CubeListBuilder.create()
                .texOffs(54, 60).addBox(2.0F, 5.0F, -4.0F, 1.0F, 1.0F, 2.0F)
                .texOffs(24, 52).addBox(1.5F, 4.5F, -6.0F, 2.0F, 2.0F, 2.0F)
                .texOffs(32, 52).addBox(1.0F, 9.0F, 2.0F, 3.0F, 3.0F, 1.0F)
                .texOffs(0, 63).addBox(2.0F, 10.0F, 2.0F, 1.0F, 1.0F, 2.0F)
                .texOffs(40, 52).addBox(1.5F, 9.5F, 4.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v9_right_arm = root.addOrReplaceChild("v9_right_arm", CubeListBuilder.create()
                .texOffs(48, 52).addBox(-3.0F, 4.0F, 2.0F, 3.0F, 3.0F, 1.0F)
                .texOffs(40, 68).addBox(-2.0F, 5.0F, 2.0F, 1.0F, 1.0F, 1.0F)
                .texOffs(56, 52).addBox(-2.5F, 4.5F, 3.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_v9_left_arm = root.addOrReplaceChild("v9_left_arm", CubeListBuilder.create()
                .texOffs(16, 6).addBox(3.0F, 7.0F, -1.0F, 1.0F, 3.0F, 3.0F)
                .texOffs(48, 63).addBox(3.0F, 8.0F, 0.0F, 2.0F, 1.0F, 1.0F)
                .texOffs(0, 56).addBox(5.0F, 7.5F, -0.5F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }
}
