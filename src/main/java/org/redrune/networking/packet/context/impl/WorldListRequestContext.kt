package org.redrune.networking.packet.context.impl

import org.redrune.game.entity.actor.player.Player
import org.redrune.networking.packet.context.PacketContext
import org.redrune.networking.packet.outgoing.impl.WorldListPacketBuilder

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
class WorldListRequestContext(
    /**
     * The type of update that is being requested
     */
    private val updateType: Int
) : PacketContext() {
    override fun handle(player: Player) {
        player.session.write(WorldListPacketBuilder(updateType == 0))
    }
}