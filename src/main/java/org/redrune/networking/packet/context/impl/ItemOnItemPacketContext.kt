package org.redrune.networking.packet.context.impl

import org.redrune.game.content.entity.actor.player.event.item.ItemInterfaceInteractionEvent
import org.redrune.game.entity.actor.player.Player
import org.redrune.networking.packet.context.PacketContext

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 2019-02-04
 */
class ItemOnItemPacketContext(
    private val interfaceId: Int,
    private val itemUsedId: Int,
    private val fromSlot: Int,
    private val interfaceId2: Int,
    private val itemUsedWithId: Int,
    private val toSlot: Int
) : PacketContext() {
    override fun handle(player: Player) {
        player.eventManager.start(
            ItemInterfaceInteractionEvent(
                interfaceId,
                itemUsedId,
                fromSlot,
                interfaceId2,
                itemUsedWithId,
                toSlot
            )
        )
    }
}