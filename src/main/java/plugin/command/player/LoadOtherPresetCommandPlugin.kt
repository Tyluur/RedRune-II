package plugin.command.player

import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.global.World
import org.redrune.utility.game.InputEvent
import plugin.command.CommandManifest

@CommandManifest(description = "Loads a preset of other players", types = [String::class])
class LoadOtherPresetCommandPlugin : CommandPlugin() {

    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        player.packets.requestClientInput(object :
            InputEvent("Enter players name:", InputEventType.LONG_TEXT) {
            override fun handleInput() {
                val target = World.getPlayerByDisplayName(getInput()) ?: return
                player.packets.requestClientInput(object :
                    InputEvent("Enter other players presetname:", InputEventType.LONG_TEXT) {
                    override fun handleInput() {
                        player.presetManager.loadPreset(getInput(), target)
                    }
                })
            }
        })
    }

    override fun identifiers(): Array<String> {
        return arguments("loadother")
    }
}