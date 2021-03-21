package org.redrune.networking.channel

import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import org.redrune.networking.NetworkSession
import org.redrune.networking.codec.RS2PacketEncoder
import org.redrune.networking.codec.handshake.HandshakeDecoder
import org.redrune.utility.constants.NetworkConstants

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 7/19/2017
 */
@Sharable
class WorldChannelInitializer : ChannelInitializer<SocketChannel>() {
    override fun initChannel(channel: SocketChannel) {
        val pipeline = channel.pipeline()
        pipeline.addLast("encoder", RS2PacketEncoder())
        pipeline.addLast("decoder", HandshakeDecoder())
        pipeline.addLast("handler", CHANNEL_READER)
        pipeline.addLast("registrar", REGISTRAR)
        // sets the session
        pipeline.channel().attr(NetworkConstants.SESSION_KEY).set(NetworkSession(channel))
    }

    companion object {
        private val CHANNEL_READER = WorldChannelReader()
        private val REGISTRAR = WorldChannelRegistrar()
    }
}