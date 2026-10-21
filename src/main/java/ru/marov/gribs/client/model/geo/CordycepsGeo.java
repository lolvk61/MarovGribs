// Сгенерировано art/gribs_art.py из art/marovgribs.blend. Руками не править: перезапишется при экспорте.
package ru.marov.gribs.client.model.geo;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class CordycepsGeo {
    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 32;

    private CordycepsGeo() {
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p_growth = root.addOrReplaceChild("growth", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4.5F, -8.5F, -4.5F, 9.0F, 1.0F, 9.0F)
                .texOffs(40, 0).addBox(-3.0F, -12.0F, -2.0F, 1.0F, 4.0F, 1.0F)
                .texOffs(44, 0).addBox(-3.5F, -14.0F, -2.5F, 2.0F, 2.0F, 2.0F)
                .texOffs(36, 0).addBox(1.0F, -13.0F, 0.0F, 1.0F, 5.0F, 1.0F)
                .texOffs(52, 0).addBox(0.5F, -15.0F, -0.5F, 2.0F, 2.0F, 2.0F)
                .texOffs(16, 10).addBox(-1.0F, -11.0F, 2.0F, 1.0F, 3.0F, 1.0F)
                .texOffs(0, 10).addBox(-1.5F, -13.0F, 1.5F, 2.0F, 2.0F, 2.0F)
                .texOffs(20, 10).addBox(3.0F, -11.0F, -3.0F, 1.0F, 3.0F, 1.0F)
                .texOffs(8, 10).addBox(2.5F, -13.0F, -3.5F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }
}
