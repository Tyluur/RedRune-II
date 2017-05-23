package org.redrune.network.protocol.handshake;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.redrune.network.NetworkConstants;
import org.redrune.network.protocol.ProtocolThrottle.Protocol;
import org.redrune.network.protocol.ProtocolThrottle.ProtocolRequest;
import org.redrune.network.protocol.handshake.msg.HSRequestEvent;

import java.util.List;

/**
 * HSReadEvent.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
@ProtocolRequest(request = Protocol.REQUEST_HANDSHAKE)
public class HSReadEvent extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() > 2) {
			int revision = in.readInt();
			if (revision != NetworkConstants.REVISION) {
				ctx.channel().disconnect().sync();
				System.out.println("Closed the channel, revision=" + revision + ".");
				return;
			}
			out.add(new HSRequestEvent(revision));
			System.out.println(revision + " handshake completed.");
		} else {
			System.out.println(in.readableBytes());
		}
	}

}
