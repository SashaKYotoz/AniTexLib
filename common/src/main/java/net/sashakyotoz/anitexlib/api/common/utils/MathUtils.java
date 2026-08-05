package net.sashakyotoz.anitexlib.api.common.utils;

import net.minecraft.util.Mth;

public class MathUtils {
    public static double getOscillatingValue(int tickCount,int periodInSeconds) {
        double phase = (2 * Math.PI * (tickCount % (periodInSeconds*20))) / (periodInSeconds*20);
        return 0.5 * (1 + Math.sin(phase));
    }
    public static float simpleAlphaFunction(float intensity, float pAgeInTicks) {
        intensity = intensity <= 0 ? 0.1F : intensity;
        return Math.max(0.0F, Mth.cos(pAgeInTicks * intensity) * 0.5F);
    }
}