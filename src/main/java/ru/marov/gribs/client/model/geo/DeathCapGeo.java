// Сгенерировано art/gribs_art.py из art/marovgribs.blend. Руками не править: перезапишется при экспорте.
package ru.marov.gribs.client.model.geo;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class DeathCapGeo {
    public static final int TEXTURE_WIDTH = 128;
    public static final int TEXTURE_HEIGHT = 64;

    private DeathCapGeo() {
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p_volva = root.addOrReplaceChild("volva", CubeListBuilder.create()
                .texOffs(64, 26).addBox(-6.0F, -4.0F, -6.0F, 12.0F, 4.0F, 12.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_body = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(80, 0).addBox(-4.0F, -18.0F, -4.0F, 8.0F, 18.0F, 8.0F)
                .texOffs(0, 45).addBox(-5.0F, -8.0F, -5.0F, 10.0F, 2.0F, 10.0F),
                PartPose.offset(0.0F, 20.0F, 0.0F));
        PartDefinition p_cap = p_body.addOrReplaceChild("cap", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-10.0F, -6.0F, -10.0F, 20.0F, 6.0F, 20.0F)
                .texOffs(0, 26).addBox(-8.0F, -9.0F, -8.0F, 16.0F, 3.0F, 16.0F)
                .texOffs(40, 45).addBox(-5.0F, -11.0F, -5.0F, 10.0F, 2.0F, 10.0F),
                PartPose.offset(0.0F, -18.0F, 0.0F));
        PartDefinition p_right_arm = root.addOrReplaceChild("right_arm", CubeListBuilder.create()
                .texOffs(112, 26).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 13.0F, 2.0F),
                PartPose.offset(-5.0F, 7.0F, 0.0F));
        PartDefinition p_left_arm = root.addOrReplaceChild("left_arm", CubeListBuilder.create()
                .texOffs(120, 26).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 13.0F, 2.0F),
                PartPose.offset(5.0F, 7.0F, 0.0F));
        PartDefinition p_root_front = root.addOrReplaceChild("root_front", CubeListBuilder.create()
                .texOffs(80, 45).addBox(-1.0F, -1.0F, -5.0F, 2.0F, 2.0F, 5.0F),
                PartPose.offset(0.0F, 23.0F, -6.0F));
        PartDefinition p_root_back = root.addOrReplaceChild("root_back", CubeListBuilder.create()
                .texOffs(94, 45).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 5.0F),
                PartPose.offset(0.0F, 23.0F, 6.0F));
        PartDefinition p_root_right = root.addOrReplaceChild("root_right", CubeListBuilder.create()
                .texOffs(108, 45).addBox(-5.0F, -1.0F, -1.0F, 5.0F, 2.0F, 2.0F),
                PartPose.offset(-6.0F, 23.0F, 0.0F));
        PartDefinition p_root_left = root.addOrReplaceChild("root_left", CubeListBuilder.create()
                .texOffs(0, 57).addBox(0.0F, -1.0F, -1.0F, 5.0F, 2.0F, 2.0F),
                PartPose.offset(6.0F, 23.0F, 0.0F));
        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }
}
