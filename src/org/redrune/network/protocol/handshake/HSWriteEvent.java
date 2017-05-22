package org.redrune.network.protocol.handshake;

import org.redrune.network.protocol.Protocol;
import org.redrune.network.protocol.ProtocolResponse;
import org.redrune.network.protocol.handshake.msg.HSResponseEvent;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * HSWriteEvent.java
 * 
 * @author Chryonic May 22, 2017 | RedRune
 */
public class HSWriteEvent extends MessageToByteEncoder<HSResponseEvent> {

	public HSWriteEvent() {
		super(HSResponseEvent.class);
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, HSResponseEvent event, ByteBuf out) throws Exception {

		out.writeByte(event.getResponse().getClientId());
		if (event.getResponse().equals(ProtocolResponse.SUCCESSFUL_CONNECTION)) {

			for (int key : Protocol.UKEYS) {
				out.writeInt(key);
			}
		}
	}

}
