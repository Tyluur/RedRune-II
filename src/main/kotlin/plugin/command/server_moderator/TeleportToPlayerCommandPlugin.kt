package plugin.command.server_moderator

import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.global.World
import plugin.command.CommandManifest

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since January 28, 2021
 */
@CommandManifest(description = "Teleports you to a player", types = [String::class])
class TeleportToPlayerCommandPlugin : CommandPlugin() {

    override fun handle(player: Player, args: Array<out String>, console: Boolean, clientCommand: Boolean) {
        val target = World.getPlayerByDisplayName(getCompleted(args, 1))
        player.setNextWorldTile(target)
        player.packets.sendMessage("You teleport to " + target.username.toString() + ".")
    }

    override fun identifiers() = arrayOf("teleto")

}