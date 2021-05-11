package plugin.command.server_moderator

import game.content.plugin.type.CommandPlugin
import game.entity.actor.player.Player
import game.global.World
import plugin.command.CommandManifest

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/1/2017
 */
@CommandManifest(description = "Kicks a player from the server", types = [String::class])
class KickCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val name = getCompleted(args, 1)
        val target = World.getPlayerByDisplayName(name) ?: return
        target.session.channel.close()
        World.removePlayer(target, false)
    }

    override fun identifiers(): Array<String> {
        return arguments("kick")
    }
}