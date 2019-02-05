package org.redrune.networking.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.redrune.networking.NetworkSession;
import org.redrune.networking.packet.Packet;
import org.redrune.networking.packet.PacketType;
import org.redrune.utility.constants.NetworkConstants;
import org.redrune.utility.constants.PacketConstants;

import java.util.List;

/**
 * Decodes a received packet.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @author Emperor
 * @since 7/19/17
 */
public class RS2PacketDecoder extends ByteToMessageDecoder {
	
	/**
	 * Constructs a new {@code RS2GameDecoder} {@code Object}.
	 *
	 * @param session
	 * 		The session.
	 */
	public RS2PacketDecoder(NetworkSession session) {
		session.getChannel().attr(NetworkConstants.SESSION_KEY).setIfAbsent(session);
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (!in.isReadable()) {
			return;
		}
		// the opcode of the packet
		int opcode = in.readUnsignedByte();
		
		// verify opcode in bounds
		if (opcode < 0 || opcode >= PacketConstants.PACKET_SIZES.length) {
			System.out.println("packet_opcode [" + opcode + "] out of bounds");
			in.skipBytes(in.readableBytes());
			return;
		}
		// grabs the expected length
		int length = PacketConstants.PACKET_SIZES[opcode];
		// modifies the length
		if (length == -1) {
			if (in.readableBytes() >= 1) {
				length = in.readUnsignedByte() & 0xFF;
			} else {
				return;
			}
		} else if (length == -2) {
			if (in.readableBytes() >= 2) {
				length = in.readShort() & 0xFFFF;
			} else {
				return;
			}
		}
		else if (length == -4) {
			if (in.readableBytes() >= 1) {
				length = in.readableBytes();
			} else {
				return;
			}
		}
		if (in.readableBytes() >= length) {
			byte[] payload = new byte[length];
			in.readBytes(payload, 0, length);
			out.add(new Packet(opcode, PacketType.STANDARD, Unpooled.copiedBuffer(payload)));
		} else {
			System.out.println("Did not go through with " + opcode + " - " + in.readableBytes() + " - " + length);
		}
	}
	
}