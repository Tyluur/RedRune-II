package org.redrune.networking.packet.incoming.impl

import org.redrune.game.entity.actor.player.Player
import org.redrune.networking.packet.Packet
import org.redrune.networking.packet.context.PacketContext
import org.redrune.networking.packet.context.impl.WalkPacketContext
import org.redrune.networking.packet.incoming.IncomingPacketReader
import org.redrune.utility.constants.PacketConstants

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
class WalkPacketReader : IncomingPacketReader {
    override fun bindings(): IntArray {
        return arguments(PacketConstants.WALKING_PACKET, PacketConstants.MINI_WALKING_PACKET)
    }

    override fun read(player: Player, packet: Packet): PacketContext {
        val destX = packet.readUnsignedShortLE128()
        val destY = packet.readUnsignedShortLE128()
        val forceRun = packet.readByte().toInt() == 1
        return WalkPacketContext(destX, destY, forceRun)
    }
}