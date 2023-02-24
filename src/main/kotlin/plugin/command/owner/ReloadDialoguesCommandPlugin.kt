package plugin.command.owner

import org.redrune.game.content.entity.actor.player.dialogue.DialogueHandler
import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-08
 */
class ReloadDialoguesCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        DialogueHandler.reload()
        player.packets.sendMessage("All dialogues have been reloaded.")
    }

    override fun identifiers(): Array<String> {
        return arguments("rld")
    }
}