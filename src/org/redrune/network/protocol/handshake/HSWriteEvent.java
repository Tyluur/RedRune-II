package org.redrune.network.protocol.handshake;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.redrune.network.NetworkConstants;
import org.redrune.network.protocol.handshake.msg.HSResponseEvent;

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
		for (int key : NetworkConstants.DATA) {
			out.writeInt(key);
		}
		out.writeByte(event.getResponse().getClientId());
	}
	
}
