package net.sashakyotoz.anitexlib.api.client.particle;

import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.anitexlib.Constants;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Internal
public class ParticleModelRegistry {
    public static final ResourceLocation CIRCLE_MODEL = Constants.makeId("particle/circle");
    public static final ResourceLocation CUBE_MODEL = Constants.makeId("particle/cube");
    public static final ResourceLocation WAVE_MODEL = Constants.makeId("particle/wave");

    public static List<ResourceLocation> getAllModelLocations() {
        return List.of(CIRCLE_MODEL, CUBE_MODEL, WAVE_MODEL);
    }
}