package plugin.command.owner

import game.content.plugin.type.CommandPlugin
import game.entity.`object`.WorldObject
import game.entity.actor.player.Player
import game.global.map.region.RegionManager
import utility.game.entity.`object`.ObjectSpawning
import plugin.command.CommandManifest

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/4/2017
 */
@CommandManifest(description = "Stores an object spawn at our current location", types = [Int::class])
class StoreObjectSpawnCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val id = intParam(args, 1)
        val type = intParamOrDefault(args, 2, 10)
        val rotation = intParamOrDefault(args, 3, 0)
        val spawned = WorldObject(id, type, rotation, player)

        ObjectSpawning.saveObject(spawned)

        RegionManager.spawnObject(spawned)
    }

    override fun identifiers(): Array<String> {
        return arguments("storeobj", "storeo")
    }
}