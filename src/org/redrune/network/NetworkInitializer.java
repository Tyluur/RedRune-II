package org.redrune.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.redrune.network.protocol.ProtocolThrottle;

import java.net.InetSocketAddress;

/**
 * NetInitializer.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public class NetworkInitializer extends ChannelInitializer<SocketChannel> {

	public static void connect(String address, int port) throws InterruptedException {
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup());
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.childHandler(new NetworkInitializer());
		bootstrap.bind(new InetSocketAddress(address, port)).sync();
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast("p.handler", new ProtocolThrottle());
		ch.pipeline().addLast("n.listener", new ChannelListener());
	}

}
