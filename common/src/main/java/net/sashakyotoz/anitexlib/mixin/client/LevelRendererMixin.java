package net.sashakyotoz.anitexlib.mixin.client;

import net.minecraft.client.renderer.LevelRenderer;
import net.sashakyotoz.anitexlib.api.client.render.type.RenderTypeHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Inject(method = "renderLevel", at = @At("TAIL"))
    private void flushDelayedRenders(CallbackInfo ci) {
        RenderTypeHandler.flushDelayedRenders();
    }
}