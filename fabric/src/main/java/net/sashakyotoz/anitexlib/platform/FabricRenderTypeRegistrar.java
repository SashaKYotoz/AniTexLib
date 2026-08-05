package net.sashakyotoz.anitexlib.platform;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;
import net.sashakyotoz.anitexlib.api.client.render.type.IRenderTypeRegistrar;

public class FabricRenderTypeRegistrar implements IRenderTypeRegistrar {

    @Override
    public RenderType create(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, RenderType.CompositeState state) {
        return RenderType.create(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, state);
    }
}