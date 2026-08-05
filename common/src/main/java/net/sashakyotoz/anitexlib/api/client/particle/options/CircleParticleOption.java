package net.sashakyotoz.anitexlib.api.client.particle.options;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.sashakyotoz.anitexlib.api.common.AniTexLibRegs;

import java.util.Locale;

public record CircleParticleOption(float criticalAngle, float scale, float redColor, float greenColor,
                                   float blueColor) implements ParticleOptions {
    public static final Codec<CircleParticleOption> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("criticalAngle").forGetter(CircleParticleOption::criticalAngle),
            Codec.FLOAT.fieldOf("scale").forGetter(CircleParticleOption::scale),
            Codec.FLOAT.fieldOf("redColor").forGetter(CircleParticleOption::redColor),
            Codec.FLOAT.fieldOf("greenColor").forGetter(CircleParticleOption::greenColor),
            Codec.FLOAT.fieldOf("blueColor").forGetter(CircleParticleOption::blueColor)
    ).apply(instance, CircleParticleOption::new));
    public static final Deserializer<CircleParticleOption> DESERIALIZER = new Deserializer<>() {
        public CircleParticleOption fromCommand(ParticleType<CircleParticleOption> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float r = reader.readFloat();
            reader.expect(' ');
            float s = reader.readFloat();
            reader.expect(' ');
            float f = reader.readFloat();
            reader.expect(' ');
            float f1 = reader.readFloat();
            reader.expect(' ');
            float f2 = reader.readFloat();
            return new CircleParticleOption(r, s, f, f1, f2);
        }

        public CircleParticleOption fromNetwork(ParticleType<CircleParticleOption> type, FriendlyByteBuf byteBuf) {
            CompoundTag tag = byteBuf.readNbt();
            float roll = tag.getFloat("criticalAngle");
            float scale = tag.getFloat("scale");
            float r = tag.getFloat("red");
            float g = tag.getFloat("green");
            float b = tag.getFloat("blue");
            return new CircleParticleOption(roll, scale, r, g, b);
        }
    };

    public void writeToNetwork(FriendlyByteBuf pBuffer) {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("criticalAngle", this.criticalAngle);
        tag.putFloat("scale", this.scale);
        tag.putFloat("red", this.redColor);
        tag.putFloat("green", this.greenColor);
        tag.putFloat("blue", this.blueColor);
        pBuffer.writeNbt(tag);
    }

    public String writeToString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f", BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()), criticalAngle, scale, redColor, greenColor, blueColor);
    }

    public ParticleType<CircleParticleOption> getType() {
        return AniTexLibRegs.Particles.CIRCLE_LIKE_PARTICLE;
    }
}