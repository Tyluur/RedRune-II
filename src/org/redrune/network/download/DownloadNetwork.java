package org.redrune.network.download;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.redrune.network.NetworkConstants;
import org.redrune.network.download.channel.DSChannelPipeline;
import org.redrune.utility.tool.Misc;

import java.util.logging.Logger;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/19/2017
 */
@Sharable
public class DownloadNetwork {
	
	/**
	 * The instance of the logger
	 */
	private static final Logger LOGGER = Misc.constructLogger(DownloadNetwork.class);
	
	/**
	 * Binds the network the port for the download server
	 *
	 * @throws InterruptedException
	 * 		If there was an exception in the process
	 */
	public static void bind() throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup(7);
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			
			// builds the bootstrap
			bootstrap.group(bossGroup, workerGroup);
			bootstrap.option(ChannelOption.SO_BACKLOG, 128);
			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
			bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
			bootstrap.childHandler(new DSChannelPipeline());
			
			// Bind and start to accept incoming connections.
			ChannelFuture future = bootstrap.bind(NetworkConstants.DOWNLOAD_PORT_ID).sync();
			
			// show debug messages
			LOGGER.info("Download server bound to port " + NetworkConstants.DOWNLOAD_PORT_ID);
			
			// Wait until the server socket is closed.
			future.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
			LOGGER.info("Download server shut down.");
		}
	}
	
}
