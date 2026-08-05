package net.sashakyotoz.anitexlib.api.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.sashakyotoz.anitexlib.Constants;
import net.sashakyotoz.anitexlib.api.client.render.type.IRenderTypeRegistrar;

public class AniTexLibClientRegs {
    public static class Types {
        public static ShaderInstance GLOWING_SHADER, GLOWING_PARTICLE_SHADER;

        public static final RenderStateShard.TransparencyStateShard ADDITIVE_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("additive_transparency", () -> {
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        }, () -> {
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        });
        public static final RenderStateShard.TransparencyStateShard TRANSLUCENT_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("translucent_transparency", () -> {
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        }, () -> {
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        });

        public static RenderType GLOWING;

        public static RenderType GLOWING_PARTICLE;

        public static ShaderInstance getGlowingShader() {
            return GLOWING_SHADER;
        }

        public static ShaderInstance getGlowingParticleShader() {
            return GLOWING_PARTICLE_SHADER;
        }

        public static void registerTypes(IRenderTypeRegistrar factory) {
            GLOWING = factory.create(Constants.MOD_ID + ":glowing",
                    DefaultVertexFormat.POSITION_COLOR,
                    VertexFormat.Mode.QUADS, 256, true, false,
                    RenderType.CompositeState.builder()
                            .setWriteMaskState(new RenderStateShard.WriteMaskStateShard(true, false))
                            .setLightmapState(new RenderStateShard.LightmapStateShard(false))
                            .setTransparencyState(ADDITIVE_TRANSPARENCY)
                            .setShaderState(new RenderStateShard.ShaderStateShard(Types::getGlowingShader))
                            .createCompositeState(false));
            GLOWING_PARTICLE = factory.create(Constants.MOD_ID + ":glowing_particle",
                    DefaultVertexFormat.PARTICLE,
                    VertexFormat.Mode.QUADS, 256, true, false,
                    RenderType.CompositeState.builder()
                            .setWriteMaskState(new RenderStateShard.WriteMaskStateShard(true, false))
                            .setLightmapState(new RenderStateShard.LightmapStateShard(false))
                            .setTransparencyState(ADDITIVE_TRANSPARENCY)
                            .setTextureState(new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_PARTICLES, false, false))
                            .setShaderState(new RenderStateShard.ShaderStateShard(Types::getGlowingParticleShader))
                            .createCompositeState(false));
        }
    }
}