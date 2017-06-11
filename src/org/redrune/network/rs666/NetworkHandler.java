package org.redrune.network.rs666;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.ServerSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.redrune.core.system.SystemManager;
import org.redrune.game.GameFlags;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.rs666.codec.handshake.HandshakePacket;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.incoming.IncomingPacketRepository;
import org.redrune.utility.Misc;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import static org.redrune.network.NetworkConstants.BASE_PORT_ID;

/**
 * This is the network handler for the main game protocol. This initializes the main game server and the update server.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public final class NetworkHandler extends SimpleChannelHandler {
	
	/**
	 * The instance of the logger
	 */
	private static final Logger logger = Misc.constructLogger(NetworkHandler.class);
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		Object message = e.getMessage();
		if (message == null) {
			return;
		}
		if (message instanceof HandshakePacket) {
			HandshakePacket handshakeMessage = (HandshakePacket) message;
			ctx.getChannel().write(handshakeMessage.getPacket());
		} else if (message instanceof Packet) {
			Object attached = ctx.getAttachment();
			if (attached == null) {
				return;
			}
			NetworkSession session = (NetworkSession) attached;
			final Player player = session.getPlayer();
			if (player == null) {
				return;
			}
			IncomingPacketRepository.handlePacket(player, (Packet) message);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		//		e.getCause().printStackTrace();
	}
	
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		try {
			Object attached = ctx.getAttachment();
			if (attached == null) {
				return;
			}
			NetworkSession session = (NetworkSession) attached;
			Player player = session.getPlayer();
			if (player != null) {
				if (player.getNetworkSession().isInLobby()) {
					player.leaveLobby();
				} else {
					player.deregister();
				}
			}
			session.setPlayer(null);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Binds the local address to port {@link org.redrune.network.NetworkConstants#BASE_PORT_ID} + {@link
	 * GameFlags#worldId}
	 */
	public static void bind() throws IOException {
		int port = BASE_PORT_ID + GameFlags.worldId;
		Executor executor = Executors.newCachedThreadPool();
		
		ServerBootstrap bootstrap = new ServerBootstrap();
		ServerSocketChannelFactory socketFactory = new NioServerSocketChannelFactory(executor, executor, SystemManager.PROCESSOR_COUNT);
		ChannelPipelineFactory pipelineFactory = new NetworkPipeline();
		
		bootstrap.setOption("localAddress", new InetSocketAddress(port));
		bootstrap.setOption("child.tcpNoDelay", true);
		
		bootstrap.setFactory(socketFactory);
		bootstrap.setPipelineFactory(pipelineFactory);
		bootstrap.bind();
		
		logger.info("Network bound to port: " + port);
	}
	
}
