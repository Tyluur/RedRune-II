package plugin.command.player

import org.redrune.game.content.plugin.PluginRepository
import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.constants.InterfaceConstants
import plugin.command.CommandManifest


/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/1/2017
 */
@CommandManifest(description = "Lists the commands available.")
class ListCommandsCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val messages = ArrayList<String>()
        val commands = PluginRepository.commands

        for (command in commands) {
            if (!player.rightsContains(command.rightRequired)) {
                continue
            }
            var names = ""
            val identifiers = command.identifiers()
            val withIndex = identifiers.withIndex()
            for ((index, name) in withIndex) {
                names += "$name${if (index == command.identifiers().size - 1) "" else ", "}"
            }
            messages.add("::[$names] - ${command.manifest?.description}")
        }

        InterfaceConstants.sendQuestScroll(player, "Commands", *messages.toTypedArray())
    }

    override fun identifiers(): Array<String> {
        return arguments("commands", "cmds")
    }
}