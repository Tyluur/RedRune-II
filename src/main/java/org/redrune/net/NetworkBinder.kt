package org.redrune.net

import com.github.michaelbull.logging.InlineLogger
import io.netty.bootstrap.ServerBootstrap
import io.netty.buffer.PooledByteBufAllocator
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import org.redrune.engine.SystemManager
import org.redrune.net.channel.WorldChannelInitializer
import org.redrune.utility.constants.NetworkConstants

/**
 * This is the network binder for the game protocol.
 *
 * @author Tyluur <itstyluur@icloud.com>
 * @since 5/18/2017
 */
@Sharable
object NetworkBinder {

    /**
     * Binds to port [NetworkConstants.PORT_ID]
     */
    fun bind() {
        val bossGroup = NioEventLoopGroup(SystemManager.PROCESSOR_COUNT)
        val workerGroup = NioEventLoopGroup(SystemManager.PROCESSOR_COUNT)
        try {
            val bootstrap = ServerBootstrap()

            // builds the bootstrap
            bootstrap.group(bossGroup, workerGroup)
            bootstrap.channel(NioServerSocketChannel::class.java)
            bootstrap.option(ChannelOption.SO_BACKLOG, 25)
            bootstrap.option(ChannelOption.SO_REUSEADDR, true)
            bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, NetworkConstants.TIMEOUT_RATE)
            bootstrap.childOption(ChannelOption.TCP_NODELAY, true)
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true)
            bootstrap.childOption(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, 32 * 1024)
            bootstrap.childOption(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, 8 * 1024)
            bootstrap.childHandler(WorldChannelInitializer())

            // Bind and start to accept incoming connections.
            val future = bootstrap.bind(NetworkConstants.PORT_ID).sync()

            // tell the console that we're bound
            logger.info { "Network bound to port: " + NetworkConstants.PORT_ID }
            future.channel().closeFuture().sync()
        } finally {
            workerGroup.shutdownGracefully()
            bossGroup.shutdownGracefully()
        }
    }

    private val logger = InlineLogger()
}
