package plugin.command.owner

import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/4/2017
 */
class RemoveObjectSpawnCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        player.putTemporaryAttribute("removing_objects", !player.getTemporaryAttribute("removing_objects", false))
        player.packets.sendMessage(
            "You are now " + (if (player.getTemporaryAttribute(
                    "removing_objects",
                    false
                )
            ) "removing" else "examining") + " objects."
        )
    }

    override fun identifiers(): Array<String> {
        return arguments("rmospns")
    }
}