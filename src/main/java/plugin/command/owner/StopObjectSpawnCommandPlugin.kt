package plugin.command.owner

import game.content.plugin.type.CommandPlugin
import game.entity.`object`.WorldObject
import game.entity.actor.player.Player
import game.global.map.region.RegionManager
import utility.game.entity.`object`.ObjectRemoval
import plugin.command.CommandManifest
import java.io.BufferedWriter
import java.io.FileWriter
import java.text.MessageFormat
import java.util.*
import java.util.function.Predicate
import java.util.stream.Collectors

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/1/2017
 */
@CommandManifest(description = "Stops an object from spawning on our tile")
class StopObjectSpawnCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val stream: List<WorldObject> = RegionManager.getRegion(player.regionId).objects.stream().filter(
            Predicate<WorldObject> { `object`: WorldObject ->
                !`object`.isSpawned && !STOPPED_OBJECTS.contains(
                    `object`
                ) && `object`.worldTile.matches(player.worldTile)
            }).collect(
            Collectors.toList()
        )
        println(stream)
        val optional: Optional<WorldObject> = stream.stream().findFirst()
        if (!optional.isPresent) {
            player.packets.sendMessage("Did not find any object on this tile...")
            return
        }
        val `object`: WorldObject = optional.get()
        try {
            BufferedWriter(FileWriter(ObjectRemoval.NONSPAWNING_OBJECTS_FILE, true)).use { bw ->
                val pattern = "{0} {1} {2} {3} {4} {5}"
                val arguments = arrayOf<Any>(
                    `object`.id.toString(),
                    `object`.type,
                    `object`.rotation,
                    player.x.toString(),
                    player.y.toString(),
                    player.plane.toString()
                )
                val output = MessageFormat.format(pattern, *arguments)
                bw.append(output)
                bw.newLine()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        STOPPED_OBJECTS.add(`object`)
        RegionManager.removeObject(`object`)
        player.packets.sendMessage("Found and stopped this object from spawning!<br>$`object`")
    }

    override fun identifiers(): Array<String> {
        return arguments("stos")
    }

    companion object {
        @JvmField
        val STOPPED_OBJECTS: MutableList<WorldObject> = ArrayList<WorldObject>()
    }
}