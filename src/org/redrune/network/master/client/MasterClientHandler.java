package org.redrune.network.master.client;

import lombok.Getter;
import lombok.Setter;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.redrune.game.GameFlags;
import org.redrune.network.RS2MasterCommunication;
import org.redrune.network.master.MasterConstants;
import org.redrune.network.master.MasterPacket;
import org.redrune.network.master.MasterSession;
import org.redrune.network.master.packet.out.client.build.ClientVerificationPacketBuilder;
import org.redrune.network.master.packet.out.client.context.ClientVerificationPacketContext;
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
	 * The logger
	 */
	private static final Logger LOGGER = Misc.constructLogger(MasterClientHandler.class);
	
	/**
	 * The packet manager
	 */
	private static final MasterClientPacketManager PACKET_MANAGER = new MasterClientPacketManager("org.redrune.network.master.packet.in.client");
	
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
	 * If we are verified
	 */
	@Getter
	@Setter
	private static boolean verified;
	
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
		PACKET_MANAGER.read(ctx.getChannel(), packet);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();
	}
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		session = new MasterSession(ctx.getChannel());
		LOGGER.info("Master client was connected to the master server successfully [PORT " + PORT + "]!");
		connected = true;
		RS2MasterCommunication.writeMasterPacket(new ClientVerificationPacketBuilder(new ClientVerificationPacketContext(GameFlags.worldId, MasterConstants.PASSWORD)).build());
	}
	
	@Override
	public void channelClosed(final ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		LOGGER.info("The master client connection was dropped!");
		connected = false;
		MasterClientConnectionListener.scheduleReconnection();
	}
	
}
