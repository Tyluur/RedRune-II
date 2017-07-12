package org.redrune.network.rs666.codec;

import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.Packet.PacketType;
import org.redrune.network.rs666.packet.PacketBuilder;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

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
		Packet packet;
		if (message instanceof PacketBuilder) {
			packet = ((PacketBuilder) message).toPacket();
		} else {
			packet = (Packet) message;
		}
		if (!packet.isRaw()) {
			int packetLength = packet.getBuffer().readableBytes() + 4;
			ChannelBuffer response = ChannelBuffers.buffer(packetLength);
			final int opcode = packet.getOpcode();
			if (opcode >= 128) {
				response.writeByte(128);
				response.writeByte(opcode);
			} else {
				response.writeByte(opcode);
			}
			if (packet.getType() == PacketType.VAR_BYTE) {
				response.writeByte(packet.getBuffer().readableBytes());
			} else if (packet.getType() == PacketType.VAR_SHORT) {
				if (packetLength > 65535) {
					throw new IllegalStateException("Could not send a packet with " + packetLength + " bytes within 16 bits.");
				}
				response.writeByte((byte) (packet.getBuffer().readableBytes() >> 8));
				response.writeByte((byte) packet.getBuffer().readableBytes());
			}
			response.writeBytes(packet.getBuffer());
			return response;
		}
		return packet.getBuffer();
	}
	
}
