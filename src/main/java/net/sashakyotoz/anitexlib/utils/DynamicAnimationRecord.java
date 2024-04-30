package net.sashakyotoz.anitexlib.utils;

import java.util.UUID;

public record DynamicAnimationRecord(String modId, String textureFolder, String nameOfTexture, boolean condition, int stopFrame, UUID entityUUID) {
}
