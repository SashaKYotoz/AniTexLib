package net.sashakyotoz.anitexlib.client.particles.types;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.sashakyotoz.anitexlib.AniTexLib;
import net.sashakyotoz.anitexlib.client.particles.parents.options.CircleParticleOption;
import net.sashakyotoz.anitexlib.client.particles.types.models.CircleParticleModel;
import org.antlr.v4.runtime.misc.Triple;
import org.jetbrains.annotations.NotNull;

public class CircleLikeParticle extends TextureSheetParticle {
    public Triple<Float, Float, Float> END_COLOR;
    public Integer[] LIFETIME_VARIANTS = new Integer[3];
    public float criticalAngle = 0;

    public CircleLikeParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteset) {
        super(level, x, y, z, vx, vy, vz);
        LIFETIME_VARIANTS[0] = 60;
        LIFETIME_VARIANTS[1] = 80;
        LIFETIME_VARIANTS[2] = 100;
        this.lifetime = LIFETIME_VARIANTS[RandomSource.create().nextInt(LIFETIME_VARIANTS.length)];
        this.gravity = 0;
        this.setAlpha(0.5f);
        new CircleRenderSequence(this).start();
    }
    @Override
    public void tick() {
        animateColor();
        super.tick();
    }

    public void animateColor() {
        float delta = 0.15f + age/40f;
        float r = Mth.lerp(delta, 0, this.END_COLOR.a);
        float g = Mth.lerp(delta, 0, this.END_COLOR.b);
        float b = Mth.lerp(delta, 0, this.END_COLOR.c);
        this.setColor(r, g, b);
    }

    private static class CircleRenderSequence {
        private final CircleLikeParticle particle;

        private class CircleRenderer {
            public final EntityModel<Entity> model = new CircleParticleModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(CircleParticleModel.LAYER_LOCATION));

            public CircleRenderer() {
                MinecraftForge.EVENT_BUS.register(this);
            }

            @SubscribeEvent
            public void render(RenderLevelStageEvent event) {
                if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
                    VertexConsumer consumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.entityTranslucent(new ResourceLocation(AniTexLib.MODID,"textures/particle/circle.png")));
                    Vec3 camPos = event.getCamera().getPosition();
                    double x = Mth.lerp(event.getPartialTick(), particle.xo, particle.x) - camPos.x();
                    double y = Mth.lerp(event.getPartialTick(), particle.yo, particle.y) - camPos.y() + 0.1f;
                    double z = Mth.lerp(event.getPartialTick(), particle.zo, particle.z) - camPos.z();
                    event.getPoseStack().pushPose();
                    event.getPoseStack().translate(x, y, z);
                    event.getPoseStack().mulPose(Axis.XP.rotationDegrees(180 - Mth.lerp(particle.age/75f,-particle.criticalAngle,particle.criticalAngle)));
                    event.getPoseStack().mulPose(Axis.ZN.rotationDegrees(180 - Mth.lerp(particle.age/75f,-particle.criticalAngle,particle.criticalAngle)));
                    event.getPoseStack().mulPose(Axis.YP.rotationDegrees(particle.age*5 % 360));
                    event.getPoseStack().scale(particle.quadSize,particle.quadSize,particle.quadSize);
                    model.renderToBuffer(event.getPoseStack(), consumer, particle.getLightColor(event.getPartialTick()), OverlayTexture.NO_OVERLAY, particle.rCol, particle.gCol, particle.bCol, particle.alpha);
                    event.getPoseStack().popPose();
                }
            }
        }

        private final CircleRenderer renderer;

        public CircleRenderSequence(CircleLikeParticle particle) {
            this.particle = particle;
            this.renderer = new CircleRenderer();
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

    public static @NotNull CircleLikeParticle.CircleLikeParticleProvider provider(SpriteSet spriteSet) {
        return new CircleLikeParticleProvider(spriteSet);
    }

    public static class CircleLikeParticleProvider implements ParticleProvider<CircleParticleOption> {
        private final SpriteSet spriteSet;

        public CircleLikeParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(CircleParticleOption pType, @NotNull ClientLevel pLevel, double x, double y, double z, double pXSpeed, double pYSpeed, double pZSpeed) {
            CircleLikeParticle particle = new CircleLikeParticle(pLevel, x, y, z, pXSpeed,pYSpeed,pZSpeed, this.spriteSet);
            particle.setParticleSpeed(pXSpeed, pYSpeed, pZSpeed);
            particle.END_COLOR = new Triple<>(pType.redColor(), pType.greenColor(), pType.blueColor());
            particle.quadSize = pType.scale();
            particle.criticalAngle = pType.criticalAngle();
            return particle;
        }
    }
}