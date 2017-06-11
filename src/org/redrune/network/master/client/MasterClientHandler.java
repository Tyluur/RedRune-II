package org.redrune.network.master.client;

import lombok.Getter;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.redrune.network.master.MasterPacket;
import org.redrune.network.master.MasterSession;
import org.redrune.network.master.client.packet.MasterClientPacketManager;
import org.redrune.utility.Misc;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.redrune.network.master.MasterConstants.*;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/9/2017
 */
public class MasterClientHandler extends SimpleChannelHandler {
	
	/**
	 * The channel
	 */
	@Getter
	private static MasterSession session;
	
	/**
	 * If we have connected to the server
	 */
	@Getter
	private static boolean connected;
	
	/**
	 * The logger
	 */
	private static final Logger LOGGER = Misc.constructLogger(MasterClientHandler.class);
	
	/**
	 * Starts the client connection
	 */
	public static void main(String[] args) {
		connect();
	}
	
	/**
	 * Connects to the master client
	 */
	public static boolean connect() {
		try {  // Configure the client.
			final NioClientSocketChannelFactory factory = new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
			final ClientBootstrap bootstrap = new ClientBootstrap(factory);
			bootstrap.setPipelineFactory(new MasterClientPipeline());
			
			bootstrap.setOption("tcpNoDelay", true);
			bootstrap.setOption("keepAlive", true);
			
			bootstrap.connect(new InetSocketAddress(HOST, PORT));
			
			LOGGER.info("Master client connected to port " + PORT);
			return true;
		} catch (Throwable e) {
			LOGGER.log(Level.SEVERE, "Unable to connect", e);
			return false;
		}
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		final MasterPacket packet = (MasterPacket) e.getMessage();
		if (packet == null) {
			return;
		}
		MasterClientPacketManager.read(ctx.getChannel(), packet);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
	
	}
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		session = new MasterSession(ctx.getChannel());
		LOGGER.info("Master client was connected to the master server successfully [PORT " + PORT + "]!");
		connected = true;
	}
	
	@Override
	public void channelClosed(final ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		LOGGER.info("The master client connection was dropped!");
		connected = false;
		MasterClientConnectionListener.scheduleReconnection();
	}
	
}
