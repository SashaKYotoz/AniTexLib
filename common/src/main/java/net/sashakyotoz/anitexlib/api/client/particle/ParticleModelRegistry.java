package net.sashakyotoz.anitexlib.api.client.particle;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.anitexlib.Constants;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Internal
public class ParticleModelRegistry {
    public static final ModelResourceLocation CIRCLE_MODEL = new ModelResourceLocation(Constants.makeId("particle/circle"), "inventory");
    public static final ModelResourceLocation CUBE_MODEL = new ModelResourceLocation(Constants.makeId("particle/cube"), "inventory");
    public static final ModelResourceLocation WAVE_MODEL = new ModelResourceLocation(Constants.makeId("particle/wave"), "inventory");

    public static List<ResourceLocation> getAllModelLocations() {
        return List.of(
                CIRCLE_MODEL,
                CUBE_MODEL,
                WAVE_MODEL
        );
    }
}