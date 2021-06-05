package org.redrune.game.content.entity.actor.player.event.player

import org.redrune.game.content.entity.actor.player.event.Event
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.global.WorldTile
import org.redrune.game.global.map.route.RouteFinder
import org.redrune.game.global.map.route.strategy.FixedTileStrategy
import org.redrune.utility.functions.Misc

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-06
 */
class PlayerWalkEvent(
    /**
     * The destination x to travel to
     */
    private val destX: Int,
    /**
     * The destination y to travel to
     */
    private val destY: Int,
    /**
     * If the player should be forced to run
     */
    private val forceRun: Boolean
) : Event() {
    override fun run(player: Player) {
        if (!player.hasStarted() || !player.attributes.clientHasLoadedMapRegion() || player.isDead) {
            return
        }
        val currentTime = Misc.currentTimeMillis()
        if (player.locks.isMovementLocked) {
            return
        }
        if (player.freezeDelay >= currentTime) {
            player.packets.sendMessage("A magical force prevents you from moving.")
            return
        }
        player.stopAll()
        // forces the new run flag
        if (forceRun) {
            player.isRunModeOn = true
        }
        // calculates the amount of steps in the path
        val calculatedSteps = RouteFinder.findRoute(
            RouteFinder.WALK_ROUTEFINDER, player.x, player.y, player.plane, player.size, FixedTileStrategy(
                destX, destY
            ), true
        )
        // the buffer with the x steps
        val bufferX = RouteFinder.getLastPathBufferX()
        // the buffer with they steps
        val bufferY = RouteFinder.getLastPathBufferY()

        // adds walk steps to the movement queue
        var last = -1
        for (i in calculatedSteps - 1 downTo 0) {
            if (!player.addWalkSteps(bufferX[i], bufferY[i], 25, true)) {
                break
            }
            last = i
        }

        // sends destination on the minimap
        if (last != -1) {
            val tile = WorldTile(bufferX[last], bufferY[last], player.plane)
            player.packets.sendMinimapFlag(
                tile.getLocalX(player.lastLoadedMapRegionTile),
                tile.getLocalY(player.lastLoadedMapRegionTile)
            )
        } else {
            player.packets.sendResetMinimapFlag()
        }
    }

    override fun policies(): Array<EventPolicy> {
        return arguments(EventPolicy.STOP_WALK, EventPolicy.CLOSE_INTERFACE)
    }
}