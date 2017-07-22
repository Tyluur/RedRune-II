package org.redrune.network.lobby.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.redrune.network.NetworkConstants;
import org.redrune.network.lobby.codec.download.DownloadDecoder;
import org.redrune.network.world.packet.PacketBuilder;

import java.util.List;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/19/2017
 */
public class RequestTypeVerificationDecoder extends ByteToMessageDecoder {
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (!in.isReadable()) {
			return;
		}
		final ChannelPipeline pipeline = ctx.pipeline().remove(this);
		
		int opcode = in.readByte() & 0xFF;
		PacketBuilder builder = new PacketBuilder();
		if (opcode == NetworkConstants.JS5_REQUEST) {
			int version = in.readInt();
			if (version != NetworkConstants.REVISION) {
				builder.writeByte((byte) 6);
			} else {
				builder.writeByte((byte) 0);
				for (int i = 0; i < 27; i++) {
					builder.writeInt(NetworkConstants.DATA[i]);
				}
				pipeline.addBefore("handler", "decoder", new DownloadDecoder());
			}
		} else if (opcode == NetworkConstants.LOGIN_REQUEST) {
			builder.writeByte(0);
			pipeline.addBefore("handler", "decoder", new LobbyLoginDecoder());
		} else {
			System.out.println("Received unhandled opcode: " + opcode);
		}
		
		ctx.writeAndFlush(builder.getBuffer());
	}
}
