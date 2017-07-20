package org.redrune.network.world.codec.io;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.redrune.network.NetworkConstants;
import org.redrune.network.NetworkSession;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.Packet.PacketType;

import java.util.List;

/**
 * Decodes a received packet.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @author Dementhium development team
 * @author Emperor
 * @since 7/19/17
 */
public class RSPacketDecoder extends ByteToMessageDecoder {
	
	/**
	 * Constructs a new {@code RS2GameDecoder} {@code Object}.
	 *
	 * @param session
	 * 		The networkSession.
	 */
	public RSPacketDecoder(NetworkSession session) {
		session.getChannel().attr(NetworkConstants.SESSION_KEY).setIfAbsent(session);
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		// the session
		NetworkSession session = ctx.channel().attr(NetworkConstants.SESSION_KEY).get();
		int opcode = in.readUnsignedByte();
		if (opcode < 0) {
			in.discardReadBytes();
			return;
		}
		opcode = (opcode - session.getInCipher().getNextValue()) & 0xFF;
		int length = NetworkConstants.PACKET_SIZES[opcode];
		if (length == -1 && in.isReadable()) {
			length = in.readUnsignedByte();
		}
		if (length <= in.readableBytes()) {
			Packet packet;
			if (length < 1) {
				packet = new Packet(opcode, PacketType.STANDARD, Unpooled.buffer());
			} else {
				byte[] payload = new byte[length];
				in.readBytes(payload, 0, length);
				packet = new Packet(opcode, PacketType.STANDARD, Unpooled.wrappedBuffer(payload));
			}
			out.add(packet);
		}
	}
}
