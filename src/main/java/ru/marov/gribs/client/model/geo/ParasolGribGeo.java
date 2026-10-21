// Сгенерировано art/gribs_art.py из art/marovgribs.blend. Руками не править: перезапишется при экспорте.
package ru.marov.gribs.client.model.geo;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class ParasolGribGeo {
    public static final int TEXTURE_WIDTH = 128;
    public static final int TEXTURE_HEIGHT = 64;

    private ParasolGribGeo() {
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p_body = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-2.0F, -28.0F, -2.0F, 4.0F, 28.0F, 4.0F)
                .texOffs(104, 32).addBox(-3.0F, -22.0F, -3.0F, 6.0F, 1.0F, 6.0F),
                PartPose.offset(0.0F, 21.0F, 0.0F));
        PartDefinition p_cap = p_body.addOrReplaceChild("cap", CubeListBuilder.create()
                .texOffs(16, 0).addBox(-14.0F, -2.0F, -14.0F, 28.0F, 2.0F, 28.0F)
                .texOffs(0, 32).addBox(-10.0F, -4.0F, -10.0F, 20.0F, 2.0F, 20.0F)
                .texOffs(80, 32).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 2.0F, 6.0F),
                PartPose.offset(0.0F, -28.0F, 0.0F));
        PartDefinition p_right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create()
                .texOffs(0, 54).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 3.0F, 3.0F),
                PartPose.offset(-1.5F, 21.0F, 0.0F));
        PartDefinition p_left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create()
                .texOffs(12, 54).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 3.0F, 3.0F),
                PartPose.offset(1.5F, 21.0F, 0.0F));
        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }
}
