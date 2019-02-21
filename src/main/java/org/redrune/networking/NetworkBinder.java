package org.redrune.networking;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.redrune.engine.SystemManager;
import org.redrune.networking.channel.WorldChannelInitializer;
import org.redrune.utility.constants.NetworkConstants;

/**
 * This is the network binder for the main game protocol. This initializes the main game server and the update server.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
@Sharable
public final class NetworkBinder {
	
	/**
	 * Binds to port {@link NetworkConstants#PORT_ID}
	 */
	public static void bind() throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup(SystemManager.PROCESSOR_COUNT);
		EventLoopGroup workerGroup = new NioEventLoopGroup(SystemManager.PROCESSOR_COUNT);
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			
			// builds the bootstrap
			bootstrap.group(bossGroup, workerGroup);
			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.option(ChannelOption.SO_BACKLOG, 25);
			bootstrap.option(ChannelOption.SO_REUSEADDR, true);
			bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
			bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, NetworkConstants.TIMEOUT_RATE);
			bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
			bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
			bootstrap.childOption(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, 32 * 1024);
			bootstrap.childOption(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, 8 * 1024);
			bootstrap.childHandler(new WorldChannelInitializer());
			
			// Bind and start to accept incoming connections.
			ChannelFuture future = bootstrap.bind(NetworkConstants.PORT_ID).sync();
			
			// tell the console that we're bound
			System.out.println("Network bound to port: " + NetworkConstants.PORT_ID);
			
			future.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
	
}
