package plugin.command.administrator

import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.mask.Animation
import org.redrune.game.entity.actor.player.Player
import plugin.command.CommandManifest

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/5/2017
 */
@CommandManifest(description = "Performs an animation", types = [Int::class])
class PerformAnimationCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val id = intParam(args, 1)
        val speed = intParamOrDefault(args, 2, 0)
        player.nextAnimation = Animation(id, speed)
    }

    override fun identifiers(): Array<String> {
        return arguments("anim")
    }
}