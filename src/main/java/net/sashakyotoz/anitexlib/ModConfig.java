package net.sashakyotoz.anitexlib;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.BooleanValue TO_SHOW_EXAMPLE;
    public static final ForgeConfigSpec.BooleanValue USE_ADVANCED_PARTICLE_RENDERER;
    static {
        BUILDER.push("Configs for AniTexLib");
        TO_SHOW_EXAMPLE = BUILDER.comment("Determine if example texture animation must be shown").define("to_show_example",false);
        USE_ADVANCED_PARTICLE_RENDERER = BUILDER.comment("Determine if particles must use advanced render system").define("use_advanced_particle_renderer",true);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
