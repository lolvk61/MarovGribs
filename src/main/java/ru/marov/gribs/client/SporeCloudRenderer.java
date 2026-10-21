package ru.marov.gribs.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import ru.marov.gribs.MarovGribs;
import ru.marov.gribs.entity.SporeCloud;

/**
 * Draws a spore cloud as a cluster of big camera-facing puffs. The layout matches
 * cloud_puffs() in art/gribs_art.py, so the Blender preview shows the same shape.
 */
public class SporeCloudRenderer extends EntityRenderer<SporeCloud> {
    private static final ResourceLocation TEXTURE = MarovGribs.id("textures/entity/spore_puff.png");
    private static final int VARIANTS = 4;
    private static final float GOLDEN_ANGLE = (float) (Math.PI * (3.0 - Math.sqrt(5.0)));
    private static final float MAX_ALPHA = 0.9F;

    public SporeCloudRenderer(EntityRendererProvider.Context context) {
        super(context);
        shadowRadius = 0.0F;
    }

    @Override
    public void render(SporeCloud cloud, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffers, int packedLight) {
        float age = cloud.tickCount + partialTick;
        float alpha = MAX_ALPHA * Mth.clamp(age / 2.0F, 0.0F, 1.0F) * (1.0F - cloud.getFade(partialTick));
        if (alpha <= 0.01F) {
            return;
        }
        float radius = cloud.getCurrentRadius(partialTick);
        int color = cloud.getColor();
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        int a = (int) (alpha * 255.0F);

        Quaternionf camera = entityRenderDispatcher.cameraOrientation();
        Vector3f right = camera.transform(new Vector3f(1.0F, 0.0F, 0.0F));
        Vector3f up = camera.transform(new Vector3f(0.0F, 1.0F, 0.0F));
        VertexConsumer consumer = buffers.getBuffer(RenderType.entityTranslucent(TEXTURE));
        PoseStack.Pose pose = poseStack.last();

        // Seeded by entity id: every cloud has its own shape, stable from frame to frame.
        RandomSource random = RandomSource.create(cloud.getId() * 3121L + 7L);
        int count = Mth.clamp(Math.round(4.0F + cloud.getRadius() * 2.5F), 7, 14);
        for (int i = 0; i < count; i++) {
            float dy = 1.0F - 2.0F * (i + 0.5F) / count;
            float ring = Mth.sqrt(1.0F - dy * dy);
            float theta = i * GOLDEN_ANGLE;
            float dist = radius * (0.25F + 0.45F * random.nextFloat());
            float size = radius * (0.7F + 0.35F * random.nextFloat());
            float spin = (random.nextFloat() - 0.5F) * 0.5F;
            int variant = random.nextInt(VARIANTS);
            float phase = random.nextFloat() * Mth.TWO_PI;

            float x = Mth.cos(theta) * ring * dist;
            float z = Mth.sin(theta) * ring * dist;
            float y = radius * 0.5F + dy * radius * 0.3F + Mth.sin(age * 0.08F + phase) * radius * 0.03F;
            float wobble = spin + Mth.sin(age * 0.05F + phase) * 0.08F;
            puff(consumer, pose, x, y, z, right, up, size * 0.5F, wobble, variant, r, g, b, a, packedLight);
        }
        super.render(cloud, entityYaw, partialTick, poseStack, buffers, packedLight);
    }

    private static void puff(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z, Vector3f right, Vector3f up,
                             float half, float spin, int variant, int r, int g, int b, int a, int light) {
        float cos = Mth.cos(spin) * half;
        float sin = Mth.sin(spin) * half;
        float rx = right.x * cos + up.x * sin;
        float ry = right.y * cos + up.y * sin;
        float rz = right.z * cos + up.z * sin;
        float ux = up.x * cos - right.x * sin;
        float uy = up.y * cos - right.y * sin;
        float uz = up.z * cos - right.z * sin;
        float u0 = variant / (float) VARIANTS;
        float u1 = (variant + 1) / (float) VARIANTS;
        vertex(consumer, pose, x - rx - ux, y - ry - uy, z - rz - uz, u0, 1.0F, r, g, b, a, light);
        vertex(consumer, pose, x + rx - ux, y + ry - uy, z + rz - uz, u1, 1.0F, r, g, b, a, light);
        vertex(consumer, pose, x + rx + ux, y + ry + uy, z + rz + uz, u1, 0.0F, r, g, b, a, light);
        vertex(consumer, pose, x - rx + ux, y - ry + uy, z - rz + uz, u0, 0.0F, r, g, b, a, light);
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z, float u, float v,
                               int r, int g, int b, int a, int light) {
        consumer.addVertex(pose, x, y, z)
                .setColor(r, g, b, a)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(SporeCloud cloud) {
        return TEXTURE;
    }
}
