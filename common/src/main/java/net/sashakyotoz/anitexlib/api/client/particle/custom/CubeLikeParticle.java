package net.sashakyotoz.anitexlib.api.client.particle.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.anitexlib.Constants;
import net.sashakyotoz.anitexlib.api.client.particle.ParticleModelRegistry;
import net.sashakyotoz.anitexlib.api.client.particle.custom.parent.JsonModelParticle;
import net.sashakyotoz.anitexlib.api.client.particle.options.ColorableParticleOption;
import org.jetbrains.annotations.NotNull;

public class CubeLikeParticle extends JsonModelParticle {
    public Integer[] LIFETIME_VARIANTS = {15, 30, 45};
    public float quadSize = 0.5f;

    public CubeLikeParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
        super(level, x, y, z, vx, vy, vz, ParticleModelRegistry.CUBE_MODEL);
        this.lifetime = LIFETIME_VARIANTS[this.random.nextIntBetweenInclusive(0, LIFETIME_VARIANTS.length - 1)];
        this.alpha = 0.5f;
    }

    @Override
    public void setupTransformations(PoseStack poseStack, float partialTicks) {
        poseStack.scale(this.quadSize, this.quadSize, this.quadSize);
        poseStack.translate(-0.5f, 0.0f, -0.5f);
    }

    @Override
    public ResourceLocation getTexture(float partialTicks) {
        return Constants.makeId("textures/particle/cube.png");
    }

    @Override
    public int getLightColor(float partialTick) {
        return 15728880;
    }

    public static @NotNull ParticleProvider<ColorableParticleOption> provider(SpriteSet spriteSet) {
        return new CubeProvider(spriteSet);
    }

    public static class CubeProvider implements ParticleProvider<ColorableParticleOption> {
        private final SpriteSet spriteSet;

        public CubeProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(@NotNull ColorableParticleOption pType, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            CubeLikeParticle particle = new CubeLikeParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.setParticleSpeed(xSpeed, ySpeed, zSpeed);
            particle.setColor(pType.redColor(), pType.greenColor(), pType.blueColor());
            return particle;
        }
    }
}