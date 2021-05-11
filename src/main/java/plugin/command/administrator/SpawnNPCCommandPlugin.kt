package plugin.command.administrator

import game.content.plugin.type.CommandPlugin
import game.entity.actor.player.Player
import game.global.World
import plugin.command.CommandManifest

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/4/2017
 */
@CommandManifest(description = "Spawns an npc on your tile", types = [Int::class])
class SpawnNPCCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        World.spawnNPC(intParam(args, 1), player, -1, false, true)
    }

    override fun identifiers(): Array<String> {
        return arguments("npc")
    }
}