package org.redrune.networking.packet.outgoing

import org.redrune.networking.packet.Packet
import org.redrune.networking.packet.PacketBuilder

/**
 * Constructs the outgoing packet bldr
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-02
 */
abstract class OutgoingPacketBuilder
    (
        /**
         * The packet builder instance
         */
        @JvmField protected val bldr: PacketBuilder,
) {

    /**
     * The building of the packet is handled in this method. The `PacketBuilder` is converted to a `Packet`
     * via [PacketBuilder.toPacket]
     *
     * @return A newly constructed packet
     */
    abstract fun build(): Packet
}