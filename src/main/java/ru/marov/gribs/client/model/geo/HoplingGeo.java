// Сгенерировано art/gribs_art.py из art/marovgribs.blend. Руками не править: перезапишется при экспорте.
package ru.marov.gribs.client.model.geo;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class HoplingGeo {
    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 32;

    private HoplingGeo() {
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p_body = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(32, 0).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F)
                .texOffs(24, 12).addBox(-2.5F, -2.0F, -2.5F, 5.0F, 2.0F, 5.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition p_cap = p_body.addOrReplaceChild("cap", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 4.0F, 8.0F)
                .texOffs(0, 12).addBox(-3.0F, -5.0F, -3.0F, 6.0F, 1.0F, 6.0F),
                PartPose.offset(0.0F, -6.0F, 0.0F));
        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }
}
