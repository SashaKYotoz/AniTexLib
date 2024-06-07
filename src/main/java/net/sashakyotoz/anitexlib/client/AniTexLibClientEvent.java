package net.sashakyotoz.anitexlib.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sashakyotoz.anitexlib.AniTexLib;
import net.sashakyotoz.anitexlib.client.particles.types.SparkleLikeParticle;
import net.sashakyotoz.anitexlib.client.particles.types.WispLikeParticle;
import net.sashakyotoz.anitexlib.registries.ModParticleTypes;
import net.sashakyotoz.anitexlib.utils.render.RenderUtils;

import java.io.IOException;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class AniTexLibClientEvent {
    @SubscribeEvent
    public static void shaderRegistry(RegisterShadersEvent event) throws IOException {
        event.registerShader(new ShaderInstance(event.getResourceProvider(), new ResourceLocation(AniTexLib.MODID,"glowing"), DefaultVertexFormat.POSITION_COLOR),
                shader -> RenderUtils.GLOWING_SHADER = shader);
        event.registerShader(new ShaderInstance(event.getResourceProvider(), new ResourceLocation(AniTexLib.MODID,"glowing_particle"), DefaultVertexFormat.PARTICLE),
                shader -> RenderUtils.GLOWING_PARTICLE_SHADER = shader);
        event.registerShader(new ShaderInstance(event.getResourceProvider(), new ResourceLocation(AniTexLib.MODID,"sprite_particle"), DefaultVertexFormat.PARTICLE),
                shader -> RenderUtils.SPRITE_PARTICLE_SHADER = shader);
        event.registerShader(new ShaderInstance(event.getResourceProvider(), new ResourceLocation(AniTexLib.MODID,"fluid"), DefaultVertexFormat.PARTICLE),
                shader -> RenderUtils.FLUID_SHADER = shader);
    }

    @SubscribeEvent
    public static void onParticleSetup(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticleTypes.SPARK_LIKE_PARTICLE.get(), SparkleLikeParticle::provider);
        event.registerSpriteSet(ModParticleTypes.WISP_LIKE_PARTICLE.get(),WispLikeParticle::provider);
    }
}
