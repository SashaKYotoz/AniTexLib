package net.sashakyotoz.anitexlib.api.client.particle.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.sashakyotoz.anitexlib.Constants;
import net.sashakyotoz.anitexlib.api.client.particle.ParticleModelRegistry;
import net.sashakyotoz.anitexlib.api.client.particle.custom.parent.JsonModelParticle;
import net.sashakyotoz.anitexlib.api.client.particle.options.WaveParticleOption;
import net.sashakyotoz.anitexlib.api.common.AniTexLibRegs;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class WaveLikeParticle extends JsonModelParticle {

    public Vector3f endColor;
    public Integer[] LIFETIME_VARIANTS = {50, 70, 90};
    public float quadSize = 1.0f;

    public WaveLikeParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
        super(level, x, y, z, vx, vy, vz, ParticleModelRegistry.WAVE_MODEL);
        this.lifetime = LIFETIME_VARIANTS[this.random.nextInt(LIFETIME_VARIANTS.length)];
        this.gravity = 0;
        this.alpha = 0.5f;
    }

    @Override
    public void tick() {
        animateColor();
        super.tick();
    }

    public void animateColor() {
        float lifeProgress = 0.25f;
        if (this.age % 10 == 0) {
            lifeProgress = this.random.nextFloat();
        }
        float r = Mth.lerp(lifeProgress, 0, this.endColor.x());
        float g = Mth.lerp(lifeProgress, 0, this.endColor.y());
        float b = Mth.lerp(lifeProgress, 0, this.endColor.z());
        this.setColor(r, g, b);
    }

    @Override
    public ResourceLocation getTexture(float partialTicks) {
        return AniTexLibRegs.TEXTURE_ANIMATOR.get(Constants.makeId("textures/particle/wave_like/wave_0.png"));
    }

    @Override
    public void setupTransformations(PoseStack poseStack, float partialTicks) {
        poseStack.mulPose(Axis.XP.rotationDegrees(180));
        float currentRoll = Mth.lerp(partialTicks, this.oRoll, this.roll);
        poseStack.mulPose(Axis.YP.rotationDegrees(currentRoll));

        poseStack.scale(this.quadSize, this.quadSize, this.quadSize);
        poseStack.translate(-0.5f, 0.0f, -0.5f);
    }

    public static @NotNull ParticleProvider<WaveParticleOption> provider(SpriteSet spriteSet) {
        return new WaveLikeParticleProvider(spriteSet);
    }

    public static class WaveLikeParticleProvider implements ParticleProvider<WaveParticleOption> {
        private final SpriteSet spriteSet;

        public WaveLikeParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(WaveParticleOption pType, @NotNull ClientLevel pLevel, double x, double y, double z, double pXSpeed, double pYSpeed, double pZSpeed) {
            WaveLikeParticle particle = new WaveLikeParticle(pLevel, x, y, z, pXSpeed, pYSpeed, pZSpeed);
            particle.setParticleSpeed(pXSpeed, pYSpeed, pZSpeed);
            particle.endColor = new Vector3f(pType.redColor(), pType.greenColor(), pType.blueColor());
            particle.oRoll = pType.roll();
            particle.roll = pType.roll();
            particle.quadSize = pType.scale();
            return particle;
        }
    }
}