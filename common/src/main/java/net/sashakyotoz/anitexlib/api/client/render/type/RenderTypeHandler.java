package net.sashakyotoz.anitexlib.api.client.render.type;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.sashakyotoz.anitexlib.api.client.AniTexLibClientRegs;
import net.sashakyotoz.anitexlib.platform.Services;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

@ApiStatus.Internal
public class RenderTypeHandler {
    public static Matrix4f particleMVMatrix = null;
    private static MultiBufferSource.BufferSource DELAYED_RENDER = null;

    private static final boolean HAS_OPTIMIZATION_MOD = checkOptimizationMod();

    private static boolean checkOptimizationMod() {
        return Services.PLATFORM.isModLoaded("sodium") || Services.PLATFORM.isModLoaded("embeddium");
    }

    public static MultiBufferSource.BufferSource getDelayedRender() {
        if (DELAYED_RENDER == null) {
            Map<RenderType, BufferBuilder> buffers = new HashMap<>();
            for (RenderType type : new RenderType[]{
                    AniTexLibClientRegs.Types.GLOWING_PARTICLE,
                    AniTexLibClientRegs.Types.GLOWING}) {

                int bufferSize = HAS_OPTIMIZATION_MOD ? 32768 : type.bufferSize();
                buffers.put(type, new BufferBuilder(bufferSize));
            }
            DELAYED_RENDER = MultiBufferSource.immediateWithBuffers(buffers, new BufferBuilder(128));
        }
        return DELAYED_RENDER;
    }

    public static void flushDelayedRenders() {
        RenderSystem.getModelViewStack().pushPose();

        if (particleMVMatrix != null)
            RenderSystem.getModelViewStack().mulPoseMatrix(particleMVMatrix);

        RenderSystem.applyModelViewMatrix();

        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

        getDelayedRender().endBatch(AniTexLibClientRegs.Types.GLOWING_PARTICLE);

        RenderSystem.getModelViewStack().popPose();
        RenderSystem.applyModelViewMatrix();

        getDelayedRender().endBatch(AniTexLibClientRegs.Types.GLOWING);
    }
}