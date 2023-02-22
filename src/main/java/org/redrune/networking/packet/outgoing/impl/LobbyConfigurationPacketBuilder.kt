package org.redrune.networking.packet.outgoing.impl

import org.redrune.game.entity.actor.player.Player
import org.redrune.networking.packet.Packet
import org.redrune.networking.packet.PacketBuilder
import org.redrune.networking.packet.PacketType
import org.redrune.networking.packet.outgoing.OutgoingPacketBuilder
import org.redrune.utility.functions.Misc

/**
 * Constructs the outgoing packet bldr
 *
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-02
 */
class LobbyConfigurationPacketBuilder(
    /**
     * The player that the packet is being sent to
     */
    private val player: Player

) : OutgoingPacketBuilder(PacketBuilder(2, PacketType.VAR_BYTE)) {

    override fun build(): Packet {
        // leave the cast because of json boxing.
        val lastLogin =
            System.currentTimeMillis() /*((Number) player.getVariables().getAttribute(AttributeKey.LAST_LONGIN_STAMP, System.currentTimeMillis())).longValue();*/
        val now = System.currentTimeMillis()
        val jag = 1014753880308L
        val since_jag = (now - jag) / 1000 / 60 / 60 / 24
        val since_log = (now - lastLogin) / 1000 / 60 / 60 / 24
        var lastIp = player.attributes.lastIP
        if (lastIp == null) {
            lastIp = Misc.getIpAddress(player.session.channel)
        }
        bldr.writeByte(player.dominantRight.clientRight) // rights
        bldr.writeByte(0) // blackmarks
        bldr.writeByte(0) // muted? (bool)
        bldr.writeByte(0) // dunno (bool)
        bldr.writeByte(0) // dunno (bool)
        bldr.writeLong(0) // members subscription end
        bldr.writeByte(0) // 0x1 - if members, 0x2 - subscription
        bldr.writeInt(0) // recovery questions set date
        bldr.writeByte(2) // 0 - Not a member, 1 - Membership expires, 2 - Subscription active
        bldr.writeInt(0)
        bldr.writeByte(0)
        bldr.writeInt(0)
        bldr.writeShort(1) //recovery questions set
        bldr.writeShort(0) //Number of unread messages
        bldr.writeShort((since_jag - since_log).toInt()) // last logged in date
        bldr.writeInt(Misc.IPAddressToNumber(lastIp)) //Resolve hostname - last login ip
        bldr.writeByte(3) //Email registration: 0 - Unregisted, 1 - Pending Parental Confirm, 2 - Pending Confirm, 3 - Registered, 4 - No longer registered, 5 - Blank
        bldr.writeShort(0)
        bldr.writeShort(0) // 	loginResponse.AppendShort(0);
        bldr.writeByte(1) //   Script 6909
        bldr.writeGJString(player.username)
        bldr.writeByte(0) // //Script 6911r
        bldr.writeInt(0) //  loginResponse.AppendInt(character.Name.StartsWith("#") ? 0 : 1);
        bldr.writeByte(0) // Bool Script 4700
        bldr.writeShort(1) // worldid
        bldr.writeGJString("127.0.0.1")
        return bldr.toPacket()
    }
}