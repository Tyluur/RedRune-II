package org.redrune.network.rs666.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.Packet.PacketType;
import org.redrune.network.rs666.packet.PacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public final class RS2GameEncoder extends OneToOneEncoder {
	
	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object message) throws Exception {
		Packet packetMessage;
		if (message instanceof PacketBuilder) {
			packetMessage = ((PacketBuilder) message).toPacket();
		} else {
			packetMessage = (Packet) message;
		}
		if (!packetMessage.isRaw()) {
			int packetLength = 1 + packetMessage.getLength() + packetMessage.getType().getSize();
			
			ChannelBuffer response = ChannelBuffers.buffer(packetLength);
			
			response.writeByte((byte) packetMessage.getOpcode());
			if (packetMessage.getType() == PacketType.VAR_BYTE) {
				response.writeByte((byte) packetMessage.getLength());
			} else if (packetMessage.getType() == PacketType.VAR_SHORT) {
				response.writeShort((short) packetMessage.getLength());
			}
			response.writeBytes(packetMessage.getBuffer());
			
			return response;
		}
		return packetMessage.getBuffer();
	}
	
}
