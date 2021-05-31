package org.redrune.net.codec

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ReplayingDecoder
import org.redrune.net.NetworkSession
import org.redrune.net.packet.Packet
import org.redrune.net.packet.PacketType
import org.redrune.utility.constants.NetworkConstants
import org.redrune.utility.constants.PacketConstants

/**
 * Decodes a received packet.
 *
 * @author Cjay0091
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-05
 */
class RS2PacketDecoder(val session: NetworkSession) : ReplayingDecoder<GameState?>(GameState.VERSION) {
    /**
     * The opcode of the current packed being decoded
     */
    private var opcode = 0

    /**
     * The length of the current packet being decoded
     */
    private var length = 0

    @Throws(Exception::class)
    override fun decode(ctx: ChannelHandlerContext, `in`: ByteBuf, out: MutableList<Any>) {
        when (state()) {
            GameState.VERSION -> {
                opcode = `in`.readUnsignedByte().toInt()
                checkpoint(GameState.PAYLOAD_LENGTH)
            }
            GameState.PAYLOAD_LENGTH -> {
                length = PacketConstants.PACKET_SIZES[opcode].toInt()
                if (length == -1) {
                    length = `in`.readUnsignedByte().toInt()
                } else if (length == -2) {
                    length = `in`.readUnsignedShort()
                } else if (length == -3) {
                    length = `in`.readInt()
                }
                checkpoint(GameState.PAYLOAD)
            }
            GameState.PAYLOAD -> {
                try {
                    val payload = ByteArray(length)
                    `in`.readBytes(payload, 0, length)
                    `in`.markReaderIndex()
                    out.add(Packet(opcode, PacketType.STANDARD, Unpooled.copiedBuffer(payload)))
                } catch (e: Exception) {
                    println("Packet[$opcode, $length]")
                    ctx.fireExceptionCaught(e)
                }
                checkpoint(GameState.VERSION)
            }
            else -> {

            }
        }
    }

    /**
     * Constructs a new `RS2GameDecoder` `Object`.
     *
     * @param session The session.
     */
    init {
        session.channel.attr(NetworkConstants.SESSION_KEY).setIfAbsent(session)
    }
}