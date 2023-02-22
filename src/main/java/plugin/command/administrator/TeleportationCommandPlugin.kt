package plugin.command.administrator

import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.global.WorldTile
import plugin.command.CommandManifest

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/1/2017
 */
@CommandManifest(description = "Teleports you to coordinates")
class TeleportationCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        var args = args
        if (clientCommand) {
            args = args[1].split(",".toRegex()).toTypedArray()
            val plane = Integer.valueOf(args[0])
            val x = Integer.valueOf(args[1]) shl 6 or Integer.valueOf(args[3])
            val y = Integer.valueOf(args[2]) shl 6 or Integer.valueOf(args[4])
            player.setNextWorldTile(WorldTile(x, y, plane))
        } else {
            player.resetWalkSteps()
            val x = intParamOrDefault(args, 1, player.x)
            val y = intParamOrDefault(args, 2, player.y)
            val plane = intParamOrDefault(args, 3, player.plane)
            player.setNextWorldTile(WorldTile(x, y, plane))
        }
    }

    override fun identifiers(): Array<String> {
        return arguments("tele")
    }
}