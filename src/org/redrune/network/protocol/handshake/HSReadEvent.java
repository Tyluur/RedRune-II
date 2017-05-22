package org.redrune.network.protocol.handshake;

import java.util.List;

import org.redrune.network.protocol.ProtocolThrottle.Protocol;
import org.redrune.network.protocol.ProtocolThrottle.ProtocolRequest;
import org.redrune.network.protocol.handshake.msg.HSRequestEvent;
import org.redrune.utility.io.BufferUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * HSReadEvent.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
@ProtocolRequest(request = Protocol.REQUEST_HANDSHAKE)
public class HSReadEvent extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.isReadable()) {
			int size = in.readUnsignedByte();
			if (size != in.readableBytes()) {
				ctx.channel().disconnect().sync();
				return;
			}

			int major = in.readInt();

			int minor = in.readInt();

			String key = BufferUtils.readString(in);
			System.out.println(key);
			out.add(new HSRequestEvent(major, minor, key));
		}
	}

}
