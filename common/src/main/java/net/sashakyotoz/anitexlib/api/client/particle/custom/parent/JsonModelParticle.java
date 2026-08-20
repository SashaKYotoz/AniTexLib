package net.sashakyotoz.anitexlib.api.client.particle.custom.parent;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public abstract class JsonModelParticle extends Particle {

    protected final ResourceLocation baseModelLocation;

    public JsonModelParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, ResourceLocation baseModelLocation) {
        super(level, x, y, z);
        this.xd = vx;
        this.yd = vy;
        this.zd = vz;
        this.baseModelLocation = baseModelLocation;
    }

    public abstract void setupTransformations(PoseStack poseStack, float partialTicks);

    public abstract ResourceLocation getTexture(float partialTicks);

    protected ResourceLocation getBaseModelLocation() {
        return this.baseModelLocation;
    }

    protected BakedModel getBakedModel(ModelManager modelManager) {
        ModelResourceLocation neoLoc = new ModelResourceLocation(this.baseModelLocation, "standalone");
        BakedModel model = modelManager.getModel(neoLoc);
        if (model != modelManager.getMissingModel()) {
            return model;
        }
        ModelResourceLocation fabricLoc = new ModelResourceLocation(this.baseModelLocation, "fabric_resource");
        return modelManager.getModel(fabricLoc);
    }

    @Override
    public void render(VertexConsumer ignoredBuffer, Camera camera, float partialTicks) {
        ModelManager modelManager = Minecraft.getInstance().getModelManager();
        BakedModel model = getBakedModel(modelManager);

        if (model == modelManager.getMissingModel()) return;

        Vec3 camPos = camera.getPosition();
        float x = (float) (Mth.lerp(partialTicks, this.xo, this.x) - camPos.x());
        float y = (float) (Mth.lerp(partialTicks, this.yo, this.y) - camPos.y() + 0.1f);
        float z = (float) (Mth.lerp(partialTicks, this.zo, this.z) - camPos.z());

        PoseStack poseStack = new PoseStack();
        poseStack.translate(x, y, z);

        this.setupTransformations(poseStack, partialTicks);

        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

        ResourceLocation dynamicTexture = this.getTexture(partialTicks);
        RenderType renderType = RenderType.entityTranslucent(dynamicTexture);
        VertexConsumer modelConsumer = bufferSource.getBuffer(renderType);

        int light = this.getLightColor(partialTicks);
        PoseStack.Pose pose = poseStack.last();
        RandomSource random = RandomSource.create(42);

        for (Direction dir : Direction.values()) {
            renderQuads(pose, modelConsumer, model.getQuads(null, dir, random), this.rCol, this.gCol, this.bCol, this.alpha, light);
        }
        renderQuads(pose, modelConsumer, model.getQuads(null, null, random), this.rCol, this.gCol, this.bCol, this.alpha, light);

        bufferSource.endBatch(renderType);
    }

    private void renderQuads(PoseStack.Pose pose, VertexConsumer consumer, List<BakedQuad> quads, float r, float g, float b, float a, int light) {
        for (BakedQuad quad : quads) {
            int[] vertices = quad.getVertices();
            float nx = quad.getDirection().getStepX();
            float ny = quad.getDirection().getStepY();
            float nz = quad.getDirection().getStepZ();

            TextureAtlasSprite sprite = quad.getSprite();
            float spriteWidth = sprite.getU1() - sprite.getU0();
            float spriteHeight = sprite.getV1() - sprite.getV0();

            for (int i = 0; i < 4; ++i) {
                int offset = i * 8;
                float vx = Float.intBitsToFloat(vertices[offset]);
                float vy = Float.intBitsToFloat(vertices[offset + 1]);
                float vz = Float.intBitsToFloat(vertices[offset + 2]);

                float absoluteU = Float.intBitsToFloat(vertices[offset + 4]);
                float absoluteV = Float.intBitsToFloat(vertices[offset + 5]);

                float relativeU = (absoluteU - sprite.getU0()) / spriteWidth;
                float relativeV = (absoluteV - sprite.getV0()) / spriteHeight;

                consumer.addVertex(pose.pose(), vx, vy, vz)
                        .setColor(r, g, b, a)
                        .setUv(relativeU, relativeV)
                        .setOverlay(OverlayTexture.NO_OVERLAY)
                        .setLight(light)
                        .setNormal(pose, nx, ny, nz);
            }
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }
}