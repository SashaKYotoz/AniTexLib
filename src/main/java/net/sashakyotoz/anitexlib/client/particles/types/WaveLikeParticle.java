package net.sashakyotoz.anitexlib.client.particles.types;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.sashakyotoz.anitexlib.AniTexLib;
import net.sashakyotoz.anitexlib.client.particles.parents.types.WaveParticleOption;
import net.sashakyotoz.anitexlib.client.particles.types.models.CircleParticleModel;
import net.sashakyotoz.anitexlib.utils.TextureAnimator;
import org.antlr.v4.runtime.misc.Triple;
import org.jetbrains.annotations.NotNull;

public class WaveLikeParticle extends TextureSheetParticle {
    public Triple<Float, Float, Float> END_COLOR;
    public Integer[] LIFETIME_VARIANTS = new Integer[3];

    public WaveLikeParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteset) {
        super(level, x, y, z, vx, vy, vz);
        LIFETIME_VARIANTS[0] = 50;
        LIFETIME_VARIANTS[1] = 70;
        LIFETIME_VARIANTS[2] = 90;
        this.lifetime = LIFETIME_VARIANTS[RandomSource.create().nextInt(LIFETIME_VARIANTS.length)];
        this.gravity = 0;
        this.setAlpha(0.5f);
        new WaveRenderSequence(this).start();
    }
    @Override
    public void tick() {
        animateColor();
        super.tick();
    }

    public void animateColor() {
        float lifeProgress = 0.25f;
        if (this.age % 10 == 0)
            lifeProgress = random.nextFloat();
        float r = Mth.lerp(lifeProgress, 0, this.END_COLOR.a);
        float g = Mth.lerp(lifeProgress, 0, this.END_COLOR.b);
        float b = Mth.lerp(lifeProgress, 0, this.END_COLOR.c);
        this.setColor(r, g, b);
    }

    private static class WaveRenderSequence {
        private final WaveLikeParticle particle;

        private class WaveRenderer {
            public final EntityModel<Entity> model = new CircleParticleModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(CircleParticleModel.LAYER_LOCATION));

            public WaveRenderer() {
                MinecraftForge.EVENT_BUS.register(this);
            }

            @SubscribeEvent
            public void render(RenderLevelStageEvent event) {
                if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
                    VertexConsumer consumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.entityTranslucent(TextureAnimator.getAnimatedTextureByName(AniTexLib.MODID, "textures/particle/wave_like/", "wave")));
                    Vec3 camPos = event.getCamera().getPosition();
                    double x = Mth.lerp(event.getPartialTick(), particle.xo, particle.x) - camPos.x();
                    double y = Mth.lerp(event.getPartialTick(), particle.yo, particle.y) - camPos.y() + 0.1f;
                    double z = Mth.lerp(event.getPartialTick(), particle.zo, particle.z) - camPos.z();
                    event.getPoseStack().pushPose();
                    event.getPoseStack().translate(x, y, z);
                    event.getPoseStack().mulPose(Axis.XP.rotationDegrees(180));
                    event.getPoseStack().mulPose(Axis.YP.rotationDegrees(particle.roll));
                    event.getPoseStack().scale(particle.quadSize,particle.quadSize,particle.quadSize);
                    model.renderToBuffer(event.getPoseStack(), consumer, particle.getLightColor(event.getPartialTick()), OverlayTexture.NO_OVERLAY, particle.rCol, particle.gCol, particle.bCol, particle.alpha);
                    event.getPoseStack().popPose();
                }
            }
        }

        private final WaveRenderer renderer;

        public WaveRenderSequence(WaveLikeParticle particle) {
            this.particle = particle;
            this.renderer = new WaveRenderer();
        }

        public void start() {
            MinecraftForge.EVENT_BUS.register(renderer);
            MinecraftForge.EVENT_BUS.register(this);
        }

        @SubscribeEvent
        public void tick(TickEvent.ClientTickEvent event) {
            if (!particle.isAlive())
                end();
        }

        private void end() {
            MinecraftForge.EVENT_BUS.unregister(renderer);
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.NO_RENDER;
    }

    public static @NotNull WaveLikeParticle.WaveLikeParticleProvider provider(SpriteSet spriteSet) {
        return new WaveLikeParticle.WaveLikeParticleProvider(spriteSet);
    }

    public static class WaveLikeParticleProvider implements ParticleProvider<WaveParticleOption> {
        private final SpriteSet spriteSet;

        public WaveLikeParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(WaveParticleOption pType, @NotNull ClientLevel pLevel, double x, double y, double z, double pXSpeed, double pYSpeed, double pZSpeed) {
            WaveLikeParticle particle = new WaveLikeParticle(pLevel, x, y, z, pXSpeed,pYSpeed,pZSpeed, this.spriteSet);
            particle.setParticleSpeed(pXSpeed, pYSpeed, pZSpeed);
            particle.END_COLOR = new Triple<>(pType.redColor(), pType.greenColor(), pType.blueColor());
            particle.oRoll = pType.roll();
            particle.quadSize = pType.scale();
            particle.roll = pType.roll();
            return particle;
        }
    }
}