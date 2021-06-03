package org.redrune.game.content.entity.actor.player.event.item

import org.redrune.game.content.entity.actor.player.event.Event
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.actor.player.data.RouteEvent
import org.redrune.game.entity.item.FloorItem
import org.redrune.game.global.WorldTile
import org.redrune.game.global.map.region.RegionManager

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-06
 */
class ItemFloorPickupEvent(
    private val regionId: Int,
    private val forceRun: Boolean,
    private val item: FloorItem,
    private val tile: WorldTile
) : Event() {
    override fun run(player: Player) {
        if (forceRun) {
            player.isRunModeOn = true
        }
        player.stopAll(false)
        player.setRouteEvent(RouteEvent(item, Runnable {
            val floorItem = RegionManager.getRegion(regionId).getGroundItem(item.id, tile, player) ?: return@Runnable
            player.nextFaceWorldTile = tile
            RegionManager.removeGroundItem(player, floorItem)
        }, true))
    }

    override fun policies(): Array<EventPolicy> {
        return arguments(EventPolicy.CLOSE_INTERFACE)
    }
}