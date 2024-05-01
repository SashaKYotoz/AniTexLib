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
        JsonObject object;
        if (!options.isEmpty() && options.stream().anyMatch(jsonObject -> jsonObject.get("base_texture_name").getAsString().equals(name))) {
            object = options.stream()
                    .filter(jsonObject -> jsonObject.get("base_texture_name").getAsString().equals(name))
                    .findFirst().get();
            frameValue.putIfAbsent(name, 0);
            locations.putIfAbsent(name, new ResourceLocation(modId, path + name + "0.png"));
            int interval = object.get("interval_of_animation").getAsInt();
            int framesAmount = object.get("amount_of_frames").getAsInt();
            if (workQueue.size() < 1)
                queueServerWork(interval, () -> {
                    frameValue.put(name, frameValue.get(name) < framesAmount - 1 ? frameValue.get(name) + 1 : 0);
                    locations.put(name, new ResourceLocation(modId, path + name + frameValue.get(name) + ".png"));
                });
            return locations.get(name) == null ? new ResourceLocation(modId, path + name + "0.png") : locations.get(name);
        } else
            return new ResourceLocation(modId, path + name + "0.png");
    }
    /*
     * Example of returnment for getTextureLocation() in entity renderer
     */
    //AnimateOptionsReader.getObjectWithoutUpdate(modId,pEntity.getUUID()) == null ? TextureAnimator.getManagedAnimatedTextureByName(modId,"","",0,20,4,pEntity.getUUID()) : TextureAnimator.getManagedAnimatedTextureByName(modId,null,null,null,null,null,null,pEntity.getUUID());

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
//        AniTexLib.informUser("Json Object: " + AnimateOptionsReader.manageDynamicJsonObject(modId,path,name,toAnimate,stopFrame,uuid), false);
        JsonObject object;
        if (path != null && name != null && toAnimate != null && stopFrame != null && interval != null && countOfFrames != null) {
            AnimateOptionsReader.manageDynamicJsonObject(modId, path, name, toAnimate, stopFrame, interval, countOfFrames, uuid);
            return new ResourceLocation(modId, path + name + "0.png");
        } else {
            frameValue.putIfAbsent(uuid.toString(), 0);
            object = AnimateOptionsReader.getObjectWithoutUpdate(modId, uuid);
            if (object != null) {
                int localStopFrame = object.get("stopFrame").getAsInt();
                int localInterval = object.get("interval_of_animation").getAsInt();
                int framesAmount = object.get("amount_of_frames").getAsInt();
                boolean haveToContinueAnimation = object.get("haveToContinueAnimation").getAsBoolean();
                String localNameOfTexture = object.get("nameOfTexture").getAsString();
                String localTextureFolder = object.get("textureFolder").getAsString();
                locations.putIfAbsent(uuid.toString(), new ResourceLocation(modId, localTextureFolder + localNameOfTexture + frameValue.get(uuid.toString()) + ".png"));
                if (workQueue.size() < 1 && haveToContinueAnimation) {
                    if (localStopFrame > 0)
                        queueServerWork(localInterval, () -> {
                            frameValue.put(uuid.toString(), frameValue.get(uuid.toString()) < localStopFrame - 1 ? frameValue.get(uuid.toString()) + 1 : localStopFrame);
                            locations.put(uuid.toString(), new ResourceLocation(modId, localTextureFolder + localNameOfTexture + frameValue.get(uuid.toString()) + ".png"));
                        });
                    else
                        queueServerWork(localInterval, () -> {
                            frameValue.put(uuid.toString(), frameValue.get(uuid.toString()) < framesAmount - 1 ? frameValue.get(uuid.toString()) + 1 : 0);
                            locations.put(uuid.toString(), new ResourceLocation(modId, localTextureFolder + localNameOfTexture + frameValue.get(uuid.toString()) + ".png"));
                        });
                }

                return locations.get(uuid.toString()) == null ? new ResourceLocation(modId, localTextureFolder + localNameOfTexture + "0.png") : locations.get(uuid.toString());
            } else
                return null;
        }
    }

    private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

    public static void queueServerWork(int tick, Runnable action) {
        workQueue.add(new AbstractMap.SimpleEntry<>(action, tick));
    }

    @SubscribeEvent
    public static void tick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            List<AbstractMap.SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();
            workQueue.forEach(work -> {
                work.setValue(work.getValue() - 1);
                if (work.getValue() == 0)
                    actions.add(work);
            });
            actions.forEach(e -> e.getKey().run());
            workQueue.removeAll(actions);
        }
    }
}
