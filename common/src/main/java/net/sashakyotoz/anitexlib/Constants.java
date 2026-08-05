package net.sashakyotoz.anitexlib;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {
    public static final String MOD_ID = "anitexlib";
    public static final String MOD_NAME = "AniTexLib";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static ResourceLocation makeId(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
    public static ModelResourceLocation makeModelId(String path) {
        return new ModelResourceLocation(makeId(path), "standalone");
    }
}