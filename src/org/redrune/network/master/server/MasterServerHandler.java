package org.redrune.network.master.server;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.ServerSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.redrune.cache.Cache;
import org.redrune.core.master.server.MasterServerRepository;
import org.redrune.core.system.SystemManager;
import org.redrune.network.master.MasterPacket;
import org.redrune.utility.Misc;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.redrune.network.master.MasterConstants.PORT;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/9/2017
 */
public class MasterServerHandler extends SimpleChannelHandler {
	
	/**
	 * The logger
	 */
	private static final Logger LOGGER = Misc.constructLogger(MasterServerHandler.class);
	
	/**
	 * The repository
	 */
	private static final MasterServerRepository REPOSITORY = new MasterServerRepository();
	
	/**
	 * The packet manager
	 */
	private static final MasterServerPacketManager PACKET_MANAGER = new MasterServerPacketManager("org.redrune.network.master.packet.in.server");
	
	/**
	 * Starts the login server
	 */
	public static void main(String[] args) {
		bind();
	}
	
	/**
	 * Binds the login server
	 */
	public static boolean bind() {
		Cache.init();
		// defaults
		SystemManager.setDefaults();
		try {
			Executor executor = Executors.newCachedThreadPool();
			
			ServerBootstrap bootstrap = new ServerBootstrap();
			ServerSocketChannelFactory socketFactory = new NioServerSocketChannelFactory(executor, executor, SystemManager.PROCESSOR_COUNT);
			ChannelPipelineFactory pipelineFactory = new MasterServerPipeline();
			
			bootstrap.setOption("localAddress", new InetSocketAddress(PORT));
			bootstrap.setOption("child.tcpNoDelay", true);
			
			bootstrap.setFactory(socketFactory);
			bootstrap.setPipelineFactory(pipelineFactory);
			bootstrap.bind();
			
			REPOSITORY.getUpdateWorker().start();
			
			LOGGER.info("Master server bound to port: " + PORT);
			return true;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Unable to bind to port " + PORT, e);
			return false;
		}
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		try {
			final MasterPacket packet = (MasterPacket) e.getMessage();
			if (packet == null) {
				return;
			}
			PACKET_MANAGER.read(ctx.getChannel(), packet);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();
	}
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		LOGGER.info("Connection opened from " + ctx.getChannel().getRemoteAddress() + ".");
	}
	
	@Override
	public void channelClosed(final ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		LOGGER.info("Connection closed from " + ctx.getChannel().getRemoteAddress() + ".");
	}
	
	/**
	 * Gets the repository
	 */
	public static MasterServerRepository getRepository() {
		return REPOSITORY;
	}
}