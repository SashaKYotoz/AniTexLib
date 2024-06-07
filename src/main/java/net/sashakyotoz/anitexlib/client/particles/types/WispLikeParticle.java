package net.sashakyotoz.anitexlib.client.particles.types;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.sashakyotoz.anitexlib.client.particles.parents.FluidParticleRenderType;
import net.sashakyotoz.anitexlib.client.particles.parents.GlowingLikeParticle;
import org.antlr.v4.runtime.misc.Triple;
import org.jetbrains.annotations.NotNull;

public class WispLikeParticle extends GlowingLikeParticle {
    public Triple<Float, Float, Float> COLOR;
    public Triple<Float, Float, Float> END_COLOR;
    public WispLikeParticle(ClientLevel world, double x, double y, double z, float startR, float startG, float startB, float endR, float endG, float endB, SpriteSet spriteset) {
        super(world, x, y, z, startR, startG, startB, spriteset);
        LIFETIME_VARIANTS[0] = 10;
        LIFETIME_VARIANTS[1] = 30;
        LIFETIME_VARIANTS[2] = 50;
        this.lifetime = LIFETIME_VARIANTS[RandomSource.create().nextIntBetweenInclusive(0,LIFETIME_VARIANTS.length-1)];
        this.xd = ((random.nextDouble() - 0.5D) / 25);
        this.yd = ((random.nextDouble() - 0.25D) / 25);
        this.zd = ((random.nextDouble() - 0.5D) / 25);
        this.setParticleSpeed(xd,yd,zd);
        this.roll = (0.5f * (float) ((random.nextDouble() - 0.5D) * 2));
        this.gravity = 0;
        this.setAlpha(0.5f);
        COLOR = new Triple<>(startR,startG,startB);
        END_COLOR = new Triple<>(endR,endG,endB);
    }
    @Override
    public void tick() {
        animateColor();
        super.tick();
    }
    public void animateColor(){
        float lifeProgress = (float) this.age / this.lifetime;
        float r = interpolate(this.COLOR.a, this.END_COLOR.a, lifeProgress);
        float g = interpolate(this.COLOR.b, this.END_COLOR.b, lifeProgress);
        float b = interpolate(this.COLOR.c, this.END_COLOR.c, lifeProgress);
        this.setColor(r, g, b);
    }

    private float interpolate(float start, float end, float fraction) {
        return (start + (end - start) * fraction) == end ? end + (start - end) * fraction : start + (end - start) * fraction;
    }
    @Override
    public void render(VertexConsumer b, Camera info, float pticks) {
        super.render(b, info, pticks);
    }
    public static @NotNull WispParticleProvider provider(SpriteSet spriteSet) {
        return new WispParticleProvider(spriteSet);
    }
    public static class WispParticleProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public WispParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(@NotNull SimpleParticleType typeIn, @NotNull ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new WispLikeParticle(worldIn, x, y, z, 0, 0f, 0f, 0, 0.25f, 0.5f, this.spriteSet);
        }
    }
}