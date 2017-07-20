package org.redrune.network.master.network.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.redrune.network.master.network.packet.IncomingPacket;

import java.util.List;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
public class MasterDecoder extends ByteToMessageDecoder {
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		try {
			// make sure we can read it first
			if (!in.isReadable()) {
				System.out.println("unreadable bytebuf");
				return;
			}
			// marks the index
			in.markReaderIndex();
			
			// decode the buffer data
			short length = in.readShort();
			
			// good length check
			if (in.readableBytes() >= length) {
				int id = in.readInt();
				byte[] buffer = new byte[length];
				
				// store the buffer data
				in.readBytes(buffer, 0, length);
				
				// convert the buffer to a packet object now.
				out.add(new IncomingPacket(id, buffer));
			} else {
				in.resetReaderIndex();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
