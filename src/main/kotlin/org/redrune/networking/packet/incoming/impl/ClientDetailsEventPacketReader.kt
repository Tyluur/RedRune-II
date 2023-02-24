package org.redrune.networking.packet.incoming.impl

import org.redrune.game.entity.actor.player.Player
import org.redrune.networking.packet.Packet
import org.redrune.networking.packet.context.PacketContext
import org.redrune.networking.packet.context.impl.ClientDetailsPacketContext
import org.redrune.networking.packet.incoming.IncomingPacketReader
import org.redrune.utility.constants.PacketConstants

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
class ClientDetailsEventPacketReader : IncomingPacketReader {
    override fun bindings(): IntArray {
        return arguments(PacketConstants.RECEIVE_PACKET_COUNT_PACKET)
    }

    override fun read(player: Player, packet: Packet): PacketContext {
        val count = packet.readInt()
        return ClientDetailsPacketContext(count)
    }
}