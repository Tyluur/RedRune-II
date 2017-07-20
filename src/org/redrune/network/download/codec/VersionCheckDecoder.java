package org.redrune.network.download.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.redrune.network.NetworkConstants;
import org.redrune.network.world.packet.PacketBuilder;

import java.util.List;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public final class VersionCheckDecoder extends ByteToMessageDecoder {
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		// removes the handshake decoder instance from pipeline [new decoder is coming anyway]
		final ChannelPipeline pipeline = ctx.pipeline();
		if (pipeline.get(VersionCheckDecoder.class) != null) {
			pipeline.remove(this);
		}
		int opcode = in.readByte() & 0xFF;
		
		PacketBuilder response = new PacketBuilder();
		if (opcode == NetworkConstants.JS5_REQUEST) {
			int version = in.readInt();
			if (version != NetworkConstants.REVISION) {
				response.writeByte((byte) 6);
			} else {
				response.writeByte((byte) 0);
				for (int i = 0; i < 27; i++) {
					response.writeInt(NetworkConstants.DATA[i]);
				}
				pipeline.addBefore("handler", "decoder", new DownloadDecoder());
			}
		} else {
			System.out.println("Received unhandled opcode: " + opcode);
		}
		
		ctx.writeAndFlush(response.getBuffer());
	}
	
/*	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if (ctx.getPipeline().get(HandshakeDecoder.class) != null) {
			ctx.getPipeline().remove(this);
		}
		int opcode = buffer.readByte() & 0xFF;
		PacketBuilder response = new PacketBuilder();
		if (opcode == NetworkConstants.JS5_REQUEST) {
			int version = buffer.readInt();
			if (version != NetworkConstants.REVISION) {
				response.writeByte((byte) 6);
			} else {
				response.writeByte((byte) 0);
				for (int i = 0; i < 27; i++) {
					response.writeInt(NetworkConstants.DATA[i]);
				}
				ctx.getPipeline().addBefore("handler", "decoder", new JS5Decoder());
			}
		} else if (opcode == NetworkConstants.LOGIN_REQUEST) {
			ctx.getPipeline().addBefore("handler", "decoder", new RS2LoginDecoder());
			response.writeByte((byte) 0);
		} else if (opcode == NetworkConstants.EMAIL_VERIFICATION) {
			System.out.println("Received opcode " + opcode);
			
			int idk1 = buffer.readShort();
			int revision = buffer.readShort();
			
			String email = BufferUtils.readRS2String(buffer);
			int language = buffer.readByte();
			
			System.out.println(idk1 + ", " + revision + ", " + email + ", " + language);
			response.writeByte(2);
		} else if (opcode == NetworkConstants.CREATE_ACCOUNT) {
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
				idk6 = BufferUtils.readRS2String(buffer);
			}
			
			int idk7 = buffer.readByte();
			int idk8 = buffer.readByte();
			
			System.out.println(idk1 + ", " + revision + ", " + email + ", " + idk2 + ", " + password + ", " + idk3 + ", " + idk4 + ", " + idk5 + ", " + idk6 + ", " + idk7 + ", " + idk8);
			
			response.writeByte(CreationResponse.BUSY_SERVER.getValue());
			System.out.println("remaining: " + buffer.readableBytes());
		}
		return new HandshakePacket(opcode, response.toPacket());
	}*/
	
}
