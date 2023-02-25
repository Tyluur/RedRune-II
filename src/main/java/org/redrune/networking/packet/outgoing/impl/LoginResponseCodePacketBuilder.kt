package org.redrune.networking.packet.outgoing.impl

import org.redrune.networking.packet.Packet
import org.redrune.networking.packet.PacketBuilder
import org.redrune.networking.packet.outgoing.OutgoingPacketBuilder
import org.redrune.utility.game.entity.actor.player.LoginReturnCode

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-02
 */
class LoginResponseCodePacketBuilder
/**
 * Constructs a new login response packet with the numerical value of the response code. See [LoginReturnCode]
 * for the possible values
 */(
        /**
         * The byte value of the response code
         */
        private val responseCode: Int
) : OutgoingPacketBuilder(PacketBuilder()) {

    /**
     * Constructs a new login response packet with a `LoginReturnCode` `Object`
     */
    constructor(code: LoginReturnCode) : this(code.value.toInt())

    override fun build(): Packet {
        bldr.writeByte(responseCode)
        return bldr.toPacket()
    }
}