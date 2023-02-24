package org.redrune.networking.packet.context.impl

import org.redrune.game.content.entity.actor.player.event.`object`.ObjectInterfaceInteractionEvent
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.item.Item
import org.redrune.game.entity.`object`.WorldObject
import org.redrune.networking.packet.context.PacketContext

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
class ObjectItemInteractionPacketContext(
    private val `object`: WorldObject,
    private val y: Int,
    private val x: Int,
    private val itemSlot: Int,
    private val interfaceId: Int,
    private val itemId: Int,
    private val item: Item,
) : PacketContext() {

    override fun handle(player: Player) {
        player.eventManager.start(ObjectInterfaceInteractionEvent(`object`, y, x, itemSlot, interfaceId, itemId, item))
    }

}