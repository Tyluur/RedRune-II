package plugin.command.administrator

import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.game.InputEvent
import plugin.command.CommandManifest

@CommandManifest(description = "Saves a new preset", types = [String::class])
class SavePresetCommandPlugin : CommandPlugin() {

    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        player.packets.requestClientInput(object :
            InputEvent("Enter name of new preset:", InputEventType.LONG_TEXT) {
            override fun handleInput() {
                player.presetManager.savePreset(getInput());
            }
        })
    }

    override fun identifiers(): Array<String> {
        return arguments("save")
    }

}