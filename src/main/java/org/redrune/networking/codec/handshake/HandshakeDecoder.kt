package org.redrune.networking.codec.handshake;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.redrune.networking.codec.js5.UpdateServerDecoder;
import org.redrune.networking.codec.login.RS2LoginDecoder;
import org.redrune.networking.packet.PacketBuilder;
import org.redrune.utility.constants.NetworkConstants;

import java.util.List;

import static org.redrune.utility.constants.NetworkConstants.JS5_REQUEST;
import static org.redrune.utility.constants.NetworkConstants.LOGIN_REQUEST;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-02
 */
public class HandshakeDecoder extends ByteToMessageDecoder {
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		// removes the pipeline
		final ChannelPipeline pipeline = ctx.pipeline().remove(this);
		// the protocol id
		final int id = in.readByte() & 0xFF;
		// constructs a new bldr
		PacketBuilder builder = new PacketBuilder();
		switch (id) {
			case JS5_REQUEST:
				int version = in.readInt();
				if (version == NetworkConstants.PROTOCOL_NUMBER) {
					builder.writeByte((byte) 0);
					for (int i = 0; i < 27; i++) {
						builder.writeInt(NetworkConstants.GRAB_SERVER_KEYS[i]);
					}
					pipeline.addBefore("handler", "decoder", new UpdateServerDecoder());
				} else {
					builder.writeByte((byte) 6);
				}
				break;
			case LOGIN_REQUEST:
				builder.writeByte(0);
				pipeline.addBefore("handler", "decoder", new RS2LoginDecoder());
				break;
		}
		ctx.writeAndFlush(builder.getBuffer());
	}
}
