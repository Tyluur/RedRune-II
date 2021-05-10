package plugin.command.owner

import com.github.michaelbull.logging.InlineLogger
import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.global.map.region.RegionBuilder

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/12/2017
 */
class DebugCommandPlugin : CommandPlugin() {

    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val fromX = 404
        val fromY = 1164

        val toX = player.regionX
        val toY = player.regionY

        val ratio = 64

        RegionBuilder.copyAllPlanesMap(404, 1164, toX, toY, ratio);
        RegionBuilder.copyAllPlanesMap(400, 1160, toX, toY, ratio);

        player.packets.sendMapRegion(false)
    }

    override fun identifiers(): Array<String> {
        return arguments("dbg")
    }

    companion object {
        private val logger = InlineLogger()
    }
}