package org.redrune.network.master.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.redrune.network.master.MasterPacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/9/2017
 */
public class MasterPacketDecoder extends FrameDecoder {
	
	@Override
	protected Object decode(ChannelHandlerContext channelHandlerContext, Channel channel, ChannelBuffer buffer) throws Exception {
		if (buffer.readableBytes() < 2) {
			return null;
		}
		byte[] data;
		buffer.markReaderIndex();
		int length = buffer.readShort();
		if (buffer.readableBytes() >= length) {
			data = new byte[length];
			buffer.readBytes(data, 0, data.length);
		} else {
			buffer.resetReaderIndex();
			return null;
		}
		return new MasterPacket(data);
	}
}
