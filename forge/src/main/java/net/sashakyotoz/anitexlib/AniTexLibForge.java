package net.sashakyotoz.anitexlib;

import net.minecraft.core.registries.Registries;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import net.sashakyotoz.anitexlib.api.common.AniTexLibRegs;

@Mod(Constants.MOD_ID)
public class AniTexLibForge {
    
    public AniTexLibForge() {
        AniTexLib.init();

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::onRegister);
    }

    private void onRegister(RegisterEvent event) {
        event.register(Registries.PARTICLE_TYPE, helper -> AniTexLibRegs.Particles.registerAll(helper::register));
    }
}