package org.redrune.network.protocol.filetransfer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.redrune.network.protocol.ProtocolThrottle.Protocol;
import org.redrune.network.protocol.ProtocolThrottle.ProtocolRequest;
import org.redrune.network.protocol.filetransfer.msg.FTEncryptRequestEvent;
import org.redrune.network.protocol.filetransfer.msg.FTRequestEvent;

import java.util.List;

/**
 * FTReadEvent.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
@ProtocolRequest(request = Protocol.REQUEST_FILE_TRANSFER)
public class FTReadEvent extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() < 6) {
			return;
		}
		if (in.isReadable()) {
			int priority = in.readByte() & 255;
			System.out.println("priority=" + priority);
			switch (priority) {
			case 0:
			case 1:
				int container = in.readByte() & 0xFF;
				int archive = in.readShort() & 0xFFFF;
				System.out.println(container + "," + archive);
				out.add(new FTRequestEvent(container, archive, priority == 1));
				break;
			case 4:
				int key = in.readUnsignedByte();
				in.readerIndex(in.readerIndex() + 2);
				out.add(new FTEncryptRequestEvent(key));
				break;
			case 7:
				ctx.channel().disconnect().sync();
				break;
			default:
				in.readUnsignedByte();
				in.readInt();
				break;
			}
		}
	}

}
