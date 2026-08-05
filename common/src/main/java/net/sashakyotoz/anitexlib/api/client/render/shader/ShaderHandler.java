package net.sashakyotoz.anitexlib.api.client.render.shader;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.anitexlib.Constants;
import net.sashakyotoz.anitexlib.api.client.AniTexLibClientRegs;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.util.function.Consumer;

@ApiStatus.Internal
public class ShaderHandler {
    @FunctionalInterface
    public interface ShaderRegisterFunc {
        void register(ResourceLocation id, VertexFormat vertexFormat, Consumer<ShaderInstance> onLoaded) throws IOException;
    }

    public static void registerShaders(ShaderRegisterFunc registerFunc) {
        try {
            registerFunc.register(
                    Constants.makeId("glowing"),
                    DefaultVertexFormat.POSITION_COLOR,
                    shader -> AniTexLibClientRegs.Types.GLOWING_SHADER = shader
            );

            registerFunc.register(
                    Constants.makeId( "glowing_particle"),
                    DefaultVertexFormat.PARTICLE,
                    shader -> AniTexLibClientRegs.Types.GLOWING_PARTICLE_SHADER = shader
            );
        } catch (IOException e) {
            Constants.LOG.error("Failed to load custom shaders", e);
        }
    }
}