package net.sashakyotoz.anitexlib.registries;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sashakyotoz.anitexlib.AniTexLib;
import net.sashakyotoz.anitexlib.client.particles.parents.options.CircleParticleOption;
import net.sashakyotoz.anitexlib.client.particles.parents.options.ColorableParticleOption;
import net.sashakyotoz.anitexlib.client.particles.parents.options.WaveParticleOption;

import java.util.function.Function;

public class ModParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, AniTexLib.MODID);
    public static final RegistryObject<ParticleType<ColorableParticleOption>> SPARK_LIKE_PARTICLE = registerParticle("sparkle_like", true,ColorableParticleOption.DESERIALIZER,(particleType)->ColorableParticleOption.CODEC);
    public static final RegistryObject<ParticleType<ColorableParticleOption>> WISP_LIKE_PARTICLE = registerParticle("wisp_like", true,ColorableParticleOption.DESERIALIZER,(particleType)->ColorableParticleOption.CODEC);
    public static final RegistryObject<ParticleType<WaveParticleOption>> WAVE_LIKE_PARTICLE = registerParticle("wave_like",true,WaveParticleOption.DESERIALIZER,(particleType)-> WaveParticleOption.CODEC);
    public static final RegistryObject<ParticleType<CircleParticleOption>> CIRCLE_LIKE_PARTICLE = registerParticle("circle_like",true,CircleParticleOption.DESERIALIZER,(particleType)-> CircleParticleOption.CODEC);
    public static final RegistryObject<ParticleType<ColorableParticleOption>> CUBE_LIKE_PARTICLE = registerParticle("cube_like",true,ColorableParticleOption.DESERIALIZER,(particleType)-> ColorableParticleOption.CODEC);

    public static <T extends ParticleOptions> RegistryObject<ParticleType<T>> registerParticle(String pKey, boolean pOverrideLimiter, ParticleOptions.Deserializer<T> pDeserializer, final Function<ParticleType<T>, Codec<T>> pCodecFactory) {
        return PARTICLE_TYPES.register(pKey, () -> new ParticleType<>(pOverrideLimiter, pDeserializer) {
            @Override
            public Codec<T> codec() {
                return pCodecFactory.apply(this);
            }
        });
    }
}