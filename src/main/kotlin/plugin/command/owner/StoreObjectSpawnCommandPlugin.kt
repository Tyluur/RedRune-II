package plugin.command.owner

import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.`object`.WorldObject
import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.game.entity.`object`.ObjectSpawning
import plugin.command.CommandManifest

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/4/2017
 */
@CommandManifest(description = "Stores an object spawn at our current location", types = [Int::class])
class StoreObjectSpawnCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        ObjectSpawning.saveObject(WorldObject(intParam(args, 1), 10, 0, player))
    }

    override fun identifiers(): Array<String> {
        return arguments("storeobj", "storeo")
    }
}