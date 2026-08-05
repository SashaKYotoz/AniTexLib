package net.sashakyotoz.anitexlib.api.common.texture;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.anitexlib.Constants;
import net.sashakyotoz.anitexlib.api.common.texture.data.AniTextureData;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextureAnimator {
    public static final TextureAnimator INSTANCE = new TextureAnimator();
    private static final Map<ResourceLocation, AniTextureData> ANI_TEXTURES = new ConcurrentHashMap<>();

    private static final Pattern FRAME_PATTERN = Pattern.compile("^(.*?)(_?)(\\d+)(\\.png)?$");

    private TextureAnimator() {
    }

    public void clear() {
        ANI_TEXTURES.clear();
    }

    /**
     * Registers a new ani_texture manually instead of automatic json file parser
     * @param location The ResourceLocation of the first frame to animate from
     * @param amountOfFrames An amount of frames to use for animation (e.g. _0.png _1.png .. _n.png)
     * @param animationInterval An animation interval in ticks to switch frame to another
     * @param isRepeatable An option to make animation one time only
     */
    public void register(ResourceLocation location, int amountOfFrames, int animationInterval, boolean isRepeatable) {
        if (ANI_TEXTURES.putIfAbsent(location, new AniTextureData(location, amountOfFrames, animationInterval, isRepeatable, System.currentTimeMillis())) != null)
            Constants.LOG.error("AniTextures with id: {} is already registered, skipping", location);
    }
    /**
     * Internal method to gather all created ani_textures into the registry
     */
    @ApiStatus.Internal
    public void read(JsonObject json) {
        if (!json.has("location")) return;
        ResourceLocation location = new ResourceLocation(json.get("location").getAsString());
        int amountOfFrames = json.get("amount_of_frames").getAsInt();
        int animationInterval = json.get("animation_interval").getAsInt();
        boolean isRepeatable = !json.has("is_repeatable") || json.get("is_repeatable").getAsBoolean();

        register(location, amountOfFrames, animationInterval, isRepeatable);
    }

    /**
     * Retrieves the current animated frame ResourceLocation based on system time.
     * Automatically formats the path to append or update the _frame sequence.
     * @param baseLocation The location of the first frame that was declared by {@link #register(ResourceLocation, int, int, boolean)} or json file in data/ani_textures directory
     */
    public ResourceLocation get(ResourceLocation baseLocation) {
        AniTextureData data = ANI_TEXTURES.get(baseLocation);
        if (data == null)
            return baseLocation;

        int currentFrame = calculateFrame(data.amountOfFrames(), data.animationInterval(), data.isRepeatable(), data.startTime());
        return formatResourceLocation(baseLocation, currentFrame);
    }

    private int calculateFrame(int framesAmount, int intervalInTicks, boolean isRepeatable, long startTime) {
        long intervalMs = intervalInTicks * 50L;
        long elapsedMs = System.currentTimeMillis() - startTime;

        long totalFramesPassed = elapsedMs / intervalMs;
        if (!isRepeatable && totalFramesPassed >= (framesAmount - 1)) {
            return framesAmount - 1;
        }

        return (int) (totalFramesPassed % framesAmount);
    }

    private ResourceLocation formatResourceLocation(ResourceLocation base, int currentFrame) {
        String originalPath = base.getPath();
        Matcher matcher = FRAME_PATTERN.matcher(originalPath);

        if (matcher.matches()) {
            String prefix = matcher.group(1);
            String separator = matcher.group(2);
            int baseIndex = Integer.parseInt(matcher.group(3));
            String extension = matcher.group(4) != null ? matcher.group(4) : "";
            int targetFrame = baseIndex + currentFrame;

            return new ResourceLocation(base.getNamespace(), prefix + separator + targetFrame + extension);
        }

        boolean hasPng = originalPath.endsWith(".png");
        String cleanPath = hasPng ? originalPath.substring(0, originalPath.length() - 4) : originalPath;
        return new ResourceLocation(base.getNamespace(), cleanPath + "_" + currentFrame + (hasPng ? ".png" : ""));
    }
}