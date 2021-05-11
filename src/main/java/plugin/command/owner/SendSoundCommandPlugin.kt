package plugin.command.owner

import game.content.plugin.type.CommandPlugin
import game.entity.actor.player.Player
import plugin.command.CommandManifest

/**
 * @author Tyluur
 * @since 2019-05-01
 */
@CommandManifest(description = "Plays a sound by the id", types = [Int::class])
class SendSoundCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val soundId = intParam(args, 1)
        val delay = intParamOrDefault(args, 2, 0)
        val effectType = intParamOrDefault(args, 3, 1)
        player.packets.sendSound(soundId, delay, effectType)
    }

    override fun identifiers(): Array<String> {
        return arguments("sound")
    }
}