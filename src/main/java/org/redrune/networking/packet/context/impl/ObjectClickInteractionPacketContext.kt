package org.redrune.networking.packet.context.impl

import org.redrune.game.content.entity.actor.player.event.`object`.ObjectInteractionEvent
import org.redrune.game.entity.`object`.WorldObject
import org.redrune.game.entity.actor.player.Player
import org.redrune.networking.packet.context.PacketContext
import org.redrune.utility.game.ClickOption

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
class ObjectClickInteractionPacketContext(
    /**
     * The object to interact with
     */
    private val worldObject: WorldObject,
    /**
     * The option that was clicked on the object
     */
    private val option: ClickOption
) : PacketContext() {

    override fun handle(player: Player) {
        player.eventManager.start(ObjectInteractionEvent(worldObject, option))
    }

}