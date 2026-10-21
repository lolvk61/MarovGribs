// Сгенерировано art/gribs_art.py из art/marovgribs.blend. Руками не править: перезапишется при экспорте.
package ru.marov.gribs.client.model.geo;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class PufferGeo {
    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 64;

    private PufferGeo() {
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p_body = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-5.0F, -9.0F, -5.0F, 10.0F, 9.0F, 10.0F)
                .texOffs(18, 19).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 1.0F, 8.0F)
                .texOffs(0, 34).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 1.0F, 8.0F)
                .texOffs(40, 0).addBox(-6.0F, -8.0F, -4.0F, 1.0F, 7.0F, 8.0F)
                .texOffs(0, 19).addBox(5.0F, -8.0F, -4.0F, 1.0F, 7.0F, 8.0F)
                .texOffs(32, 34).addBox(-4.0F, -8.0F, 5.0F, 8.0F, 7.0F, 1.0F)
                .texOffs(0, 43).addBox(-4.0F, -8.0F, -6.0F, 8.0F, 7.0F, 1.0F),
                PartPose.offset(0.0F, 21.0F, 0.0F));
        PartDefinition p_mouth = p_body.addOrReplaceChild("mouth", CubeListBuilder.create()
                .texOffs(42, 43).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 1.0F, 3.0F),
                PartPose.offset(0.0F, -10.0F, 0.0F));
        PartDefinition p_right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create()
                .texOffs(18, 43).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 3.0F, 3.0F),
                PartPose.offset(-2.5F, 21.0F, 0.0F));
        PartDefinition p_left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create()
                .texOffs(30, 43).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 3.0F, 3.0F),
                PartPose.offset(2.5F, 21.0F, 0.0F));
        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }
}
