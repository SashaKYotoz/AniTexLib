package net.sashakyotoz.anitexlib.api.common.texture;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.sashakyotoz.anitexlib.Constants;
import net.sashakyotoz.anitexlib.api.common.AniTexLibRegs;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;

@ApiStatus.Internal
public class TextureJsonCollector {
    public static final TextureJsonCollector INSTANCE = new TextureJsonCollector();

    /**
     * The method that gathers json files out of ani_textures folder
     * @param manager ResourceManager provided by {@link net.sashakyotoz.anitexlib.mixin.common.MultiPackResourceManagerMixin}
     */
    public void preload(ResourceManager manager) {
        AniTexLibRegs.TEXTURE_ANIMATOR.clear();

        for (Map.Entry<ResourceLocation, Resource> entry : manager.listResources("ani_textures",
                path -> path.toString().endsWith(".json")).entrySet()) {

            try {
                String content = new String(entry.getValue().open().readAllBytes());
                JsonElement json = new Gson().fromJson(content, JsonElement.class);

                if (json == null || !json.isJsonObject()) continue;

                AniTexLibRegs.TEXTURE_ANIMATOR.read(json.getAsJsonObject());
            } catch (Throwable e) {
                Constants.LOG.debug("Could not read data file {}", entry.getKey());
            }
        }
    }
}