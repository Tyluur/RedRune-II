package plugin.command.server_moderator

import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player
import plugin.command.CommandManifest

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/1/2017
 */
@CommandManifest(description = "Tells you your position")
class MyPositionCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val loc = player.worldTile.toString()
        player.packets.sendMessage(loc)
        println(loc)
    }

    override fun identifiers(): Array<String> {
        return arguments("pos", "mypos")
    }
}