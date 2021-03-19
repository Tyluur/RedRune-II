package org.redrune.utility.game.entity.object;

import com.google.gson.reflect.TypeToken;
import org.redrune.game.entity.object.WorldObject;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.utility.functions.Misc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.redrune.utility.functions.GsonFunctions.GSON;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-01-31
 */
public class ObjectSpawning {

    /**
     * The location of all door mappings
     */
    private static final String STORAGE_FILE_LOCATION = "./data/repository/object/object_spawns.json";

    /**
     * The map of objects to spawn, key being the region id, and the value being the list of objects in that region
     */
    private static final Map<Integer, List<WorldObject>> OBJECTS_TO_SPAWN = new HashMap<>();

    /**
     * Initializes the object spawns
     */
    public static void initialize() {
        List<WorldObject> objectList = loadObjectsFromFile();
        if (objectList == null) {
            objectList = new ArrayList<>();
        }
        OBJECTS_TO_SPAWN.clear();
        int count = 0;
        for (WorldObject object : objectList) {
            addObject(object);
            count++;
        }
        System.out.println("Loaded " + count + " objects to spawn");
    }

    /**
     * Adds an object to the list of objects to spawn
     *
     * @param object The object
     */
    private static void addObject(WorldObject object) {
        int regionId = object.getRegionId();
        List<WorldObject> objectsInRegion = OBJECTS_TO_SPAWN.get(regionId);
        if (objectsInRegion == null) {
            objectsInRegion = new ArrayList<>();
        }
        objectsInRegion.add(object);
        OBJECTS_TO_SPAWN.put(regionId, objectsInRegion);
    }

    public static void loadObjectSpawns(int regionId) {
        List<WorldObject> objectList = OBJECTS_TO_SPAWN.get(regionId);
        if (objectList == null) {
            return;
        }
        objectList.forEach(RegionManager::spawnObject);
    }

    /**
     * Loading the objects file into a gson list
     */
    private static List<WorldObject> loadObjectsFromFile() {
        File file = new File(STORAGE_FILE_LOCATION);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        String text = Misc.getText(STORAGE_FILE_LOCATION);
        return GSON.fromJson(text, new TypeToken<List<WorldObject>>() {
        }.getType());
    }

    /**
     * Saves the list of objects to the file {@link #STORAGE_FILE_LOCATION} by overwriting it
     *
     * @param objectList The list of objects
     */
    public static void saveObjectList(List<WorldObject> objectList) {
        Misc.saveToJsonFile(STORAGE_FILE_LOCATION, objectList);
    }

    public static void saveObject(WorldObject object) {
        List<WorldObject> objects = loadObjectsFromFile();
        objects.add(object);
        saveObjectList(objects);
    }
}
