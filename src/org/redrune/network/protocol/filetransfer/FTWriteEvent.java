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
 *
 * @author Chryonic May 22, 2017 | RedRune
 */
public class FTWriteEvent extends MessageToByteEncoder<FTResponseEvent> {
	
	private byte[] data;
	
	public FTWriteEvent() {
		super(FTResponseEvent.class);
	}
	
	@Override
	protected void encode(ChannelHandlerContext ctx, FTResponseEvent event, ByteBuf out) throws Exception {
		System.out.println(event.getContainer() + ", " + event.getArchive());
		if (event.getContainer() == 255 && event.getArchive() == 255) {
			getArchiveRequestData(out);
		} else {
			getArchiveRequestData(event.getContainer(), event.getArchive(), event.isPriority(), out);
		}
	}
	
	private ByteBuf getArchiveRequestData(ByteBuf out) {
		if (data == null) {
			data = Cache.generateUkeysFile();
		}
		out.writeByte((byte) 255);
		out.writeInt(255);
		out.writeByte((byte) 0);
		out.writeInt(data.length);
		int offset = 10;
		for (byte aData : data) {
			if (offset == 512) {
				out.writeByte((byte) 255);
				offset = 1;
			}
			out.writeByte(aData);
			offset++;
		}
		return out;
	}
	
	private ByteBuf getArchiveRequestData(int container, int archive, boolean priority, ByteBuf out) {
		MainFile cache = container == 255 ? Cache.getStore().getIndex255() : Cache.getStore().getIndexes()[container].getMainFile();
		ByteBuf archiveBuffer = Unpooled.copiedBuffer(cache.getArchiveData(archive));
		int compression = archiveBuffer.readUnsignedByte();
		int length = archiveBuffer.readInt();
		int settings = compression;
		if (!priority) {
			settings |= 0x80;
		}
		out.writeByte((byte) container);
		out.writeInt(archive);
		out.writeByte((byte) settings);
		out.writeInt(length);
		int realLength = compression != 0 ? length + 4 : length;
		for (int offset = 5; offset < realLength + 5; offset++) {
			if (out.writerIndex() % 512 == 0) {
				out.writeByte((byte) 255);
			}
			out.writeByte(archiveBuffer.array()[offset]);
		}
		return out;
	}
	
}
