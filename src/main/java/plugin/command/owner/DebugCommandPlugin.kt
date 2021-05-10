package plugin.command.owner

import com.github.michaelbull.logging.InlineLogger
import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.global.map.region.RegionBuilder

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/12/2017
 */
class DebugCommandPlugin : CommandPlugin() {

    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val fromX = 422
        val fromY = 408

        val toX = 385
        val toY = 437

        val ratio = intParam(args, 1)

        RegionBuilder.copyAllPlanesMap(fromX, fromY, toX, toY, ratio)
        RegionBuilder.copyAllPlanesMap(400, 1160, toX, toY, ratio)

        player.packets.sendMapRegion(false)
    }

    override fun identifiers(): Array<String> {
        return arguments("dbg")
    }

    companion object {
        private val logger = InlineLogger()
    }
}