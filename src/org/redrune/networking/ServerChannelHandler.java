package org.redrune.networking;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.redrune.engine.SystemManager;
import org.redrune.networking.codec.decode.WorldPacketsDecoder;
import org.redrune.networking.stream.InputStream;
import org.redrune.utility.constants.NetworkConstants;

import java.net.InetSocketAddress;

public final class ServerChannelHandler extends SimpleChannelHandler {
	
	private static ChannelGroup channels;
	
	private static ServerBootstrap bootstrap;
	
	private ServerChannelHandler() {
		channels = new DefaultChannelGroup();
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
			} catch (Throwable er) {
				er.printStackTrace();
			}
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent ee) {
	
	}
	
	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) {
		channels.add(e.getChannel());
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
		channels.remove(e.getChannel());
	}
	
	public static void init() {
		new ServerChannelHandler();
	}
	
	public static int getConnectedChannelsSize() {
		return channels == null ? 0 : channels.size();
	}
	
	public static void shutdown() {
		channels.close().awaitUninterruptibly();
		bootstrap.releaseExternalResources();
	}
	
}
