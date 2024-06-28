package net.sashakyotoz.anitexlib.utils;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sashakyotoz.anitexlib.AniTexLib;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Mod.EventBusSubscriber
public class TextureAnimator {
    public static ArrayList<JsonObject> options = new ArrayList<>();
    private static final HashMap<String, ResourceLocation> locations = new HashMap<>();
    private static final HashMap<String, Integer> frameValue = new HashMap<>();
    private static final Queue<AnimationTask> workQueue = new ConcurrentLinkedQueue<>();
    /**
     * Object mainObject - main class of your mod, (f.e. ExampleMod.class)
     * <p>
     * String modId - modid of your mod, (f.e. "examplemod")
     * <p>
     * String textureFolder - location of your entities textures
     * <p>
     * String nameOfTexture - name of texture file, (f.e. "examplemod")
     * <p>
     * Register new mob texture to animate in public constructor of your main mod class (f.e. ExampleMod.class)
     */
    public static void addEntityToAnimate(Class<?> aClass, String modId, String textureFolder, String nameOfTexture) {
        JsonObject jsonObject = AnimateOptionsReader.getObjectOfTexturesSet(aClass, modId, textureFolder, nameOfTexture);
        if (jsonObject != null) {
            options.add(jsonObject);
            AniTexLib.informUser("JsonObject was added", false);
        }
    }
    /**
     * String modId - modid of your mod, (f.e. "examplemod")
     * <p>
     * String path - location of your entities textures, (f.e. "entity/pig_animated")
     * <p>
     * String name - name of texture file, (f.e. "pig_animated"), name must be unique for each type of entity, don't put .png after name!
     * <p>
     * !--Names of texture files have to be ordered to be animated correctly (f.e. pig_animated0.png, pig_animated1.png and etc..)--!
     *
     * @return ResourceLocation of texture in textures' set with its current index
     */
    public static ResourceLocation getAnimatedTextureByName(String modId, String path, String name) {
        if (!options.isEmpty() && options.stream().anyMatch(jsonObject -> jsonObject.get("base_texture_name").getAsString().equals(name))) {
            JsonObject object = options.stream()
                    .filter(jsonObject -> jsonObject.get("base_texture_name").getAsString().equals(name))
                    .findFirst()
                    .orElseThrow();
            frameValue.putIfAbsent(name, 0);
            locations.putIfAbsent(name, new ResourceLocation(modId, path + name + "0.png"));
            int interval = object.get("interval_of_animation").getAsInt();
            int framesAmount = object.get("amount_of_frames").getAsInt();

            queueAnimationTask(name, interval, framesAmount, modId, path);
            return locations.getOrDefault(name, new ResourceLocation(modId, path + name + "0.png"));
        } else {
            return new ResourceLocation(modId, path + name + "0.png");
        }
    }
    /**
     * String modId - modid of your mod, (f.e. "examplemod")
     * <p>
     * String path - location of your entities textures, (f.e. "entity/pig_animated")
     * <p>
     * String name - name of texture file, (f.e. "pig_animated"), name must be unique for each type of entity, don't put .png after name!
     * <p>
     * !--Names of texture files have to be ordered to be animated correctly (f.e. pig_animated0.png, pig_animated1.png and etc..)--!
     *
     * @return ResourceLocation of texture in textures' set with its current index
     */
    public static ResourceLocation getManagedAnimatedTextureByName(String modId, @Nullable String path, @Nullable String name, @Nullable Boolean toAnimate, @Nullable Integer stopFrame, @Nullable Integer interval, @Nullable Integer countOfFrames, UUID uuid) {
        if (path != null && name != null && toAnimate != null && stopFrame != null && interval != null && countOfFrames != null) {
            AnimateOptionsReader.manageDynamicJsonObject(modId, path, name, toAnimate, stopFrame, interval, countOfFrames, uuid);
            return new ResourceLocation(modId, path + name + "0.png");
        } else {
            frameValue.putIfAbsent(uuid.toString(), 0);
            JsonObject object = AnimateOptionsReader.getObjectWithoutUpdate(modId, uuid);

            if (object != null) {
                int localStopFrame = object.get("stopFrame").getAsInt();
                int localInterval = object.get("interval_of_animation").getAsInt();
                int framesAmount = object.get("amount_of_frames").getAsInt();
                boolean haveToContinueAnimation = object.get("haveToContinueAnimation").getAsBoolean();
                String localNameOfTexture = object.get("nameOfTexture").getAsString();
                String localTextureFolder = object.get("textureFolder").getAsString();
                locations.putIfAbsent(uuid.toString(), new ResourceLocation(modId, localTextureFolder + localNameOfTexture + frameValue.get(uuid.toString()) + ".png"));

                if (haveToContinueAnimation)
                    queueAnimationTask(uuid.toString(), localInterval, localStopFrame > 0 ? localStopFrame : framesAmount, modId, localTextureFolder);

                return locations.getOrDefault(uuid.toString(), new ResourceLocation(modId, localTextureFolder + localNameOfTexture + "0.png"));
            } else {
                return null;
            }
        }
    }

    private static void queueAnimationTask(String name, int interval, int framesAmount, String modId, String path) {
        if (workQueue.stream().noneMatch(task -> task.name.equals(name))) {
            workQueue.add(new AnimationTask(name, interval, framesAmount, modId, path));
        }
    }

    @SubscribeEvent
    public static void tick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            List<AnimationTask> completedTasks = new ArrayList<>();

            for (AnimationTask task : workQueue) {
                task.remainingTicks--;
                if (task.remainingTicks <= 0) {
                    frameValue.put(task.name, frameValue.get(task.name) < task.framesAmount - 1 ? frameValue.get(task.name) + 1 : 0);
                    locations.put(task.name, new ResourceLocation(task.modId, task.path + task.name + frameValue.get(task.name) + ".png"));
                    task.remainingTicks = task.interval;

                    if (frameValue.get(task.name) == task.framesAmount - 1) {
                        completedTasks.add(task);
                    }
                }
            }
            workQueue.removeAll(completedTasks);
        }
    }

    private static class AnimationTask {
        String name;
        int interval;
        int framesAmount;
        String modId;
        String path;
        int remainingTicks;

        AnimationTask(String name, int interval, int framesAmount, String modId, String path) {
            this.name = name;
            this.interval = interval;
            this.framesAmount = framesAmount;
            this.modId = modId;
            this.path = path;
            this.remainingTicks = interval;
        }
    }
}