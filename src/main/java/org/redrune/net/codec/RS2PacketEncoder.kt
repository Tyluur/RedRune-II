package org.redrune.net.codec

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import org.redrune.net.packet.Packet
import org.redrune.net.packet.PacketType
import org.redrune.utility.constants.NetworkConstants

/**
 * This encodes a packet going to the rs client. All packets have a specific header and data is encoded differently
 * based on the type of packet being encoded. This class handles all said operations.
 *
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 5/18/2017
 */
@ChannelHandler.Sharable
class RS2PacketEncoder : MessageToByteEncoder<Packet>() {
    @Throws(Exception::class)
    override fun encode(ctx: ChannelHandlerContext, packet: Packet, out: ByteBuf) {
        try {
            // the session
            val session = ctx.channel().attr(NetworkConstants.SESSION_KEY).get()
            // the encoded response
            val response: ByteBuf
            if (packet.isRaw) {
                response = packet.buffer
            } else {
                // the length of the packet
                val length = packet.buffer.readableBytes()
                // create the buffer
                response = Unpooled.buffer(length + 3)
                // the id of the packet
                val opcode = packet.opcode
                // the packet type
                val type = packet.type
                // if there was no cipher
                if (opcode >= 128) {
                    response.writeByte((opcode shr 8) + 128)
                }
                response.writeByte(opcode)
                if (type == PacketType.VAR_BYTE) {
                    check(length <= 255) {  // Stack overflow.
                        "Could not send a packet with $length bytes within 8 bits."
                    }
                    response.writeByte(length)
                } else if (type == PacketType.VAR_SHORT) {
                    check(length <= 65535) {  // Stack overflow.
                        "Could not send a packet with $length bytes within 16 bits."
                    }
                    response.writeShort(length)
                }
                response.writeBytes(packet.buffer)
            }
            out.writeBytes(response)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    /**
     * Writes a smart byte to the buffer
     *
     * @param buffer The buffer
     * @param value  The value to write
     */
    private fun writeSmart(buffer: ByteBuf, value: Int) {
        if (value >= 128) {
            buffer.writeByte(128)
        }
        buffer.writeByte(value)
    }
}