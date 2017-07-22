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
		// remove this first from the pipeline
		final ChannelPipeline pipeline = ctx.pipeline().remove(this);
		// the opcode to transfer handshake
		int opcode = in.readByte() & 0xFF;
		// the builder we will write to
		PacketBuilder builder = new PacketBuilder();
		// we only care about login requests in the world
		if (opcode == NetworkConstants.LOGIN_REQUEST) {
			builder.writeByte(0);
			// transfer the decoder over to the world login
			pipeline.addBefore("handler", "decoder", new WorldLoginDecoder());
		} else {
			System.out.println("Received unhandled opcode: " + opcode);
		}
		ctx.writeAndFlush(builder.getBuffer());
	}
}
