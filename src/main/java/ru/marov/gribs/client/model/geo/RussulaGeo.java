// Сгенерировано art/gribs_art.py из art/marovgribs.blend. Руками не править: перезапишется при экспорте.
package ru.marov.gribs.client.model.geo;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class RussulaGeo {
    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 64;

    private RussulaGeo() {
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p_body = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 15).addBox(-3.5F, -8.0F, -3.5F, 7.0F, 8.0F, 7.0F),
                PartPose.offset(0.0F, 21.0F, 0.0F));
        PartDefinition p_cap = p_body.addOrReplaceChild("cap", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-6.0F, -3.0F, -6.0F, 12.0F, 3.0F, 12.0F)
                .texOffs(0, 30).addBox(-5.0F, -4.0F, -5.0F, 10.0F, 1.0F, 10.0F),
                PartPose.offset(0.0F, -8.0F, 0.0F));
        PartDefinition p_right_front_leg = root.addOrReplaceChild("right_front_leg", CubeListBuilder.create()
                .texOffs(40, 30).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(-2.0F, 21.0F, -2.0F));
        PartDefinition p_left_front_leg = root.addOrReplaceChild("left_front_leg", CubeListBuilder.create()
                .texOffs(48, 30).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(2.0F, 21.0F, -2.0F));
        PartDefinition p_right_hind_leg = root.addOrReplaceChild("right_hind_leg", CubeListBuilder.create()
                .texOffs(56, 30).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(-2.0F, 21.0F, 2.0F));
        PartDefinition p_left_hind_leg = root.addOrReplaceChild("left_hind_leg", CubeListBuilder.create()
                .texOffs(0, 41).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F),
                PartPose.offset(2.0F, 21.0F, 2.0F));
        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }
}
