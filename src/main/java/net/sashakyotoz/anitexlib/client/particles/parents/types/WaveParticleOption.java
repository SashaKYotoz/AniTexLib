package net.sashakyotoz.anitexlib.client.particles.parents.types;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.registries.ForgeRegistries;
import net.sashakyotoz.anitexlib.registries.ModParticleTypes;

import java.util.Locale;

public record WaveParticleOption(float roll,float scale,float redColor,float greenColor,float blueColor) implements ParticleOptions {
    public static final Codec<WaveParticleOption> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("roll").forGetter(WaveParticleOption::roll),
            Codec.FLOAT.fieldOf("scale").forGetter(WaveParticleOption::scale),
            Codec.FLOAT.fieldOf("redColor").forGetter(WaveParticleOption::redColor),
            Codec.FLOAT.fieldOf("greenColor").forGetter(WaveParticleOption::greenColor),
            Codec.FLOAT.fieldOf("blueColor").forGetter(WaveParticleOption::blueColor)
    ).apply(instance, WaveParticleOption::new));
    public static final ParticleOptions.Deserializer<WaveParticleOption> DESERIALIZER = new ParticleOptions.Deserializer<>() {
        public WaveParticleOption fromCommand(ParticleType<WaveParticleOption> type, StringReader reader) throws CommandSyntaxException {
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
            return new WaveParticleOption(r,s,f, f1, f2);
        }

        public WaveParticleOption fromNetwork(ParticleType<WaveParticleOption> type, FriendlyByteBuf byteBuf) {
            CompoundTag tag = byteBuf.readNbt();
            float roll = tag.getFloat("roll");
            float scale = tag.getFloat("scale");
            float r = tag.getFloat("red");
            float g = tag.getFloat("green");
            float b = tag.getFloat("blue");
            return new WaveParticleOption(roll,scale,r, g, b);
        }
    };

    public void writeToNetwork(FriendlyByteBuf pBuffer) {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("roll", this.roll);
        tag.putFloat("scale", this.scale);
        tag.putFloat("red", this.redColor);
        tag.putFloat("green", this.greenColor);
        tag.putFloat("blue", this.blueColor);
        pBuffer.writeNbt(tag);
    }

    public String writeToString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f", ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()),roll,scale, redColor, greenColor, blueColor);
    }

    public ParticleType<WaveParticleOption> getType() {
        return ModParticleTypes.WAVE_LIKE_PARTICLE.get();
    }
}