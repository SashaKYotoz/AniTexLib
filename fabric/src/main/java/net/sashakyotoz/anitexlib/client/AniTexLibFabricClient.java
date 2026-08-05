package net.sashakyotoz.anitexlib.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.sashakyotoz.anitexlib.api.client.AniTexLibClientRegs;
import net.sashakyotoz.anitexlib.api.client.particle.ParticleModelRegistry;
import net.sashakyotoz.anitexlib.api.client.particle.custom.*;
import net.sashakyotoz.anitexlib.api.client.render.shader.ShaderHandler;
import net.sashakyotoz.anitexlib.api.common.AniTexLibRegs;
import net.sashakyotoz.anitexlib.platform.FabricRenderTypeRegistrar;

public class AniTexLibFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AniTexLibClientRegs.Types.registerTypes(new FabricRenderTypeRegistrar());

        CoreShaderRegistrationCallback.EVENT.register(context -> ShaderHandler.registerShaders(context::register));

        ModelLoadingPlugin.register(pluginContext -> ParticleModelRegistry.getAllModelLocations().forEach(pluginContext::addModels));

        ParticleFactoryRegistry.getInstance().register(AniTexLibRegs.Particles.SPARK_LIKE_PARTICLE, SparkleLikeParticle::provider);
        ParticleFactoryRegistry.getInstance().register(AniTexLibRegs.Particles.WISP_LIKE_PARTICLE, WispLikeParticle::provider);
        ParticleFactoryRegistry.getInstance().register(AniTexLibRegs.Particles.WAVE_LIKE_PARTICLE, WaveLikeParticle::provider);
        ParticleFactoryRegistry.getInstance().register(AniTexLibRegs.Particles.CIRCLE_LIKE_PARTICLE, CircleLikeParticle::provider);
        ParticleFactoryRegistry.getInstance().register(AniTexLibRegs.Particles.CUBE_LIKE_PARTICLE, CubeLikeParticle::provider);
    }
}