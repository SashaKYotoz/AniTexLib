package net.sashakyotoz.anitexlib.api.client.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.sashakyotoz.anitexlib.api.client.particle.custom.parent.GlowingLikeParticle;
import net.sashakyotoz.anitexlib.api.client.particle.options.ColorableParticleOption;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class WispLikeParticle extends GlowingLikeParticle {
    public Vector3f END_COLOR;

    public WispLikeParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteset) {
        super(level, x, y, z, (float) vx, (float) vy, (float) vz, spriteset);
        LIFETIME_VARIANTS[0] = 10;
        LIFETIME_VARIANTS[1] = 30;
        LIFETIME_VARIANTS[2] = 50;
        this.lifetime = LIFETIME_VARIANTS[RandomSource.create().nextIntBetweenInclusive(0, LIFETIME_VARIANTS.length - 1)];
        this.xd = ((random.nextDouble() - 0.5D) / 25);
        this.yd = ((random.nextDouble() - 0.25D) / 25);
        this.zd = ((random.nextDouble() - 0.5D) / 25);
        this.setParticleSpeed(xd, yd, zd);
        this.roll = (0.5f * (float) ((random.nextDouble() - 0.5D) * 2));
        this.gravity = 0;
        this.setAlpha(0.5f);
    }

    @Override
    public void tick() {
        animateColor();
        super.tick();
    }

    public void animateColor() {
        float lifeProgress = 0.25f;
        if (this.age % 10 == 0)
            lifeProgress = random.nextFloat() + age > 10 ? age / 100f + 0.1f : age / 10f;
        float r = Mth.lerp(lifeProgress, 0, this.END_COLOR.x());
        float g = Mth.lerp(lifeProgress, 0, this.END_COLOR.y());
        float b = Mth.lerp(lifeProgress, 0, this.END_COLOR.z());
        this.setColor(r, g, b);
    }

    public static WispParticleProvider provider(SpriteSet spriteSet) {
        return new WispParticleProvider(spriteSet);
    }

    public static class WispParticleProvider implements ParticleProvider<ColorableParticleOption> {
        private final SpriteSet spriteSet;

        public WispParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(@NotNull ColorableParticleOption pType, @NotNull ClientLevel pLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            WispLikeParticle particle = new WispLikeParticle(pLevel, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
            particle.setParticleSpeed(xSpeed, ySpeed, zSpeed);
            particle.END_COLOR = new Vector3f(pType.redColor(), pType.greenColor(), pType.blueColor());
            return particle;
        }
    }
}