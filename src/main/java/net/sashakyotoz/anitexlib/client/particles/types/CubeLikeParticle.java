package net.sashakyotoz.anitexlib.client.particles.types;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
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
import net.sashakyotoz.anitexlib.client.particles.parents.GlowingLikeParticle;
import net.sashakyotoz.anitexlib.client.particles.parents.options.ColorableParticleOption;
import net.sashakyotoz.anitexlib.client.particles.types.models.CircleParticleModel;
import net.sashakyotoz.anitexlib.client.particles.types.models.CubeParticleModel;
import org.antlr.v4.runtime.misc.Triple;
import org.jetbrains.annotations.NotNull;

public class CubeLikeParticle extends GlowingLikeParticle {
    public Triple<Float, Float, Float> END_COLOR;
    public static @NotNull CubeLikeParticle.CubeProvider provider(SpriteSet spriteSet) {
        return new CubeProvider(spriteSet);
    }
    public static class CubeProvider implements ParticleProvider<ColorableParticleOption> {
        private final SpriteSet spriteSet;

        public CubeProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(@NotNull ColorableParticleOption pType, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            CubeLikeParticle particle = new CubeLikeParticle(level,x,y,z,(float) xSpeed,(float) ySpeed,(float) zSpeed,this.spriteSet);
            particle.setParticleSpeed(xSpeed,ySpeed,zSpeed);
            particle.END_COLOR = new Triple<>(pType.redColor(), pType.greenColor(), pType.blueColor());
            return particle;
        }
    }

    @Override
    public void tick() {
        animateColor();
        super.tick();
    }
    public void animateColor(){
        float lifeProgress = 0.25f;
        if (this.age % 5 == 0){
            lifeProgress = random.nextFloat() + age > 10 ? age/100f+0.1f : age/10f;
            quadSize+=RandomSource.create().nextBoolean() ? 0.15f : -0.1f;
        }
        float r = Mth.lerp(lifeProgress, 0, this.END_COLOR.a);
        float g = Mth.lerp(lifeProgress, 0, this.END_COLOR.b);
        float b = Mth.lerp(lifeProgress, 0, this.END_COLOR.c);
        this.setColor(r, g, b);
    }


    public CubeLikeParticle(ClientLevel level, double x, double y, double z, float vx, float vy, float vz, SpriteSet spriteset) {
        super(level, x, y, z, vx, vy, vz, spriteset);
        LIFETIME_VARIANTS[0] = 15;
        LIFETIME_VARIANTS[1] = 30;
        LIFETIME_VARIANTS[2] = 45;
        this.lifetime = LIFETIME_VARIANTS[RandomSource.create().nextIntBetweenInclusive(0,LIFETIME_VARIANTS.length-1)];
        this.setAlpha(0.5f);
        this.quadSize = 0.25f;
        new CubeRenderSequence(this);
    }
    private static class CubeRenderSequence {
        private final CubeLikeParticle particle;

        private class CubeRenderer {
            public final EntityModel<Entity> model = new CubeParticleModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(CubeParticleModel.LAYER_LOCATION));

            public CubeRenderer() {
                MinecraftForge.EVENT_BUS.register(this);
            }

            @SubscribeEvent
            public void render(RenderLevelStageEvent event) {
                if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
                    VertexConsumer consumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.entityTranslucent(new ResourceLocation(AniTexLib.MODID,"textures/particle/cube.png")));
                    Vec3 camPos = event.getCamera().getPosition();
                    double x = Mth.lerp(event.getPartialTick(), particle.xo, particle.x) - camPos.x();
                    double y = Mth.lerp(event.getPartialTick(), particle.yo, particle.y) - camPos.y();
                    double z = Mth.lerp(event.getPartialTick(), particle.zo, particle.z) - camPos.z();
                    event.getPoseStack().pushPose();
                    event.getPoseStack().translate(x, y - particle.quadSize > 0.3f || particle.quadSize < 0.1f ? particle.quadSize : 0, z);
                    event.getPoseStack().scale(particle.quadSize,particle.quadSize,particle.quadSize);
                    model.renderToBuffer(event.getPoseStack(), consumer, particle.getLightColor(event.getPartialTick()), OverlayTexture.NO_OVERLAY, particle.rCol, particle.gCol, particle.bCol, particle.alpha);
                    event.getPoseStack().popPose();
                }
            }
        }

        private final CubeRenderer renderer;

        public CubeRenderSequence(CubeLikeParticle particle) {
            this.particle = particle;
            this.renderer = new CubeRenderer();
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
}