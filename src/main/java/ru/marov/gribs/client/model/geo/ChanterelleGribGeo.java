// Сгенерировано art/gribs_art.py из art/marovgribs.blend. Руками не править: перезапишется при экспорте.
package ru.marov.gribs.client.model.geo;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class ChanterelleGribGeo {
    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 32;

    private ChanterelleGribGeo() {
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p_body = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 10).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F)
                .texOffs(16, 10).addBox(-3.0F, -8.0F, -3.0F, 6.0F, 2.0F, 6.0F),
                PartPose.offset(0.0F, 22.0F, 0.0F));
        PartDefinition p_cap = p_body.addOrReplaceChild("cap", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 2.0F, 8.0F),
                PartPose.offset(0.0F, -8.0F, 0.0F));
        PartDefinition p_tail = p_body.addOrReplaceChild("tail", CubeListBuilder.create()
                .texOffs(32, 0).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 7.0F)
                .texOffs(40, 10).addBox(-1.5F, -1.5F, 7.0F, 3.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, -2.0F, 2.0F, 0.436F, 0.0F, 0.0F));
        PartDefinition p_right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create()
                .texOffs(52, 10).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(-1.0F, 22.0F, 0.0F));
        PartDefinition p_left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create()
                .texOffs(0, 20).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(1.0F, 22.0F, 0.0F));
        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }
}
