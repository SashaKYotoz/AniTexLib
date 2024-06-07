package net.sashakyotoz.anitexlib.client.particles.parents;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.sashakyotoz.anitexlib.utils.render.RenderTypesHandler;
import net.sashakyotoz.anitexlib.utils.render.RenderUtils;
import org.lwjgl.opengl.GL11;

public class FluidParticleRenderType implements ParticleRenderType {
    public static final FluidParticleRenderType INSTANCE = new FluidParticleRenderType();

    private static void beginRenderCommon(BufferBuilder bufferBuilder, TextureManager textureManager) {
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShader(RenderUtils::getFluidShader);
        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
        RenderTypesHandler.particleMVMatrix = RenderSystem.getModelViewMatrix();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
    }

    private static void endRenderCommon() {
        Minecraft.getInstance().textureManager.getTexture(TextureAtlas.LOCATION_PARTICLES).restoreLastBlurMipmap();
        RenderSystem.depthMask(true);
    }

    @Override
    public void begin(BufferBuilder b, TextureManager tex) {
        beginRenderCommon(b, tex);
    }

    @Override
    public void end(Tesselator t) {
        t.end();
        RenderSystem.enableDepthTest();
        endRenderCommon();
    }
}