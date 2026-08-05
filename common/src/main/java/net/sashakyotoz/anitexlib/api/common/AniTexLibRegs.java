package net.sashakyotoz.anitexlib.api.common;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.anitexlib.Constants;
import net.sashakyotoz.anitexlib.api.client.particle.options.CircleParticleOption;
import net.sashakyotoz.anitexlib.api.client.particle.options.ColorableParticleOption;
import net.sashakyotoz.anitexlib.api.client.particle.options.WaveParticleOption;
import net.sashakyotoz.anitexlib.api.common.texture.TextureAnimator;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class AniTexLibRegs {
    public static final TextureAnimator TEXTURE_ANIMATOR = TextureAnimator.INSTANCE;

    public static class Particles {
        public static final ParticleType<ColorableParticleOption> SPARK_LIKE_PARTICLE = create(true, ColorableParticleOption.DESERIALIZER, type -> ColorableParticleOption.CODEC);
        public static final ParticleType<ColorableParticleOption> WISP_LIKE_PARTICLE = create(true, ColorableParticleOption.DESERIALIZER, type -> ColorableParticleOption.CODEC);
        public static final ParticleType<WaveParticleOption> WAVE_LIKE_PARTICLE = create(true, WaveParticleOption.DESERIALIZER, type -> WaveParticleOption.CODEC);
        public static final ParticleType<CircleParticleOption> CIRCLE_LIKE_PARTICLE = create(true, CircleParticleOption.DESERIALIZER, type -> CircleParticleOption.CODEC);
        public static final ParticleType<ColorableParticleOption> CUBE_LIKE_PARTICLE = create(true, ColorableParticleOption.DESERIALIZER, type -> ColorableParticleOption.CODEC);

        private static <T extends ParticleOptions> ParticleType<T> create(
                boolean overrideLimiter,
                ParticleOptions.Deserializer<T> deserializer,
                Function<ParticleType<T>, Codec<T>> codecFactory
        ) {
            return new ParticleType<>(overrideLimiter, deserializer) {
                @Override
                public Codec<T> codec() {
                    return codecFactory.apply(this);
                }
            };
        }

        public static void registerAll(BiConsumer<ResourceLocation, ParticleType<?>> registrar) {
            registrar.accept(Constants.makeId("sparkle_like"), SPARK_LIKE_PARTICLE);
            registrar.accept(Constants.makeId("wisp_like"), WISP_LIKE_PARTICLE);
            registrar.accept(Constants.makeId("wave_like"), WAVE_LIKE_PARTICLE);
            registrar.accept(Constants.makeId("circle_like"), CIRCLE_LIKE_PARTICLE);
            registrar.accept(Constants.makeId("cube_like"), CUBE_LIKE_PARTICLE);
        }
    }
}