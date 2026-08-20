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

public record CircleParticleOption(float criticalAngle, float scale, float redColor, float greenColor,
                                   float blueColor) implements ParticleOptions {
    public static final MapCodec<CircleParticleOption> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.FLOAT.fieldOf("criticalAngle").forGetter(CircleParticleOption::criticalAngle),
                    Codec.FLOAT.fieldOf("scale").forGetter(CircleParticleOption::scale),
                    Codec.FLOAT.fieldOf("redColor").forGetter(CircleParticleOption::redColor),
                    Codec.FLOAT.fieldOf("greenColor").forGetter(CircleParticleOption::greenColor),
                    Codec.FLOAT.fieldOf("blueColor").forGetter(CircleParticleOption::blueColor)
            ).apply(instance,CircleParticleOption::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, CircleParticleOption> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            CircleParticleOption::criticalAngle,
            ByteBufCodecs.FLOAT,
            CircleParticleOption::scale,
            ByteBufCodecs.FLOAT,
            CircleParticleOption::redColor,
            ByteBufCodecs.FLOAT,
            CircleParticleOption::greenColor,
            ByteBufCodecs.FLOAT,
            CircleParticleOption::blueColor,
            CircleParticleOption::new
    );

    public ParticleType<CircleParticleOption> getType() {
        return AniTexLibRegs.Particles.CIRCLE_LIKE_PARTICLE;
    }
}