package net.sashakyotoz.anitexlib.api.client.particle.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.sashakyotoz.anitexlib.api.common.AniTexLibRegs;

public record WaveParticleOption(float roll,float scale,float redColor,float greenColor,float blueColor) implements ParticleOptions {
    public static final MapCodec<WaveParticleOption> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.FLOAT.fieldOf("criticalAngle").forGetter(WaveParticleOption::roll),
                    Codec.FLOAT.fieldOf("scale").forGetter(WaveParticleOption::scale),
                    Codec.FLOAT.fieldOf("redColor").forGetter(WaveParticleOption::redColor),
                    Codec.FLOAT.fieldOf("greenColor").forGetter(WaveParticleOption::greenColor),
                    Codec.FLOAT.fieldOf("blueColor").forGetter(WaveParticleOption::blueColor)
            ).apply(instance,WaveParticleOption::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, WaveParticleOption> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            WaveParticleOption::roll,
            ByteBufCodecs.FLOAT,
            WaveParticleOption::scale,
            ByteBufCodecs.FLOAT,
            WaveParticleOption::redColor,
            ByteBufCodecs.FLOAT,
            WaveParticleOption::greenColor,
            ByteBufCodecs.FLOAT,
            WaveParticleOption::blueColor,
            WaveParticleOption::new
    );

    public ParticleType<WaveParticleOption> getType() {
        return AniTexLibRegs.Particles.WAVE_LIKE_PARTICLE;
    }
}
