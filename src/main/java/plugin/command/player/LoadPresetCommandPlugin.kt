package plugin.command.player

import game.content.plugin.type.CommandPlugin
import game.entity.actor.player.Player
import utility.game.InputEvent
import plugin.command.CommandManifest

@CommandManifest(description = "Loads a preset of yours", types = [String::class])
class LoadPresetCommandPlugin : CommandPlugin() {

    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        player.packets.requestClientInput(object :
            InputEvent("Enter presetname:", InputEventType.LONG_TEXT) {
            override fun handleInput() {
                player.presetManager.loadPreset(getInput(), player)
            }
        })
    }

    override fun identifiers(): Array<String> {
        return arguments("load")
    }

}