package plugin.command.server_assistant

import plugin.command.CommandManifest
import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.global.World

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