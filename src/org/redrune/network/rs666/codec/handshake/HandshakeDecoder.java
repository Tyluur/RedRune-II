package org.redrune.network.rs666.codec.handshake;

import org.jboss.netty.buffer.ChannelBuffer;
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
			
			int idk1 = buffer.readShort();
			int revision = buffer.readShort();

			String email = BufferUtils.readRS2String(buffer);
			int language = buffer.readByte();
			
			System.out.println(idk1 + ", " + revision + ", " + email + ", " + language);
			response.writeByte(2);
		} else if (opcode == CREATE_ACCOUNT) {
			System.out.println("Received opcode " + opcode + ", readableBytes=[ " + buffer.readableBytes() + "]");
			
			int idk1 = buffer.readShort();
			int revision = buffer.readShort();
			
			String email = BufferUtils.readRS2String(buffer);
			int idk2 = buffer.readShort();
			String password = BufferUtils.readRS2String(buffer);
			int idk3 = buffer.readByte();
			int idk4 = buffer.readByte();
			int idk5 = buffer.readByte();
			String idk6 = null;
			if (idk5 == 1) {
				idk6 = BufferUtils.readJagString(buffer);
			}
			
			int idk7 = buffer.readByte();
			int idk8 = buffer.readByte();
			
			System.out.println(idk1 + ", " + revision + ", " + email + ", " + idk2 + ", " + password + ", " + idk3 + ", " + idk4 + ", " + idk5 + ", " + idk6 + ", " + idk7 + ", " + idk8);
			
			response.writeByte(CreationResponse.BUSY_SERVER.getValue());
			System.out.println("remaining: " + buffer.readableBytes());
		}
		return new HandshakePacket(opcode, response.toPacket());
	}
}
