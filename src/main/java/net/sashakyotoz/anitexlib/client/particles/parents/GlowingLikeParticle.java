package net.sashakyotoz.anitexlib.client.particles.parents;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.sashakyotoz.anitexlib.client.particles.parents.rendertypes.GlowingParticleRenderType;

public class GlowingLikeParticle extends TextureSheetParticle {
    public SpriteSet spriteset;
    public Integer[] LIFETIME_VARIANTS = new Integer[3];

    public GlowingLikeParticle(ClientLevel level, double x, double y, double z, float r, float g, float b, SpriteSet spriteset) {
        super(level, x, y, z, 0, 0, 0);
        this.setColor(r, g, b);
        this.setPos(x, y, z);
        this.gravity = 0;
        this.friction = 0f;
        this.lifetime = 10;
        this.spriteset = spriteset;
        this.pickSprite(spriteset);
    }

    @Override
    protected int getLightColor(float pPartialTick) {
        return 0x7800F0;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return GlowingParticleRenderType.INSTANCE;
    }

    @Override
    public void tick() {
        float ratio = (float) (this.getLifetime() - this.age) / this.getLifetime();
        this.setAlpha(ratio);
        this.setSize(this.bbWidth * ratio, this.bbHeight * ratio);
        super.tick();
    }
}
