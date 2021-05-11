package utility.game.repository.npc.spawn

import com.github.michaelbull.logging.InlineLogger
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import game.entity.actor.npc.NPC
import game.global.World
import game.global.WorldTile
import utility.functions.Misc
import utility.functions.Misc.FaceDirection
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.*
import java.util.function.Consumer

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/1/2017
 */
object NPCSpawnRepository {
    /**
     * The gson instance
     */
    private val GSON = GsonBuilder().setPrettyPrinting().create()

    /**
     * The location that the spawns will be stored
     */
    private const val SPAWNS_LOCATION = "./data/repository/npc/spawns/"

    /**
     * Adds a spawn to the list of spawns and saves it
     *
     * @param npcId
     * The id of the spawn
     * @param tile
     * The tile of the spawn
     * @param direction
     * The direction of the spawn
     */
    fun addSpawn(npcId: Int, tile: WorldTile, direction: FaceDirection) {
        var spawns = loadFromFile(tile.regionId)
        if (spawns == null) {
            spawns = ArrayList()
        }
        spawns.add(NPCSpawn(npcId, tile, direction))
        saveData(tile.regionId, spawns)
        World.spawnNPC(npcId, tile, -1, true, direction)
        println("Spawned " + npcId + " on " + tile + " facing " + direction + " at region " + tile.regionId)
    }

    /**
     * Removes an npc spawn
     *
     * @param npc
     * The npc to remove the spawn for
     */
    @JvmStatic
    fun removeSpawn(npc: NPC) {
        val spawns = loadFromFile(npc.regionId) ?: return
        var removed = false
        val `it$` = spawns.iterator()
        while (`it$`.hasNext()) {
            val spawn = `it$`.next()
            if (spawn.npcId == npc.id && spawn.tile.matches(npc.respawnTile)) {
                `it$`.remove()
                removed = true
            }
        }
        if (removed) {
            saveData(npc.regionId, spawns)
            println("Removed npc and saved file!\t$npc")
        }
    }

    /**
     * Loads all of the [NPCSpawn]s of the region into the world
     *
     * @param regionId
     * The region to find the spawns of
     */
    @JvmStatic
    fun loadSpawns(regionId: Int) {
        if (!regionSpawnsExist(regionId)) {
            logger.debug { ("region spawn $regionId does not exist") }
            return
        }
        val spawns: List<NPCSpawn>? = loadFromFile(regionId)
        if (spawns == null) {
            logger.debug { ("region spawn $regionId does not exist") }
            return
        }
        spawns.forEach(Consumer { spawn: NPCSpawn ->
            World.spawnNPC(
                spawn.npcId,
                spawn.tile,
                -1,
                true,
                spawn.direction
            )
        })
    }

    /**
     * Saves the data to a file
     *
     * @param regionId
     * The region of the spawns
     * @param spawns
     * The spawn data to write
     */
    fun saveData(regionId: Int, spawns: List<NPCSpawn>?) {
        try {
            FileWriter(getFileLocation(regionId)).use { writer ->
                val builder = GsonBuilder().setPrettyPrinting().disableHtmlEscaping()
                val gson = builder.create()
                gson.toJson(spawns, writer)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Loads and constructs a new `NPCSpawn` `List` from the [.getFileLocation] for the region
     * id
     *
     * @param regionId
     * The id of the region
     */
    private fun loadFromFile(regionId: Int): MutableList<NPCSpawn>? {
        val file = File(getFileLocation(regionId))
        return if (!file.exists()) {
            null
        } else GSON.fromJson(
            Misc.getText(file.absolutePath),
            object : TypeToken<List<NPCSpawn?>?>() {}.type
        )
    }

    /**
     * Checks if there are spawns for the region
     *
     * @param regionId
     * The region id to check for
     */
    private fun regionSpawnsExist(regionId: Int): Boolean {
        return File(getFileLocation(regionId)).exists()
    }

    /**
     * @param regionId
     * The id of the region
     */
    private fun getFileLocation(regionId: Int): String {
        return SPAWNS_LOCATION + regionId + ".json"
    }

    private val logger = InlineLogger()
}