package org.redrune.network.master.server;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.ServerSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.redrune.core.master.server.MasterServerRepository;
import org.redrune.core.system.SystemManager;
import org.redrune.network.master.MasterPacket;
import org.redrune.network.master.server.packet.MasterServerPacketManager;
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
	 * Starts the login server
	 */
	public static void main(String[] args) {
		bind();
	}
	
	/**
	 * Binds the login server
	 */
	private static void bind() {
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
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Unable to bind to port " + PORT, e);
		}
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		try {
			final MasterPacket packet = (MasterPacket) e.getMessage();
			if (packet == null) {
				return;
			}
			MasterServerPacketManager.read(ctx.getChannel(), packet);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		ctx.getChannel().close();
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