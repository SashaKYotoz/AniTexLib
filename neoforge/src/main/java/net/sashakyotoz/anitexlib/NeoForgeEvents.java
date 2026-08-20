package net.sashakyotoz.anitexlib;

import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.sashakyotoz.anitexlib.api.common.texture.TextureJsonCollector;

@EventBusSubscriber
public class NeoForgeEvents {
    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener((ResourceManagerReloadListener) TextureJsonCollector.INSTANCE::preload);
    }
}