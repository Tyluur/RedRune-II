package plugin.command.player

import plugin.command.CommandManifest
import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.global.World
import org.redrune.utility.constants.SkillConstants
import plugin.command.player.YellCommandPlugin
import org.redrune.utility.functions.Misc
import org.redrune.game.entity.actor.player.data.PlayerRight
import org.redrune.utility.constants.InterfaceConstants
import org.redrune.cache.loaders.ItemDefinitions
import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.game.InputEvent

@CommandManifest(description = "Saves a new preset", types = [String::class])
class SavePresetCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val name = getCompleted(args, 1)
        player.packets.requestClientInput(object :
            InputEvent("Enter name of new preset:", InputEventType.LONG_TEXT) {
            override fun handleInput() {
                player.presetManager.savePreset(name);
            }
        })
    }

    override fun identifiers(): Array<String> {
        return arguments("save")
    }
}