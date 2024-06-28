package net.sashakyotoz.anitexlib.client.particles.types;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.sashakyotoz.anitexlib.client.particles.parents.GlowingLikeParticle;
import org.antlr.v4.runtime.misc.Triple;
import org.jetbrains.annotations.NotNull;

public class SparkleLikeParticle extends GlowingLikeParticle {
    public static Triple<Float, Float, Float> COLOR = new Triple<>(1f, 1f, 1f);
    public static @NotNull SparkleLikeParticle.SparkleProvider provider(SpriteSet spriteSet) {
        return new SparkleLikeParticle.SparkleProvider(spriteSet);
    }
    public static class SparkleProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public SparkleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(@NotNull SimpleParticleType pType, @NotNull ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SparkleLikeParticle(worldIn, x, y, z, COLOR.a, COLOR.b, COLOR.c, this.spriteSet);
        }
    }

    public SparkleLikeParticle(ClientLevel level, double x, double y, double z, float r, float g, float b, SpriteSet spriteset) {
        super(level, x, y, z, r, g, b, spriteset);
        LIFETIME_VARIANTS[0] = 20;
        LIFETIME_VARIANTS[1] = 30;
        LIFETIME_VARIANTS[2] = 60;
        this.lifetime = LIFETIME_VARIANTS[RandomSource.create().nextIntBetweenInclusive(0,LIFETIME_VARIANTS.length-1)];
        this.y +=0.25f;
        this.xd = ((random.nextDouble() - 0.5D) / 25);
        this.yd = ((random.nextDouble() - 0.5D) / 25);
        this.zd = ((random.nextDouble() - 0.5D) / 25);
        this.setParticleSpeed(xd,yd,zd);
        this.roll = (0.5f * (float) ((random.nextDouble() - 0.5D)));
        this.gravity -=0.3f;
        this.setAlpha(0.5f);
        this.setPower(4);
    }
}
