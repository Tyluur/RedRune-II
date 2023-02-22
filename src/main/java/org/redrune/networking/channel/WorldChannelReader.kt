package org.redrune.networking.channel

import com.google.common.base.Objects
import com.google.common.base.Preconditions
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import org.redrune.networking.packet.Packet
import org.redrune.networking.packet.incoming.IncomingPacketRepository
import org.redrune.utility.constants.NetworkConstants
import org.redrune.utility.functions.Misc

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 7/19/2017
 */
@Sharable
class WorldChannelReader : SimpleChannelInboundHandler<Packet>() {

    @Throws(Exception::class)
    override fun channelRead0(ctx: ChannelHandlerContext, packet: Packet) {
        try {
            val session = ctx.channel().attr(NetworkConstants.SESSION_KEY).get()
            // makes sure we have a session
            Preconditions.checkArgument(session != null, "No session set for channel.")
            // the player of the session
            val player = session!!.player ?: return
            // make sure we have a player
            player.attributes.packetsDecoderPing = Misc.currentTimeMillis()
            IncomingPacketRepository.handlePacket(player, packet)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, e: Throwable) {
        println("ctx = [$ctx], e = [$e]")
        if (NetworkConstants.IGNORED_EXCEPTIONS.stream()
                .noneMatch { `$it`: String? -> Objects.equal(`$it`, e.message) }
        ) {
            e.printStackTrace()
        }
        ctx.channel().close()
    }
}