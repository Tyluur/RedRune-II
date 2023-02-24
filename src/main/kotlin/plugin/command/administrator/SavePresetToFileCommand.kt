package plugin.command.administrator

import org.redrune.game.content.entity.actor.player.skills.PresetHandler
import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.actor.player.data.Preset
import plugin.command.CommandManifest

@CommandManifest(description = "Saves a preset to the global repository")
class SavePresetToFileCommand : CommandPlugin() {
    override fun handle(player: Player, args: Array<out String>, console: Boolean, clientCommand: Boolean) {
        PresetHandler.dumpPreset(Preset.generatePreset(player))
    }

    override fun identifiers(): Array<String> {
        return arguments("save_global_preset")
    }
}