package org.redrune.network.protocol.filetransfer;

import org.redrune.cache.Cache;
import org.redrune.network.protocol.filetransfer.msg.FTResponseEvent;

import com.alex.store.MainFile;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * FTWriteEvent.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public class FTWriteEvent extends MessageToByteEncoder<FTResponseEvent> {

	private byte[] data;

	public FTWriteEvent() {
		super(FTResponseEvent.class);
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, FTResponseEvent event, ByteBuf out) throws Exception {
		if (event.getContainer() == 0xff && event.getArchive() == 0xff) {
			getArchiveRequestData(out);
		} else {
			getArchiveRequestData(event.getContainer(), event.getArchive(), event.isPriority(), out);
		}
	}

	private ByteBuf getArchiveRequestData(int container, int archive, boolean priority, ByteBuf out) {
		MainFile cache = container == 0xff ? Cache.getSTORE().getIndex255()
				: Cache.getSTORE().getIndexes()[container].getMainFile();
		ByteBuf archiveBuffer = Unpooled.copiedBuffer(cache.getArchiveData(archive));
		int compression = archiveBuffer.readUnsignedByte();
		int length = archiveBuffer.readInt();
		int settings = compression;
		if (!priority) {
			settings |= 0x80;
		}
		int realLength = compression != 0 ? length + 4 : length;
		out.writeByte((byte) container);
		out.writeInt(archive);
		out.writeByte((byte) settings);
		out.writeInt(length);
		for (int index = 5; index < realLength + 5; index++) {
			if (out.writerIndex() % 0x200 == 0) {
				out.writeByte((byte) 0xff);
			}
			out.writeByte(archiveBuffer.array()[index]);
		}
		return out;
	}

	private ByteBuf getArchiveRequestData(ByteBuf out) {
		if (data == null) {
			data = Cache.generateUkeysFile();
		}
		out.writeByte((byte) 0xff);
		out.writeInt(0xff);
		out.writeByte((byte) 0);
		out.writeInt(data.length);
		int offset = 10;
		for (int index = 0; index < data.length; index++) {
			if (offset == 0x200) {
				out.writeByte((byte) 0xff);
				offset = 1;
			}
			out.writeByte(data[index]);
			offset++;
		}
		return out;
	}

}
