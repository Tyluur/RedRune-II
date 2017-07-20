package org.redrune.network.world.channel;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/19/2017
 */
@Sharable
public class WorldChannelRegistrar extends ChannelInboundHandlerAdapter {
	
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
		System.out.println("NetworkRegistrar.channelRegistered");
	}
	
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		super.channelUnregistered(ctx);
		System.out.println("NetworkRegistrar.channelUnregistered");
	}
}
