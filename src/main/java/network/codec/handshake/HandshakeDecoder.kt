package network.codec.handshake

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import network.codec.js5.UpdateServerDecoder
import network.codec.login.RS2LoginDecoder
import network.packet.PacketBuilder
import utility.constants.NetworkConstants
import kotlin.experimental.and

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-02
 */
class HandshakeDecoder : ByteToMessageDecoder() {

    @Throws(Exception::class)
    override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: List<Any>) {
        // removes the pipeline
        val pipeline = ctx.pipeline().remove(this)

        // the protocol id
        val id = (buf.readByte() and 0xFF.toByte()).toInt()

        // constructs a new bldr
        val builder = PacketBuilder()

        when (id) {
            NetworkConstants.JS5_REQUEST -> {
                val version = buf.readInt()
                if (version == NetworkConstants.PROTOCOL_NUMBER) {
                    builder.writeByte(0.toByte())
                    var i = 0
                    while (i < 27) {
                        builder.writeInt(NetworkConstants.GRAB_SERVER_KEYS[i])
                        i++
                    }
                    pipeline.addBefore("handler", "decoder", UpdateServerDecoder())
                } else {
                    builder.writeByte(6.toByte())
                }
            }
            NetworkConstants.LOGIN_REQUEST -> {
                builder.writeByte(0)
                pipeline.addBefore("handler", "decoder", RS2LoginDecoder())
            }
        }
        ctx.writeAndFlush(builder.buffer)
    }
}