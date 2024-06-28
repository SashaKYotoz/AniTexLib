package net.sashakyotoz.anitexlib.registries;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sashakyotoz.anitexlib.AniTexLib;
import net.sashakyotoz.anitexlib.client.particles.parents.types.WaveParticleOption;

import java.util.function.Function;

public class ModParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, AniTexLib.MODID);
    public static final RegistryObject<SimpleParticleType> SPARK_LIKE_PARTICLE = PARTICLE_TYPES.register("sparkle_like", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> WISP_LIKE_PARTICLE = PARTICLE_TYPES.register("wisp_like", () -> new SimpleParticleType(true));
    public static final RegistryObject<ParticleType<WaveParticleOption>> WAVE_LIKE_PARTICLE = registerParticle("wave_like",true,WaveParticleOption.DESERIALIZER,(particleType)-> WaveParticleOption.CODEC);

    public static <T extends ParticleOptions> RegistryObject<ParticleType<T>> registerParticle(String pKey, boolean pOverrideLimiter, ParticleOptions.Deserializer<T> pDeserializer, final Function<ParticleType<T>, Codec<T>> pCodecFactory) {
        return PARTICLE_TYPES.register(pKey, () -> new ParticleType<T>(pOverrideLimiter, pDeserializer) {
            @Override
            public Codec<T> codec() {
                return pCodecFactory.apply(this);
            }
        });
    }
}