package net.sashakyotoz.anitexlib;

import com.mojang.logging.LogUtils;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sashakyotoz.anitexlib.registries.ModParticleTypes;
import net.sashakyotoz.anitexlib.utils.ExampleItem;
import net.sashakyotoz.anitexlib.utils.TextureAnimator;
import net.sashakyotoz.anitexlib.utils.render.RenderTypesHandler;
import org.slf4j.Logger;

@Mod(AniTexLib.MODID)
public class AniTexLib {
    public static final String MODID = "anitexlib";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final RegistryObject<Item> EXAMPLE_ITEM = ITEMS.register("example_item",()->new ExampleItem(new Item.Properties()));

    public AniTexLib() {
        MinecraftForge.EVENT_BUS.register(this);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, net.sashakyotoz.anitexlib.ModConfig.SPEC);
        TextureAnimator.addEntityToAnimate(AniTexLib.class,MODID,"entity/pig_animated","pig_animated");
        ModParticleTypes.PARTICLE_TYPES.register(bus);
        ITEMS.register(bus);
        DistExecutor.unsafeCallWhenOn(Dist.CLIENT,()->()->{
            forgeBus.addListener(RenderTypesHandler::onRenderWorldLast);
            return new Object();
        });
    }
    public static void informUser(String s,boolean isError){
        if (isError)
            LOGGER.error("\u001B[31mAniTexLib informs: {}\u001B[0m", s);
        else
            LOGGER.debug("AniTexLib informs: {}", s);
    }
}