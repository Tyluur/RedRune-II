package org.redrune.networking.packet.outgoing.impl

import org.redrune.game.entity.actor.player.Player
import org.redrune.networking.packet.Packet
import org.redrune.networking.packet.PacketBuilder
import org.redrune.networking.packet.PacketType
import org.redrune.networking.packet.outgoing.OutgoingPacketBuilder
import org.redrune.utility.functions.Misc

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-06
 */
class MessagePacketBuilder(private val p: Player?, private val text: String, private val type: Int) :
        OutgoingPacketBuilder(PacketBuilder(102, PacketType.VAR_BYTE)) {

    override fun build(): Packet {
        var maskData = 0
        if (p != null) {
            maskData = maskData or 0x1
            if (p.attributes.hasDisplayName()) {
                maskData = maskData or 0x2
            }
        }
        bldr.writeSmart(type)
        bldr.writeInt(0) // junk, not used by client
        bldr.writeByte(maskData)
        if (maskData and 0x1 != 0) {
            bldr.writeString(p!!.displayName)
            if (p.attributes.hasDisplayName()) {
                bldr.writeString(Misc.formatPlayerNameForDisplay(p.username))
            }
        }
        bldr.writeString(text)
        return bldr.toPacket()
    }
}