package org.redrune.network.world.codec.io;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.redrune.cache.crypto.ISAACCipher;
import org.redrune.network.NetworkConstants;
import org.redrune.network.NetworkSession;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.Packet.PacketType;

/**
 * This encodes a packet going to the rs client. All packets have a specific header and data is encoded differently
 * based on the type of packet being encoded. This class handles all said operations.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
@Sharable
public final class RSPacketEncoder extends MessageToByteEncoder<Packet> {
	
	@Override
	protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) throws Exception {
		// the session
		NetworkSession session = ctx.channel().attr(NetworkConstants.SESSION_KEY).get();
		// the encoded response
		ByteBuf response;
		if (packet.isRaw()) {
			response = packet.getBuffer();
		} else {
			int packetLength = packet.getBuffer().readableBytes() + 4;
			response = Unpooled.buffer(packetLength);
			int opcode = packet.getOpcode();
			final ISAACCipher outCipher = session.getOutCipher();
			if (outCipher == null) {
				writeSmartByte(response, opcode);
			} else {
				if (opcode >= 128) {
					writeSecureByte(response, outCipher, (opcode >> 8) + 128);
				}
				writeSecureByte(response, outCipher, opcode);
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
		}
		ctx.writeAndFlush(response);
	}
	
	/**
	 * Writes a smart byte to the buffer
	 *
	 * @param buffer
	 * 		The buffer
	 * @param value
	 * 		The value to write
	 */
	private void writeSmartByte(ByteBuf buffer, int value) {
		if (value >= 128) {
			buffer.writeByte((value >> 8) + 128);
			buffer.writeByte(value);
		} else {
			buffer.writeByte(value);
		}
	}
	
	/**
	 * Writes a value to the buffer as a byte using isaac encryption
	 *
	 * @param buffer
	 * 		The buffer
	 * @param cipher
	 * 		The cipher
	 * @param value
	 * 		The value
	 */
	public void writeSecureByte(ByteBuf buffer, ISAACCipher cipher, int value) {
		buffer.writeByte(value + cipher.getNextValue());
	}
}