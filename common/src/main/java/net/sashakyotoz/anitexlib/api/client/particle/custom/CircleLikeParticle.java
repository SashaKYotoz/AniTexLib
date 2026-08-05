package net.sashakyotoz.anitexlib.api.client.particle.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.sashakyotoz.anitexlib.Constants;
import net.sashakyotoz.anitexlib.api.client.particle.ParticleModelRegistry;
import net.sashakyotoz.anitexlib.api.client.particle.custom.parent.JsonModelParticle;
import net.sashakyotoz.anitexlib.api.client.particle.options.CircleParticleOption;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class CircleLikeParticle extends JsonModelParticle {

    public Vector3f endColor;
    public Integer[] LIFETIME_VARIANTS = {60, 80, 100};
    public float criticalAngle = 0;
    public float quadSize = 1.0f;

    protected CircleLikeParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
        super(level, x, y, z, vx, vy, vz, ParticleModelRegistry.CIRCLE_MODEL);
        this.lifetime = LIFETIME_VARIANTS[RandomSource.create().nextInt(LIFETIME_VARIANTS.length)];
        this.gravity = 0;
        this.alpha = 0.5f;
    }

    @Override
    public void setupTransformations(PoseStack poseStack, float partialTicks) {
        poseStack.mulPose(Axis.XP.rotationDegrees(180 - Mth.lerp(this.age / 75f, -this.criticalAngle, this.criticalAngle)));
        poseStack.mulPose(Axis.ZN.rotationDegrees(180 - Mth.lerp(this.age / 75f, -this.criticalAngle, this.criticalAngle)));
        poseStack.mulPose(Axis.YP.rotationDegrees((this.age * 5) % 360));
        poseStack.scale(this.quadSize, this.quadSize, this.quadSize);
        poseStack.translate(-0.5f, 0.0f, -0.5f);
    }

    @Override
    public ResourceLocation getTexture(float partialTicks) {
        return Constants.makeId("textures/particle/circle.png");
    }

    @Override
    public void tick() {
        super.tick();
        float delta = 0.15f + this.age / 40f;
        float r = Mth.lerp(delta, 0, this.endColor.x());
        float g = Mth.lerp(delta, 0, this.endColor.y());
        float b = Mth.lerp(delta, 0, this.endColor.z());
        this.setColor(r, g, b);
    }

    public static CircleLikeParticle.CircleLikeParticleProvider provider(SpriteSet spriteSet) {
        return new CircleLikeParticle.CircleLikeParticleProvider(spriteSet);
    }

    public static class CircleLikeParticleProvider implements ParticleProvider<CircleParticleOption> {
        private final SpriteSet spriteSet;

        public CircleLikeParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(CircleParticleOption pType, @NotNull ClientLevel pLevel, double x, double y, double z, double pXSpeed, double pYSpeed, double pZSpeed) {
            CircleLikeParticle particle = new CircleLikeParticle(pLevel, x, y, z, pXSpeed, pYSpeed, pZSpeed);
            particle.setParticleSpeed(pXSpeed, pYSpeed, pZSpeed);
            particle.endColor = new Vector3f(pType.redColor(), pType.greenColor(), pType.blueColor());
            particle.quadSize = pType.scale();
            particle.criticalAngle = pType.criticalAngle();
            return particle;
        }
    }
}