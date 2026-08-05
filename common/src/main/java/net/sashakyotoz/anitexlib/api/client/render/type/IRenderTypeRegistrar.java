package net.sashakyotoz.anitexlib.api.client.render.type;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;

@FunctionalInterface
public interface IRenderTypeRegistrar {
    RenderType create(
            String name,
            VertexFormat format,
            VertexFormat.Mode mode,
            int bufferSize,
            boolean affectsCrumbling,
            boolean sortOnUpload,
            RenderType.CompositeState state
    );
}