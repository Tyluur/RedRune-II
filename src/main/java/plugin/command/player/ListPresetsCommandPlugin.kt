package plugin.command.player

import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.constants.InterfaceConstants
import plugin.command.CommandManifest

@CommandManifest(description = "Lists your presets.")
class ListPresetsCommandPlugin : CommandPlugin() {

    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {

        val messages = ArrayList<String>()

        player.presetManager.setups.forEach {
            val name = it.key
            val preset = it.value

            messages.add("Preset: [$name]")
        }

        InterfaceConstants.sendQuestScroll(player, "Presets", *messages.toTypedArray())
    }

    override fun identifiers(): Array<String> {
        return arguments("presets", "listpresets")
    }

}