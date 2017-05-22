package org.redrune.network.rs666.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.redrune.anetworking.rs666.packet.PacketBuilder;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public final class RS2GameEncoder extends OneToOneEncoder {
	
	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object message) throws Exception {
		if (message instanceof ChannelBuffer) {
			return ChannelBuffers.copiedBuffer((ChannelBuffer) message);
		}
		
		Packet packetMessage;
		if (message instanceof PacketBuilder) {
			packetMessage = ((PacketBuilder) message).toPacket();
		} else {
			packetMessage = (Packet) message;
		}
		if (!packetMessage.isRaw()) {
			int packetLength = packetMessage.getBuffer().readableBytes() + 3;
			ChannelBuffer response = ChannelBuffers.buffer(packetLength);
			if (packetMessage.getOpcode() > 127) {
				response.writeByte((byte) 128);
			}
			
			response.writeByte((byte) packetMessage.getOpcode());
			if (packetMessage.getType() == PacketType.VAR_BYTE) {
				response.writeByte((byte) packetMessage.getBuffer().readableBytes());
			} else if (packetMessage.getType() == PacketType.VAR_SHORT) {
				response.writeByte((byte)(packetMessage.getBuffer().readableBytes() >> 8));
				response.writeByte((byte) packetMessage.getBuffer().readableBytes());
			}
			response.writeBytes(packetMessage.getBuffer());
			
			return response;
		}
		return packetMessage.getBuffer();
	}
	
}
