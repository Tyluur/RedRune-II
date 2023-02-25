package org.redrune.networking.packet.outgoing.impl

import org.redrune.game.entity.actor.player.Player
import org.redrune.networking.packet.Packet
import org.redrune.networking.packet.PacketBuilder
import org.redrune.networking.packet.PacketType
import org.redrune.networking.packet.outgoing.OutgoingPacketBuilder

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
class LoginConfigurationPacketBuilder(private val player: Player) :
        OutgoingPacketBuilder(PacketBuilder(2, PacketType.VAR_BYTE)) {

    override fun build(): Packet {
        bldr.writeByte(player.dominantRight.clientRight)
        bldr.writeByte(0)
        bldr.writeByte(0)
        bldr.writeByte(0)
        bldr.writeByte(1)
        bldr.writeByte(0)
        bldr.writeShort(player.index)
        bldr.writeByte(1)
        bldr.write24BitInteger(0)
        bldr.writeByte(1)
        bldr.writeString(player.displayName)
        return bldr.toPacket()
    }
}