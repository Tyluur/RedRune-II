package org.redrune.network.download.channel;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.redrune.network.NetworkConstants;
import org.redrune.network.NetworkSession;
import org.redrune.network.world.packet.Packet;

import static org.redrune.network.NetworkConstants.IGNORED_EXCEPTIONS;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/19/2017
 */
public class DSChannelReader extends SimpleChannelInboundHandler<Packet> {
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {
		NetworkSession session = ctx.channel().attr(NetworkConstants.SESSION_KEY).get();
		Preconditions.checkArgument(session != null, "Channel read a packet without session being set");
		
		System.out.println("Read a packet: " + packet);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
		if (IGNORED_EXCEPTIONS.stream().noneMatch($it -> Objects.equal($it, e.getMessage()))) {
			e.printStackTrace();
		}
		ctx.channel().close();
	}
}
