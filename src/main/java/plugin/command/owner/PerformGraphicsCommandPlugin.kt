package plugin.command.owner

import game.content.plugin.type.CommandPlugin
import game.entity.actor.mask.Graphics
import game.entity.actor.player.Player
import plugin.command.CommandManifest

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/5/2017
 */
@CommandManifest(description = "Performs a graphic", types = [Int::class])
class PerformGraphicsCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val id = intParam(args, 1)
        val speed = intParamOrDefault(args, 2, 0)
        val height = intParamOrDefault(args, 3, 0)
        val rotation = intParamOrDefault(args, 4, 0)
        player.setNextGraphics(Graphics(id, speed, height, rotation))
    }

    override fun identifiers(): Array<String> {
        return arguments("gfx")
    }
}