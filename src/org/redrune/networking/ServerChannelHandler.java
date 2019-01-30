package org.redrune.networking;

import lombok.Getter;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.redrune.engine.SystemManager;
import org.redrune.networking.codec.decode.WorldPacketsDecoder;
import org.redrune.networking.stream.InputStream;
import org.redrune.utility.constants.NetworkConstants;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ServerChannelHandler extends SimpleChannelHandler {
	
	/**
	 * The bootstrap factory
	 */
	private static ServerBootstrap bootstrap;
	
	/**
	 * The list of all sessions in the game
	 */
	@Getter
	private static List<Session> sessionList = new CopyOnWriteArrayList<>();
	
	private ServerChannelHandler() {
		bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(SystemManager.SERVER_BOSS_CHANNEL_EXECUTOR, SystemManager.SERVER_WORKER_CHANNEL_EXECUTOR, SystemManager.serverWorkersCount));
		bootstrap.getPipeline().addLast("handler", this);
		bootstrap.setOption("reuseAddress", true); // reuses adress for bind
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.TcpAckFrequency", true);
		bootstrap.setOption("child.keepAlive", true);
		bootstrap.bind(new InetSocketAddress(NetworkConstants.PORT_ID));
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		if (!(e.getMessage() instanceof ChannelBuffer)) {
			return;
		}
		Object sessionObject = ctx.getAttachment();
		if (sessionObject instanceof Session) {
			Session session = (Session) sessionObject;
			sessionList.add(session);
			if (session.getDecoder() == null) {
				return;
			}
			ChannelBuffer buf = (ChannelBuffer) e.getMessage();
			buf.markReaderIndex();
			int avail = buf.readableBytes();
			if (avail < 1 || avail > NetworkConstants.RECEIVE_DATA_LIMIT) {
				return;
			}
			byte[] buffer = new byte[avail];
			buf.readBytes(buffer);
			try {
				session.getDecoder().decode(new InputStream(buffer));
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent ee) {
	
	}
	
	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) {
	
	}
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		ctx.setAttachment(new Session(e.getChannel()));
	}
	
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		Object sessionObject = ctx.getAttachment();
		if (sessionObject instanceof Session) {
			Session session = (Session) sessionObject;
			sessionList.remove(session);
			if (session.getDecoder() == null) {
				return;
			}
			if (session.getDecoder() instanceof WorldPacketsDecoder) {
				session.getWorldPackets().getPlayer().finish();
			}
		}
	}
	
	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
	
	}
	
	public static void init() {
		new ServerChannelHandler();
	}
	
	public static void shutdown() {
		bootstrap.releaseExternalResources();
	}
	
}
