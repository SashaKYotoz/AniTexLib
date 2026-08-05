package net.sashakyotoz.anitexlib.mixin.common;

import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.sashakyotoz.anitexlib.api.common.texture.TextureJsonCollector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(MultiPackResourceManager.class)
public class MultiPackResourceManagerMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void gatherJsons(PackType type, List<PackResources> packs, CallbackInfo ci) {
        TextureJsonCollector.INSTANCE.preload((ResourceManager) this);
    }
}