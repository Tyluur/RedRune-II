package plugin.command.owner

import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.actor.player.data.PlayerRight
import org.redrune.game.global.World

class GiveRightsCommandPlugin : CommandPlugin() {

    override fun handle(player: Player, args: Array<out String>, console: Boolean, clientCommand: Boolean) {
        val name = args[1]
        val rightsEntered = args[2].uppercase()

        val target = World.getPlayerByDisplayName(name)

        val right = PlayerRight.valueOf(rightsEntered)
        target.giveRight(right)

        player.packets.sendMessage("You have given $target the right $right")
    }

    override fun identifiers(): Array<String> = arrayOf("giverights")
}