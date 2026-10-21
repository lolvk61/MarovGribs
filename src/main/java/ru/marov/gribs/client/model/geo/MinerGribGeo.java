// Сгенерировано art/gribs_art.py из art/marovgribs.blend. Руками не править: перезапишется при экспорте.
package ru.marov.gribs.client.model.geo;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class MinerGribGeo {
    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 32;

    private MinerGribGeo() {
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p_body = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(36, 0).addBox(-3.0F, -7.0F, -2.5F, 6.0F, 7.0F, 5.0F)
                .texOffs(20, 12).addBox(-2.5F, -6.0F, 2.5F, 5.0F, 5.0F, 2.0F),
                PartPose.offset(0.0F, 20.0F, 0.0F));
        PartDefinition p_cap = p_body.addOrReplaceChild("cap", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4.5F, -3.0F, -4.5F, 9.0F, 3.0F, 9.0F)
                .texOffs(0, 12).addBox(-2.5F, -5.0F, -2.5F, 5.0F, 2.0F, 5.0F)
                .texOffs(20, 19).addBox(-1.0F, -5.0F, -3.5F, 2.0F, 2.0F, 1.0F),
                PartPose.offset(0.0F, -7.0F, 0.0F));
        PartDefinition p_right_arm = root.addOrReplaceChild("right_arm", CubeListBuilder.create()
                .texOffs(34, 12).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 5.0F, 2.0F)
                .texOffs(50, 12).addBox(-0.5F, 3.0F, -6.0F, 1.0F, 1.0F, 5.0F)
                .texOffs(16, 19).addBox(-0.5F, 1.0F, -7.0F, 1.0F, 5.0F, 1.0F),
                PartPose.offset(-4.0F, 15.0F, 0.0F));
        PartDefinition p_left_arm = root.addOrReplaceChild("left_arm", CubeListBuilder.create()
                .texOffs(42, 12).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 5.0F, 2.0F),
                PartPose.offset(4.0F, 15.0F, 0.0F));
        PartDefinition p_right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create()
                .texOffs(0, 19).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F),
                PartPose.offset(-1.5F, 20.0F, 0.0F));
        PartDefinition p_left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create()
                .texOffs(8, 19).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F),
                PartPose.offset(1.5F, 20.0F, 0.0F));
        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }
}
