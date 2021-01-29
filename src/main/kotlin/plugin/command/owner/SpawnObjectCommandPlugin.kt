package plugin.command.owner

import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.`object`.WorldObject
import plugin.command.CommandManifest
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.global.map.region.RegionManager

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/4/2017
 */
@CommandManifest(description = "Spawns an object on your position", types = [Int::class])
class SpawnObjectCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val id = intParam(args, 1)
        val type = intParamOrDefault(args, 2, 10)
        val rotation = intParamOrDefault(args, 3, 0)
        val `object` = WorldObject(id, type, rotation, player)
        RegionManager.spawnObject(`object`)
        player.packets.sendMessage("Spawned Object: $`object`")
        println("Spawned Object: $`object`")
    }

    override fun identifiers(): Array<String> {
        return arguments("obj", "spawnobject")
    }
}