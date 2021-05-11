package plugin.command.administrator

import game.content.entity.actor.player.skills.PresetHandler
import game.content.plugin.type.CommandPlugin
import game.entity.actor.player.Player
import game.entity.actor.player.data.Preset
import plugin.command.CommandManifest

@CommandManifest(description = "Saves a preset to the utility.global repository")
class SavePresetToFileCommand : CommandPlugin() {
    override fun handle(player: Player, args: Array<out String>, console: Boolean, clientCommand: Boolean) {
        PresetHandler.dumpPreset(Preset.generatePreset(player))
    }

    override fun identifiers(): Array<String> {
        return arguments("save_global_preset")
    }
}