package net.sashakyotoz.anitexlib.client;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.sashakyotoz.anitexlib.api.client.AniTexLibClientRegs;
import net.sashakyotoz.anitexlib.api.client.particle.ParticleModelRegistry;
import net.sashakyotoz.anitexlib.api.client.particle.custom.*;
import net.sashakyotoz.anitexlib.api.client.render.shader.ShaderHandler;
import net.sashakyotoz.anitexlib.api.common.AniTexLibRegs;
import net.sashakyotoz.anitexlib.platform.NeoForgeRenderTypeRegistrar;

@EventBusSubscriber(value = Dist.CLIENT)
public class AniTexLibNeoForgeClient {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> AniTexLibClientRegs.Types.registerTypes(new NeoForgeRenderTypeRegistrar()));
    }

    @SubscribeEvent
    public static void onRegisterShaders(RegisterShadersEvent event) {
        ShaderHandler.registerShaders(
                (id, vertexFormat, onLoaded) -> event.registerShader(
                        new net.minecraft.client.renderer.ShaderInstance(event.getResourceProvider(), id, vertexFormat),
                        onLoaded
                )
        );
    }

    @SubscribeEvent
    public static void onModelRegister(ModelEvent.RegisterAdditional event) {
        ParticleModelRegistry.getAllModelLocations().forEach(loc ->
                event.register(new ModelResourceLocation(loc, "standalone"))
        );
    }

    @SubscribeEvent
    public static void onParticleSetup(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(AniTexLibRegs.Particles.SPARK_LIKE_PARTICLE, SparkleLikeParticle::provider);
        event.registerSpriteSet(AniTexLibRegs.Particles.WISP_LIKE_PARTICLE, WispLikeParticle::provider);
        event.registerSpriteSet(AniTexLibRegs.Particles.WAVE_LIKE_PARTICLE, WaveLikeParticle::provider);
        event.registerSpriteSet(AniTexLibRegs.Particles.CIRCLE_LIKE_PARTICLE, CircleLikeParticle::provider);
        event.registerSpriteSet(AniTexLibRegs.Particles.CUBE_LIKE_PARTICLE, CubeLikeParticle::provider);
    }
}