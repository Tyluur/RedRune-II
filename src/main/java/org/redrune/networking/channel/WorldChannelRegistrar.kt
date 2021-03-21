package org.redrune.networking.channel;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.redrune.networking.NetworkSession;
import org.redrune.utility.constants.NetworkConstants;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 7/19/2017
 */
@Sharable
public class WorldChannelRegistrar extends ChannelInboundHandlerAdapter {
	
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		try {
			NetworkSession session = ctx.channel().attr(NetworkConstants.SESSION_KEY).get();
			if (session == null) {
				System.out.println("Channel disconnected with no session");
				return;
			}
			session.onRegistration();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		try {
			NetworkSession session = ctx.channel().attr(NetworkConstants.SESSION_KEY).get();
			if (session == null) {
				System.out.println("Channel disconnected with no session");
				return;
			}
			session.onDeregistration();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
