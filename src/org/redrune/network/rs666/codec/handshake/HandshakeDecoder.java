package org.redrune.network.rs666.codec.handshake;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.redrune.network.rs666.codec.js5.JS5Decoder;
import org.redrune.network.rs666.codec.login.RS2LoginDecoder;
import org.redrune.network.rs666.packet.PacketBuilder;
import org.redrune.utility.backend.CreationResponse;
import org.redrune.utility.io.BufferUtils;

import static org.redrune.network.NetworkConstants.*;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public final class HandshakeDecoder extends FrameDecoder {
	
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if (ctx.getPipeline().get(HandshakeDecoder.class) != null) {
			ctx.getPipeline().remove(this);
		}
		int opcode = buffer.readByte() & 0xFF;
		PacketBuilder response = new PacketBuilder();
		if (opcode == JS5_REQUEST) {
			int version = buffer.readInt();
			if (version != REVISION) {
				response.writeByte((byte) 6);
			} else {
				response.writeByte((byte) 0);
				for (int i = 0; i < 27; i++) {
					response.writeInt(DATA[i]);
				}
				ctx.getPipeline().addBefore("handler", "decoder", new JS5Decoder());
			}
		} else if (opcode == LOGIN_REQUEST) {
			ctx.getPipeline().addBefore("handler", "decoder", new RS2LoginDecoder());
			response.writeByte((byte) 0);
		} else if (opcode == EMAIL_VERIFICATION) {
			System.out.println("Received opcode " + opcode);
			
			response.writeByte(2);
		} else if (opcode == CREATE_ACCOUNT) {
			System.out.println("Received opcode " + opcode + ", readableBytes=[ " + buffer.readableBytes() + "]");
			
			int[] keys = new int[4];
			for (int i = 0; i < keys.length; i++) {
				keys[i] = buffer.readInt();
			}
			
			buffer.readShort();
			buffer.readShort();
			
			buffer.readByte();
			for (int i = 0; i < 14; i++) {
				buffer.readInt();
			}
			buffer.readShort();
			
			byte[] block = new byte[BufferUtils.readableBytes(buffer)];
			buffer.readBytes(block);
			ChannelBuffer decryptedPayload = ChannelBuffers.wrappedBuffer(BufferUtils.decrypt(keys, block, 0, block.length));
			String email = BufferUtils.readRS2String(decryptedPayload).toLowerCase();
			
			System.out.println("\t" + email);
			buffer.readShort();
			System.out.println("\t" + BufferUtils.readRS2String(buffer));
			buffer.readByte();
			buffer.readByte();
			buffer.readByte();
			
			System.out.println("Finished reading, bytes=" + buffer.readableBytes());
			
			response.writeByte(CreationResponse.BUSY_SERVER.getValue());
		}
		return new HandshakePacket(opcode, response.toPacket());
	}
}
