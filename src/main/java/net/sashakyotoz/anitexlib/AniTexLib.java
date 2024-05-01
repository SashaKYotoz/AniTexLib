package net.sashakyotoz.anitexlib;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.sashakyotoz.anitexlib.utils.TextureAnimator;
import org.slf4j.Logger;

@Mod(AniTexLib.MODID)
public class AniTexLib {
    public static final String MODID = "anitexlib";
    private static final Logger LOGGER = LogUtils.getLogger();

    public AniTexLib() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, net.sashakyotoz.anitexlib.ModConfig.SPEC);
        TextureAnimator.addEntityToAnimate(AniTexLib.class,MODID,"entity/pig_animated","pig_animated");
        MinecraftForge.EVENT_BUS.register(this);
    }
    public static void informUser(String s,boolean isError){
        if (isError)
            LOGGER.error("\u001B[31m" + "AniTexLib informs: " + s + "\u001B[0m");
        else
            LOGGER.debug("AniTexLib informs: " + s);
    }
}
