package net.sashakyotoz.anitexlib.utils.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.fml.ModList;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

public class RenderTypesHandler {
    @OnlyIn(Dist.CLIENT)
    public static Matrix4f particleMVMatrix = null;

    public static void onRenderWorldLast(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_LEVEL) {
            RenderSystem.getModelViewStack().pushPose();
            RenderSystem.getModelViewStack().setIdentity();
            if (particleMVMatrix != null) RenderSystem.getModelViewStack().mulPoseMatrix(particleMVMatrix);
            RenderSystem.applyModelViewMatrix();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            getDelayedRender().endBatch(RenderUtils.GLOWING_PARTICLE);
            RenderSystem.getModelViewStack().popPose();
            RenderSystem.applyModelViewMatrix();
            getDelayedRender().endBatch(RenderUtils.GLOWING);
            getFlowingRender().endBatch(RenderUtils.FLOWING);
        }
    }

    static MultiBufferSource.BufferSource DELAYED_RENDER = null;
    static MultiBufferSource.BufferSource FLOWING_RENDER = null;

    public static MultiBufferSource.BufferSource getDelayedRender() {
        if (DELAYED_RENDER == null) {
            Map<RenderType, BufferBuilder> buffers = new HashMap<>();
            for (RenderType type : new RenderType[]{
                    RenderUtils.GLOWING_PARTICLE,
                    RenderUtils.GLOWING}) {
                buffers.put(type, new BufferBuilder(ModList.get().isLoaded("rubidium") ? 32768 : type.bufferSize()));
            }
            DELAYED_RENDER = MultiBufferSource.immediateWithBuffers(buffers, new BufferBuilder(128));
        }
        return DELAYED_RENDER;
    }
    public static MultiBufferSource.BufferSource getFlowingRender() {
        if (FLOWING_RENDER == null) {
            Map<RenderType, BufferBuilder> buffers = new HashMap<>();
            for (RenderType type : new RenderType[]{
                    RenderUtils.GLOWING_PARTICLE,
                    RenderUtils.FLOWING}) {
                buffers.put(type, new BufferBuilder(ModList.get().isLoaded("rubidium") ? 32768 : type.bufferSize()));
            }
            FLOWING_RENDER = MultiBufferSource.immediateWithBuffers(buffers, new BufferBuilder(128));
        }
        return FLOWING_RENDER;
    }
}
