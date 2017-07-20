package org.redrune.network.lobby.channel;

import com.google.common.base.Preconditions;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.redrune.network.NetworkConstants;
import org.redrune.network.world.WorldSession;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/19/2017
 */
public class LobbyChannelRegistrar extends ChannelInboundHandlerAdapter {
	
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
		System.out.println("LobbyChannelRegistrar.channelRegistered");
	}
	
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		WorldSession session = (WorldSession) ctx.channel().attr(NetworkConstants.SESSION_KEY).get();
		Preconditions.checkArgument(session != null, "No session set for channel.");
		session.pushDisconnect((byte) 0);
		super.channelUnregistered(ctx);
	}
	
}
