package org.redrune.utility.game.entity.`object`

import com.github.michaelbull.logging.InlineLogger
import com.google.gson.reflect.TypeToken
import org.redrune.game.entity.`object`.WorldObject
import org.redrune.game.global.map.region.RegionManager
import org.redrune.utility.functions.GsonFunctions
import org.redrune.utility.functions.Misc
import java.io.File
import java.util.*
import java.util.function.Consumer

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-01-31
 */
object ObjectSpawning {
    /**
     * The location of all door mappings
     */
    private const val STORAGE_FILE_LOCATION = "./data/repository/object/object_spawns.json"

    /**
     * The map of objects to spawn, key being the region id, and the value being the list of objects in that region
     */
    private val OBJECTS_TO_SPAWN = HashMap<Int, MutableList<WorldObject>?>()

    /**
     * Initializes the object spawns
     */
    fun initialize() {
        var objectList = loadObjectsFromFile()
        if (objectList == null) {
            objectList = ArrayList<WorldObject>()
        }
        OBJECTS_TO_SPAWN.clear()
        var count = 0
        for (`object` in objectList) {
            addObject(`object`)
            count++
        }
        logger.info { "Loaded $count objects to spawn" }
    }

    /**
     * Adds an object to the list of objects to spawn
     *
     * @param object The object
     */
    private fun addObject(`object`: WorldObject) {
        val regionId = `object`.regionId
        var objectsInRegion = OBJECTS_TO_SPAWN[regionId]
        if (objectsInRegion == null) {
            objectsInRegion = ArrayList<WorldObject>()
        }
        objectsInRegion.add(`object`)
        OBJECTS_TO_SPAWN[regionId] = objectsInRegion
    }

    @JvmStatic
    fun loadObjectSpawns(regionId: Int) {
        val objectList = OBJECTS_TO_SPAWN[regionId] ?: return
        with(objectList) {
            forEach(Consumer { `object`: WorldObject? -> RegionManager.spawnObject(`object`) })
        }
    }

    /**
     * Loading the objects file into a gson list
     */
    private fun loadObjectsFromFile(): MutableList<WorldObject> {
        val file = File(STORAGE_FILE_LOCATION)
        if (!file.exists()) {
            return ArrayList<WorldObject>()
        }
        val text: String = Misc.getText(STORAGE_FILE_LOCATION)
        return GsonFunctions.GSON.fromJson(text, object : TypeToken<List<WorldObject?>?>() {}.type)
    }

    /**
     * Saves the list of objects to the file [.STORAGE_FILE_LOCATION] by overwriting it
     *
     * @param objectList The list of objects
     */
    private fun saveObjectList(objectList: List<WorldObject>?) {
        Misc.saveToJsonFile(STORAGE_FILE_LOCATION, objectList)
    }

    fun saveObject(`object`: WorldObject) {
        val objects: MutableList<WorldObject> = loadObjectsFromFile()
        objects.add(`object`)
        saveObjectList(objects)
    }

    private val logger = InlineLogger()
}