package plugin.command.server_moderator

import plugin.command.CommandManifest
import game.content.plugin.type.CommandPlugin
import game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
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