package net.sashakyotoz.anitexlib;

import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.sashakyotoz.anitexlib.api.common.AniTexLibRegs;

@Mod(Constants.MOD_ID)
public class AniTexLibNeoForge {
    
    public AniTexLibNeoForge(IEventBus modBus) {
        AniTexLib.init();

        modBus.addListener(this::onRegister);
    }

    private void onRegister(RegisterEvent event) {
        event.register(Registries.PARTICLE_TYPE, helper -> AniTexLibRegs.Particles.registerAll(helper::register));
    }
}