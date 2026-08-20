package net.sashakyotoz.anitexlib.api.client.particle.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.sashakyotoz.anitexlib.api.client.AniTexLibClientRegs;
import net.sashakyotoz.anitexlib.api.client.render.type.RenderTypeHandler;
import org.lwjgl.opengl.GL11;

public class GlowingParticleRenderType implements ParticleRenderType {
    public static final GlowingParticleRenderType INSTANCE = new GlowingParticleRenderType();

    @Override
    public BufferBuilder begin(Tesselator tesselator, TextureManager manager) {
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        RenderSystem.setShader(AniTexLibClientRegs.Types::getGlowingParticleShader);
        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
        RenderTypeHandler.particleMVMatrix = RenderSystem.getModelViewMatrix();
        return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
    }
}