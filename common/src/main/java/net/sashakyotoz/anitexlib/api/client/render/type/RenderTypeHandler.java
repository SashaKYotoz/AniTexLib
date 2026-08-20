package net.sashakyotoz.anitexlib.api.client.render.type;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.sashakyotoz.anitexlib.api.client.AniTexLibClientRegs;
import net.sashakyotoz.anitexlib.platform.Services;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.SequencedMap;

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
            SequencedMap<RenderType, ByteBufferBuilder> buffers = new Object2ObjectLinkedOpenHashMap<>();
            for (RenderType type : new RenderType[]{
                    AniTexLibClientRegs.Types.GLOWING_PARTICLE,
                    AniTexLibClientRegs.Types.GLOWING}) {

                int bufferSize = HAS_OPTIMIZATION_MOD ? 32768 : type.bufferSize();
                buffers.put(type, new ByteBufferBuilder(bufferSize));
            }
            DELAYED_RENDER = MultiBufferSource.immediateWithBuffers(buffers, new ByteBufferBuilder(128));
        }
        return DELAYED_RENDER;
    }

    public static void flushDelayedRenders() {
        RenderSystem.getModelViewStack().pushMatrix();
        if (particleMVMatrix != null) RenderSystem.getModelViewStack().mul(particleMVMatrix);
        RenderSystem.applyModelViewMatrix();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        getDelayedRender().endBatch(AniTexLibClientRegs.Types.GLOWING_PARTICLE);
        RenderSystem.getModelViewStack().popMatrix();

        RenderSystem.applyModelViewMatrix();

        getDelayedRender().endBatch(AniTexLibClientRegs.Types.GLOWING);
    }
}