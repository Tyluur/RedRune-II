package org.redrune.networking.packet.incoming.impl

import org.redrune.game.entity.actor.player.Player
import org.redrune.networking.packet.Packet
import org.redrune.networking.packet.context.PacketContext
import org.redrune.networking.packet.context.impl.PingPacketContext
import org.redrune.networking.packet.incoming.IncomingPacketReader
import org.redrune.utility.constants.PacketConstants

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
class PingPacketReader : IncomingPacketReader {
    override fun bindings(): IntArray {
        return arguments(PacketConstants.PING_PACKET, PacketConstants.PING_STATISTICS_PACKET)
    }

    override fun read(player: Player, packet: Packet): PacketContext {
        val packetId = packet.opcode
        return if (packetId == PacketConstants.PING_PACKET) {
            PingPacketContext(-1)
        } else {
            val ping = packet.readShort()
            PingPacketContext(ping)
        }
    }
}