package org.redrune.network.download.channel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/19/2017
 */
public class DSChannelRegistrar extends ChannelInboundHandlerAdapter  {
	
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
		System.out.println("DSChannelRegistrar.channelRegistered");
	}
	
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		super.channelUnregistered(ctx);
		System.out.println("DSChannelRegistrar.channelUnregistered");
	}
}
