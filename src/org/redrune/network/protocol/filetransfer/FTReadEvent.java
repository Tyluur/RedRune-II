package org.redrune.network.protocol.filetransfer;

import java.util.List;

import org.redrune.network.protocol.ProtocolThrottle.Protocol;
import org.redrune.network.protocol.ProtocolThrottle.ProtocolRequest;
import org.redrune.network.protocol.filetransfer.msg.FTEncryptRequestEvent;
import org.redrune.network.protocol.filetransfer.msg.FTRequestEvent;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

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
			int opcode = in.readUnsignedByte();
			switch (opcode) {
			case 0:
			case 1:
				int container = in.readUnsignedByte();
				int archive = in.readInt();
				out.add(new FTRequestEvent(container, archive, opcode == 1));
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
