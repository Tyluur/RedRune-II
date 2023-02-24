package org.redrune.utility.game.entity.`object`

import com.github.michaelbull.logging.InlineLogger
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.`object`.WorldObject
import org.redrune.game.global.WorldTile
import java.io.BufferedReader
import java.io.FileReader
import java.util.function.Predicate
import java.util.stream.Collectors

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since Dec 13, 2013
 */
object ObjectRemoval {
    /**
     * The file to read from
     */
    const val NONSPAWNING_OBJECTS_FILE = "data/repository/object/nonspawning.txt"

    /**
     * The list of objects that aren't spawned
     */
    private val OBJECTS: MutableSet<WorldObject> = LinkedHashSet<WorldObject>()

    /**
     * Starts up and populates the list
     */
    fun initialize() {
        populateList()
    }

    /**
     * Populates the list with data from the file
     */
    private fun populateList() {
        try {
            val reader = BufferedReader(FileReader(NONSPAWNING_OBJECTS_FILE))
            while (true) {
                val line = reader.readLine() ?: break
                if (line.startsWith("//") || line.trim { it <= ' ' }.equals("", ignoreCase = true)) {
                    continue
                }
                var id = 0
                var type = 0
                var rotation = 0
                var x = 0
                var y = 0
                var z = 0
                try {
                    val split = line.split(" ".toRegex()).toTypedArray()
                    id = split[0].toInt()
                    type = split[1].toInt()
                    rotation = split[2].toInt()
                    x = split[3].toInt()
                    y = split[4].toInt()
                    z = split[5].toInt()
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }
                OBJECTS.add(WorldObject(id, type, rotation, WorldTile(x, y, z)))
            }
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        logger.info { "Loaded " + OBJECTS.size + " objects not to be spawned." }
    }

    /**
     * Gets a list of all the objects removed on a region
     *
     * @param regionId
     * The region id
     */
    fun getRemovedAtRegion(regionId: Int): List<WorldObject> {
        return OBJECTS.stream().filter(Predicate<WorldObject> { p: WorldObject -> p.regionId == regionId })
            .collect(
                Collectors.toList()
            )
    }

    /**
     * Finds out if a removed object exists on this tile, and if so we returni t
     *
     * @param object
     * The object
     * @return The `WorldObject` that existed
     */
    @JvmStatic
    fun removedObjectExists(`object`: WorldObject): WorldObject? {
        for (loopObject in OBJECTS) {
            if (loopObject == `object`) {
                return `object`
            }
        }
        return null
    }

    @JvmStatic
    fun handleRegionChange(player: Player) {
        if (!player.hasStarted()) {
            return
        }
        player.packets.refreshSpawnedObjects()
        /*		if (!player.hasStarted()) {
			return;
		}
		OBJECTS.stream().filter(object -> object.getRegionId() == player.getRegionId()).forEach(object -> {
			String key = "destroyed_object_" + object.getIds() + "_" + object.getRegionId();
			if (player.getTemporaryAttribute(key, false)) {
				return;
			}
			player.getPackets().sendDestroyObject(object);
			player.putAttribute(key, true);
		});*/
    }

    private val logger = InlineLogger()
}