package org.redrune.network.world.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.redrune.network.NetworkConstants;
import org.redrune.network.world.packet.PacketBuilder;

import java.util.List;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/19/2017
 */
public class WorldHandshakeDecoder extends ByteToMessageDecoder {
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (!in.isReadable()) {
			return;
		}
		final ChannelPipeline pipeline = ctx.pipeline().remove(this);
		
		int opcode = in.readByte() & 0xFF;
		PacketBuilder builder = new PacketBuilder();
		if (opcode == NetworkConstants.LOGIN_REQUEST) {
			builder.writeByte(0);
			pipeline.addBefore("handler", "decoder", new WorldLoginDecoder());
		} else {
			System.out.println("Received unhandled opcode: " + opcode);
		}
		
		ctx.writeAndFlush(builder.getBuffer());
		
	}
}
