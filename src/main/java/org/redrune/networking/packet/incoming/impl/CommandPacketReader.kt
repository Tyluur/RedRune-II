package org.redrune.networking.packet.incoming.impl

import org.redrune.game.entity.actor.player.Player
import org.redrune.networking.packet.Packet
import org.redrune.networking.packet.context.PacketContext
import org.redrune.networking.packet.context.impl.CommandPacketContext
import org.redrune.networking.packet.incoming.IncomingPacketReader
import org.redrune.utility.constants.PacketConstants

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
class CommandPacketReader : IncomingPacketReader {
    override fun bindings(): IntArray {
        return arguments(PacketConstants.COMMANDS_PACKET)
    }

    override fun read(player: Player, packet: Packet): PacketContext {
        val clientCommand = packet.readUnsignedByte() == 1
        val unknown = packet.readUnsignedByte() == 1
        val command = packet.readRS2String()
        return CommandPacketContext(clientCommand, unknown, command)
    }
}