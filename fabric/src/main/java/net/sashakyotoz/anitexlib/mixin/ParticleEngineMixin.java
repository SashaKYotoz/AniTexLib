package net.sashakyotoz.anitexlib.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.sashakyotoz.anitexlib.api.client.particle.render.GlowingParticleRenderType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Queue;

@Mixin(ParticleEngine.class)
public abstract class ParticleEngineMixin {
    @Final
    @Shadow
    private Map<ParticleRenderType, Queue<Particle>> particles;

    @Final
    @Shadow
    private TextureManager textureManager;

    @Inject(method = "render", at = @At("TAIL"))
    private void renderCustomParticles(LightTexture lightTexture, Camera camera, float partialTicks, CallbackInfo ci) {
        Queue<Particle> queue = this.particles.get(GlowingParticleRenderType.INSTANCE);
        if (queue != null && !queue.isEmpty()) {
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder bufferBuilder = GlowingParticleRenderType.INSTANCE.begin(tesselator, this.textureManager);

            if (bufferBuilder != null) {
                for (Particle particle : queue)
                    particle.render(bufferBuilder, camera, partialTicks);

                BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
                RenderSystem.depthMask(true);
                RenderSystem.defaultBlendFunc();
                RenderSystem.disableBlend();
            }
        }
    }
}