package net.sashakyotoz.anitexlib;

import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sashakyotoz.anitexlib.api.common.texture.TextureJsonCollector;

@Mod.EventBusSubscriber
public class ForgeEvents {
    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener((ResourceManagerReloadListener) TextureJsonCollector.INSTANCE::preload);
    }
}