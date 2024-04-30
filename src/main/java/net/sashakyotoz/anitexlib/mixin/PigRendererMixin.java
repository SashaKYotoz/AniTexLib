package net.sashakyotoz.anitexlib.mixin;

import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Pig;
import net.sashakyotoz.anitexlib.AniTexLib;
import net.sashakyotoz.anitexlib.ModConfig;
import net.sashakyotoz.anitexlib.utils.TextureAnimator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PigRenderer.class)
public class PigRendererMixin {
    @Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Pig;)Lnet/minecraft/resources/ResourceLocation;", at=@At("RETURN"), cancellable = true)
    public void getTextureLocation(Pig pEntity, CallbackInfoReturnable<ResourceLocation> cir){
        cir.setReturnValue((TextureAnimator.options.isEmpty() && !ModConfig.TO_SHOW_EXAMPLE.get()) ? new ResourceLocation("textures/entity/pig/pig.png") : TextureAnimator.getAnimatedTextureByName(AniTexLib.MODID,"textures/entity/pig_animated/","pig_animated"));
    }
}
