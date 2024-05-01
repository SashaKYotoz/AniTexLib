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
        if (ModConfig.TO_SHOW_EXAMPLE.get())
            cir.setReturnValue(TextureAnimator.options.isEmpty() ? new ResourceLocation("textures/entity/pig/pig.png") : TextureAnimator.getAnimatedTextureByName(AniTexLib.MODID,"textures/entity/pig_animated/","pig_animated"));
//        ResourceLocation tmpLocation = AnimateOptionsReader.getObjectWithoutUpdate(AniTexLib.MODID,pEntity.getUUID()) == null ? TextureAnimator.getManagedAnimatedTextureByName(AniTexLib.MODID,"textures/entity/pig_animated/","pig_animated",pEntity.isOnGround(),0,10,4,pEntity.getUUID()) : TextureAnimator.getManagedAnimatedTextureByName(AniTexLib.MODID,null,null,null,null,null,null,pEntity.getUUID());
//        cir.setReturnValue(tmpLocation != null ? tmpLocation : new ResourceLocation("textures/entity/pig/pig.png"));
    }
}
