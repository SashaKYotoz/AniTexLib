package net.sashakyotoz.anitexlib.client.particles.types;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.sashakyotoz.anitexlib.client.particles.parents.GlowingLikeParticle;
import net.sashakyotoz.anitexlib.client.particles.parents.options.ColorableParticleOption;
import org.antlr.v4.runtime.misc.Triple;
import org.jetbrains.annotations.NotNull;

public class SparkleLikeParticle extends GlowingLikeParticle {
    public static @NotNull SparkleLikeParticle.SparkleProvider provider(SpriteSet spriteSet) {
        return new SparkleLikeParticle.SparkleProvider(spriteSet);
    }
    public static class SparkleProvider implements ParticleProvider<ColorableParticleOption> {
        private final SpriteSet spriteSet;

        public SparkleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(@NotNull ColorableParticleOption pType, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SparkleLikeParticle particle = new SparkleLikeParticle(level,x,y,z,(float) xSpeed,(float) ySpeed,(float) zSpeed,this.spriteSet);
            particle.setParticleSpeed(xSpeed,ySpeed,zSpeed);
            particle.setColor(pType.redColor(),pType.greenColor(), pType.blueColor());
            return particle;
        }
    }

    public SparkleLikeParticle(ClientLevel level, double x, double y, double z, float vx, float vy, float vz, SpriteSet spriteset) {
        super(level, x, y, z, vx, vy, vz, spriteset);
        LIFETIME_VARIANTS[0] = 20;
        LIFETIME_VARIANTS[1] = 30;
        LIFETIME_VARIANTS[2] = 60;
        this.lifetime = LIFETIME_VARIANTS[RandomSource.create().nextIntBetweenInclusive(0,LIFETIME_VARIANTS.length-1)];
        this.roll = (0.5f * (float) ((random.nextDouble() - 0.5D)));
        this.gravity -=0.2f;
        this.setAlpha(0.5f);
    }
}
