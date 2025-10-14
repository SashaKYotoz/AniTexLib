package net.sashakyotoz.anitexlib.client.particles.parents.options;

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

public record ColorableParticleOption(String type, float redColor, float greenColor,
                                      float blueColor) implements ParticleOptions {
    public static final Codec<ColorableParticleOption> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("type").forGetter(ColorableParticleOption::type),
            Codec.FLOAT.fieldOf("redColor").forGetter(ColorableParticleOption::redColor),
            Codec.FLOAT.fieldOf("greenColor").forGetter(ColorableParticleOption::greenColor),
            Codec.FLOAT.fieldOf("blueColor").forGetter(ColorableParticleOption::blueColor)
    ).apply(instance, ColorableParticleOption::new));

    @Override
    public ParticleType<?> getType() {
        return switch (type) {
            case "sparkle" -> ModParticleTypes.SPARK_LIKE_PARTICLE.get();
            case "cube" -> ModParticleTypes.CUBE_LIKE_PARTICLE.get();
            default -> ModParticleTypes.WISP_LIKE_PARTICLE.get();
        };
    }

    public static final ParticleOptions.Deserializer<ColorableParticleOption> DESERIALIZER = new ParticleOptions.Deserializer<>() {
        public ColorableParticleOption fromCommand(ParticleType<ColorableParticleOption> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            String t = reader.readString();
            reader.expect(' ');
            float f = reader.readFloat();
            reader.expect(' ');
            float f1 = reader.readFloat();
            reader.expect(' ');
            float f2 = reader.readFloat();
            return new ColorableParticleOption(t, f, f1, f2);
        }

        public ColorableParticleOption fromNetwork(ParticleType<ColorableParticleOption> type, FriendlyByteBuf byteBuf) {
            CompoundTag tag = byteBuf.readNbt();
            String pType = tag.getString("type");
            float r = tag.getFloat("red");
            float g = tag.getFloat("green");
            float b = tag.getFloat("blue");
            return new ColorableParticleOption(pType, r, g, b);
        }
    };

    public void writeToNetwork(FriendlyByteBuf pBuffer) {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", this.type);
        tag.putFloat("red", this.redColor);
        tag.putFloat("green", this.greenColor);
        tag.putFloat("blue", this.blueColor);
        pBuffer.writeNbt(tag);
    }

    public String writeToString() {
        return String.format(Locale.ROOT, "%s %s %.2f %.2f %.2f", ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()), type, redColor, greenColor, blueColor);
    }
}