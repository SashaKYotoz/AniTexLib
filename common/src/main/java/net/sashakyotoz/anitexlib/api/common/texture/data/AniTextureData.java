package net.sashakyotoz.anitexlib.api.common.texture.data;

import net.minecraft.resources.ResourceLocation;

public record AniTextureData(ResourceLocation location, int amountOfFrames, int animationInterval, boolean isRepeatable, long startTime) { }
