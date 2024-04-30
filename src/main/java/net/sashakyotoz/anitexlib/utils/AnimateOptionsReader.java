package net.sashakyotoz.anitexlib.utils;

import com.google.gson.*;
import net.minecraftforge.fml.loading.FMLPaths;
import net.sashakyotoz.anitexlib.AniTexLib;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class AnimateOptionsReader {
    protected static final Gson gson = new Gson();

    /**
     * Class mainClass - main class of your mod, (f.e. ExampleMod.class)
     * <p>
     * String modId - modid of your mod, (f.e. "examplemod")
     * <p>
     * String textureFolder - location of your entities textures
     * <p>
     * String nameOfTexture - name of texture file, (f.e. "example_wolf") without [.png]!
     * <p>
     * !--Names of texture files have to be ordered to be animated correctly (f.e. example_wolf0.png, example_wolf1.png and etc..)--!
     * <p>
     *
     * @return JsonObject of properties for animation of textures' set
     */
    protected static JsonObject getObjectOfTexturesSet(Class<?> mainClass, String modId, String textureFolder, String nameOfTexture) {
        AniTexLib.informUser("code gets jsonObject", false);
        String path = String.format("%s/assets/%s/textures/%s/%s.png.json", mainClass, modId, textureFolder, nameOfTexture);
        InputStream stream = mainClass.getResourceAsStream("/assets/" + modId + "/textures/" + textureFolder + "/" + nameOfTexture + ".png.json");
//        InputStream stream = AniTexLib.class.getResourceAsStream("/assets/" + AniTexLib.MODID + "/textures/" + "entity/pig_animated" + "/" + "pig_animated" + ".png.json");
        if (stream != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
                if (jsonObject != null) return jsonObject;
                else AniTexLib.informUser("JsonObject is null", true);
            } catch (IOException e) {
                AniTexLib.informUser(String.format("Impossible to read json with path %s", path), true);
            }
        } else
            AniTexLib.informUser(String.format("Impossible to find json with path %s", path), true);
        return null;
    }

    /**
     * String modId - modid of your mod, (f.e. "examplemod")
     * <p>
     * String textureFolder - location of your entities textures
     * <p>
     * UUID entityUUID - identifier of entity, its unique id (f.e. entity.getUUID())
     * <p>
     * String nameOfTexture - name of texture file, (f.e. "example_wolf") without [.png]!
     *
     * @return Dynamic jsonObject of properties for animation of textures' set with stopper and stop frame
     */
    protected static JsonObject manageDynamicJsonObject(String modId, String textureFolder, String nameOfTexture, boolean condition, int stopFrame, Integer interval, Integer countOfFrames, UUID entityUUID) {
        JsonObject jsonObject;
        AniTexLib.informUser("code gets dynamic jsonObject", false);
        Path configDir = FMLPaths.GAMEDIR.get().resolve(modId);
        Path CONTROLLERS_PATH = configDir.resolve("animations_controller.json");
        createOrUpdateJson(modId, textureFolder, nameOfTexture, condition, stopFrame,interval,countOfFrames, entityUUID);
        try {
            jsonObject = gson.fromJson(new String(Files.readAllBytes(CONTROLLERS_PATH)), JsonObject.class);
            if (jsonObject != null) return jsonObject;
        } catch (IOException e) {
            AniTexLib.informUser("Something got wrong with dynamic json parser", true);
        }
        return null;
    }

    public static JsonObject getObjectWithoutUpdate(String modId, UUID entityUUID) {
        try {
            Path configDir = FMLPaths.GAMEDIR.get().resolve(modId);
            if (!Files.exists(configDir))
                Files.createDirectories(configDir);
            Path CONTROLLERS_PATH = configDir.resolve("animations_controller.json");
            JsonObject mainObject;
            if (Files.exists(CONTROLLERS_PATH)) {
                String jsonString = new String(Files.readAllBytes(CONTROLLERS_PATH));
                mainObject = JsonParser.parseString(jsonString).getAsJsonObject();
            } else {
                mainObject = new JsonObject();
                mainObject.add("entries", new JsonArray());
            }
            JsonArray entriesArray = mainObject.getAsJsonArray("entries");
            for (JsonElement entry : entriesArray) {
                JsonObject entryObject = entry.getAsJsonObject();
                if (entryObject.get("entityUUID").getAsString().equals(entityUUID.toString()))
                    return entryObject;
            }
            Files.write(CONTROLLERS_PATH, new Gson().toJson(mainObject).getBytes());
        } catch (IOException e) {
            AniTexLib.informUser(String.format("Impossible to get json file with modid: %s, entityUUID: %s", modId, entityUUID), true);
        }
        return null;
    }

    private static void createOrUpdateJson(String modId, String textureFolder, String nameOfTexture, boolean haveToContinueAnimation, int stopFrame, Integer interval, Integer countOfFrames, UUID entityUUID) {
        try {
            Path configDir = FMLPaths.GAMEDIR.get().resolve(modId);
            if (!Files.exists(configDir))
                Files.createDirectories(configDir);
            Path CONTROLLERS_PATH = configDir.resolve("animations_controller.json");
            JsonObject mainObject;
            if (Files.exists(CONTROLLERS_PATH)) {
                String jsonString = new String(Files.readAllBytes(CONTROLLERS_PATH));
                mainObject = JsonParser.parseString(jsonString).getAsJsonObject();
            } else {
                mainObject = new JsonObject();
                mainObject.add("entries", new JsonArray());
            }

            JsonArray entriesArray = mainObject.getAsJsonArray("entries");
            boolean found = false;
            for (JsonElement entry : entriesArray) {
                JsonObject entryObject = entry.getAsJsonObject();
                if (entryObject.get("entityUUID").getAsString().equals(entityUUID.toString())) {
                    entryObject.addProperty("haveToContinueAnimation", haveToContinueAnimation);
                    entryObject.addProperty("stopFrame", stopFrame);
                    entryObject.addProperty("interval_of_animation", stopFrame);
                    entryObject.addProperty("amount_of_frames", stopFrame);
                    entryObject.addProperty("nameOfTexture", nameOfTexture);
                    entryObject.addProperty("textureFolder", textureFolder);
                    found = true;
                    break;
                }
            }

            if (!found) {
                JsonObject newEntry = new JsonObject();
                newEntry.addProperty("entityUUID", entityUUID.toString());
                newEntry.addProperty("haveToContinueAnimation", haveToContinueAnimation);
                newEntry.addProperty("stopFrame", stopFrame);
                newEntry.addProperty("nameOfTexture", nameOfTexture);
                newEntry.addProperty("textureFolder", textureFolder);
                entriesArray.add(newEntry);
            }

            Files.write(CONTROLLERS_PATH, new Gson().toJson(mainObject).getBytes());
        } catch (IOException e) {
            AniTexLib.informUser(String.format("Impossible to create/update json file with modid: %s, entityUUID: %s", modId, entityUUID), true);
        }
    }
}
