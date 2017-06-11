package org.redrune.network.master.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.redrune.network.master.MasterPacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/9/2017
 */
public class MasterPacketEncoder extends OneToOneEncoder {
	
	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object message) throws Exception {
		MasterPacket packet = (MasterPacket) message;
		ChannelBuffer buffer = ChannelBuffers.buffer(packet.getLength() + 2);
		buffer.writeShort(packet.getLength());
		buffer.writeBytes(packet.getData());
		return buffer;
	}
	
}
