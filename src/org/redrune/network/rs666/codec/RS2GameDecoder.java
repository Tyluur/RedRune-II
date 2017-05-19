package org.redrune.network.rs666.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.redrune.network.NetworkConstants;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.Packet.PacketType;
import org.redrune.network.rs666.NetworkSession;

/**
 * Decodes a received packet.
 *
 * @author Dementhium development team
 * @author Emperor
 */
public class RS2GameDecoder extends FrameDecoder {
	
	/**
	 * Constructs a new {@code RS2GameDecoder} {@code Object}.
	 *
	 * @param session
	 * 		The networkSession.
	 */
	public RS2GameDecoder(NetworkSession session) {
		super(true);
		session.getChannel().getPipeline().getContext("handler").setAttachment(session);
	}
	
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if (buffer.readableBytes() > 1000) {
			channel.close();
			return null;
		}
		if (buffer.readable()) {
			int opcode = buffer.readUnsignedByte();
			int length = NetworkConstants.PACKET_SIZES[opcode];
			if (opcode < 0) {
				buffer.discardReadBytes();
				return null;
			}
			if (length == -1 && buffer.readable()) {
				length = buffer.readUnsignedByte();
			}
			if (length <= buffer.readableBytes()) {
				Packet message;
				if (length < 1) {
					message = new Packet(opcode, PacketType.STANDARD, ChannelBuffers.dynamicBuffer());
				} else {
					byte[] payload = new byte[length];
					buffer.readBytes(payload, 0, length);
					message = new Packet(opcode, PacketType.STANDARD, ChannelBuffers.wrappedBuffer(payload));
				}
				return message;
			}
		}
		return null;
	}
	
}
