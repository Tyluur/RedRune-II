package org.redrune.utility.game.repository.`object`.climbable

import com.github.michaelbull.logging.InlineLogger
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.redrune.cache.Cache
import org.redrune.cache.loaders.ObjectDefinitions
import org.redrune.game.content.entity.`object`.ClimbActionHandler
import org.redrune.utility.file.JsonFileManager
import org.redrune.utility.functions.Misc
import java.io.File
import java.io.IOException
import java.util.*

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-21
 */
object ClimbableObjectRepository {
    /**
     * The doors mapping.
     */
    private val CLIMBABLE_OBJECTS: MutableMap<Int, ClimbableObject> = HashMap()

    /**
     * The location of the configuration file
     */
    private const val CONFIGURATION_FILE = "./data/repository/object/climbable_objects.json"

    /**
     * The gson instance
     */
    private val GSON = GsonBuilder().setPrettyPrinting().create()

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        Cache.initialize()
        val objects: MutableList<ClimbableObject> = ArrayList()
        for (i in 0 until Misc.getObjectDefinitionsSize()) {
            val def = ObjectDefinitions.getObjectDefinitions(i)
            if (def == null) {
                logger.error { ("Unable to object definitions for object $i") }
                continue
            }
            if (ClimbActionHandler.isLadder(def)) {
                objects.add(ClimbableObject(i, def.name))
            }
        }
        logger.info { "Populated the list with ${objects.size} climbable objects." }
        saveClimbables(objects)
    }

    /**
     * Loads all climbable objects from the file
     */
    fun initialize() {
        val climbableObjects = listFromFile
            ?: throw IllegalStateException("Unable to parse doors from file {$CONFIGURATION_FILE}, recheck running directory!")
        for (`object` in climbableObjects) {
            CLIMBABLE_OBJECTS[`object`.objectId] = `object`
        }
        logger.info { "Loaded " + CLIMBABLE_OBJECTS.size + " climbable objects" }
    }

    /**
     * Gets the ids of all the climbable objects that have been registered
     */
    val objectIds: Set<Int>
        get() = CLIMBABLE_OBJECTS.keys

    /**
     * Loads all the data for doors from the file
     */
    private val listFromFile: List<ClimbableObject>?
        private get() {
            val file = File(CONFIGURATION_FILE)
            if (!file.exists()) {
                return null
            }
            val text: String = Misc.getText(CONFIGURATION_FILE)
            return GSON.fromJson(text, object : TypeToken<List<ClimbableObject?>?>() {}.type)
        }

    /**
     * Saves the list of doors to file
     *
     * @param doors
     * The list of doors to save
     */
    private fun saveClimbables(doors: List<ClimbableObject>) {
        JsonFileManager.save<List<ClimbableObject>>(doors, CONFIGURATION_FILE)
    }

    private val logger = InlineLogger()

}