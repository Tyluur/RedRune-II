package org.redrune.networking.packet.context.impl

import org.redrune.game.entity.actor.player.Player
import org.redrune.networking.packet.context.PacketContext

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
class PingPacketContext(
    /**
     * The latency between client and server
     */
    private val ping: Int,
) : PacketContext() {
    override fun handle(player: Player) {}
}