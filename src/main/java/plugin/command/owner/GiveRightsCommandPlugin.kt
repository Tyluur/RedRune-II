package plugin.command.owner

import game.content.plugin.type.CommandPlugin
import game.entity.actor.player.Player
import game.entity.actor.player.data.PlayerRight
import game.global.World

class GiveRightsCommandPlugin : CommandPlugin() {

    override fun handle(player: Player, args: Array<out String>, console: Boolean, clientCommand: Boolean) {
        val target = World.getPlayerByDisplayName(getCompleted(args, 1))
        val right = PlayerRight.valueOf(args[1].toUpperCase())
        target.giveRight(right)
        player.packets.sendMessage("You have given $target the right $right")
    }

    override fun identifiers(): Array<String> = arrayOf("giveright")


}