package org.redrune.networking.packet.context.impl

import org.redrune.game.content.entity.actor.player.event.item.ItemFloorPickupEvent
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.item.FloorItem
import org.redrune.game.global.WorldTile
import org.redrune.networking.packet.context.PacketContext

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
class GroundItemPickupPacketContext(
    private val regionId: Int,
    private val forceRun: Boolean,
    private val item: FloorItem,
    private val tile: WorldTile,
) : PacketContext() {
    override fun handle(player: Player) {
        player.eventManager.start(ItemFloorPickupEvent(regionId, forceRun, item, tile))
    }
}