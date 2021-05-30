package plugin.command.administrator

import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.global.World
import plugin.command.CommandManifest

@CommandManifest(description = "Teleports a player to you", types = [String::class])
class TeleportPlayerToMeCommandPlugin : CommandPlugin() {

    override fun handle(player: Player, args: Array<out String>, console: Boolean, clientCommand: Boolean) {
        val target = World.getPlayerByDisplayName(getCompleted(args, 1))
        target.setNextWorldTile(player)
        player.packets.sendMessage("You teleport " + target.username.toString() + " to you.")
    }

    override fun identifiers() = arrayOf("teleto")

}