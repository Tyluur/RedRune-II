package plugin.command.player

import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player

class OpenForumsCommandPlugin : CommandPlugin() {
    override fun handle(
        player: Player,
        args: Array<out String>,
        console: Boolean,
        clientCommand: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun identifiers() = arrayOf("forums")

}