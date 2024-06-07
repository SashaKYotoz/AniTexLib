package net.sashakyotoz.anitexlib.registries;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sashakyotoz.anitexlib.AniTexLib;

public class ModParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, AniTexLib.MODID);
    public static final RegistryObject<SimpleParticleType> SPARK_LIKE_PARTICLE = PARTICLE_TYPES.register("sparkle_like", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> WISP_LIKE_PARTICLE = PARTICLE_TYPES.register("wisp_like", () -> new SimpleParticleType(true));
}
