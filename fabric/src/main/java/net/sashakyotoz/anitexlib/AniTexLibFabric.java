package net.sashakyotoz.anitexlib;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.sashakyotoz.anitexlib.api.common.AniTexLibRegs;

public class AniTexLibFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        AniTexLib.init();

        AniTexLibRegs.Particles.registerAll((id, type) ->
                Registry.register(BuiltInRegistries.PARTICLE_TYPE, id, type)
        );
    }
}
