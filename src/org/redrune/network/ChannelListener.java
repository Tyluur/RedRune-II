package org.redrune.network;

import org.redrune.network.session.Session;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

/**
 * ChannelListener.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public class ChannelListener extends ChannelInboundHandlerAdapter {

	public static final AttributeKey<Session> CURRENT_SESSION = AttributeKey.valueOf("ChannelListener.attr");

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Channel connected from address "
				+ ctx.channel().remoteAddress().toString().split(":")[0].replace("/", ""));
		super.channelRegistered(ctx);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		if (ctx.channel().attr(CURRENT_SESSION).get() != null) {
			ctx.channel().attr(CURRENT_SESSION).get().disconnect();
		}
		System.out.println("Channel disconnected from address "
				+ ctx.channel().remoteAddress().toString().split(":")[0].replace("/", ""));
		super.channelUnregistered(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (cause.getMessage().equals("An existing connection was forcibly closed by the remote host")) {
			return;
		}
		super.exceptionCaught(ctx, cause);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Session session = ctx.channel().attr(CURRENT_SESSION).get();
		if (session != null && ctx.channel().isRegistered()) {
			session.throttleRequest(msg);
		}
		super.channelRead(ctx, msg);
	}

}
