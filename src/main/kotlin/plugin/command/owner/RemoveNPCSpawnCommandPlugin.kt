package plugin.command.owner

import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/4/2017
 */
class RemoveNPCSpawnCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        player.putTemporaryAttribute("removing_npcs", !player.getTemporaryAttribute("removing_npcs", false))
        player.packets.sendMessage(
            "You are now " + (if (player.getTemporaryAttribute(
                    "removing_npcs",
                    false
                )
            ) "removing" else "examining") + " npcs."
        )
    }

    override fun identifiers(): Array<String> {
        return arguments("rspns")
    }
}