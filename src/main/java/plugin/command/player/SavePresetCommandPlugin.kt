package plugin.command.player

import game.content.plugin.type.CommandPlugin
import game.entity.actor.player.Player
import utility.game.InputEvent
import plugin.command.CommandManifest

@CommandManifest(description = "Saves a new preset", types = [String::class])
class SavePresetCommandPlugin : CommandPlugin() {

    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        player.packets.requestClientInput(object :
            InputEvent("Enter name of new preset:", InputEventType.LONG_TEXT) {
            override fun handleInput() {
                player.presetManager.savePreset(getInput())
            }
        })
    }

    override fun identifiers(): Array<String> {
        return arguments("save")
    }

}