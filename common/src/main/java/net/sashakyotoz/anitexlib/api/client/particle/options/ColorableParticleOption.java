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

public record ColorableParticleOption(String type, float redColor, float greenColor,
                                      float blueColor) implements ParticleOptions {
    public static final MapCodec<ColorableParticleOption> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.STRING.fieldOf("type").forGetter(ColorableParticleOption::type),
                    Codec.FLOAT.fieldOf("redColor").forGetter(ColorableParticleOption::redColor),
                    Codec.FLOAT.fieldOf("greenColor").forGetter(ColorableParticleOption::greenColor),
                    Codec.FLOAT.fieldOf("blueColor").forGetter(ColorableParticleOption::blueColor)
            ).apply(instance, ColorableParticleOption::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ColorableParticleOption> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            ColorableParticleOption::type,
            ByteBufCodecs.FLOAT,
            ColorableParticleOption::redColor,
            ByteBufCodecs.FLOAT,
            ColorableParticleOption::greenColor,
            ByteBufCodecs.FLOAT,
            ColorableParticleOption::blueColor,
            ColorableParticleOption::new
    );

    @Override
    public ParticleType<?> getType() {
        return switch (type) {
            case "sparkle" -> AniTexLibRegs.Particles.SPARK_LIKE_PARTICLE;
            case "cube" -> AniTexLibRegs.Particles.CUBE_LIKE_PARTICLE;
            default -> AniTexLibRegs.Particles.WISP_LIKE_PARTICLE;
        };
    }
}