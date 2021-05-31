package org.redrune.net.channel

import com.github.michaelbull.logging.InlineLogger
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import org.redrune.utility.constants.NetworkConstants

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 7/19/2017
 */
@Sharable
class WorldChannelRegistrar : ChannelInboundHandlerAdapter() {

    @Throws(Exception::class)
    override fun channelRegistered(ctx: ChannelHandlerContext) {
        try {
            val session = ctx.channel().attr(NetworkConstants.SESSION_KEY).get()
            if (session == null) {
                logger.info { "Channel registered with no session" }
                return
            }
            session.onRegistration()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(Exception::class)
    override fun channelUnregistered(ctx: ChannelHandlerContext) {
        try {
            val session = ctx.channel().attr(NetworkConstants.SESSION_KEY).get()
            if (session == null) {
                logger.info { "Channel unregistered with no session" }
                return
            }
            session.onDeregistration()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {

        private val logger = InlineLogger()

    }

}