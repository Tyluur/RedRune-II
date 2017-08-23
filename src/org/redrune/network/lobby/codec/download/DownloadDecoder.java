package org.redrune.network.lobby.codec.download;

import com.alex.store.Index;
import com.alex.store.Store;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.redrune.cache.CacheFileStore;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public class DownloadDecoder extends ByteToMessageDecoder {
	
	/**
	 * The download requests
	 */
	private LinkedList<DownloadRequest> requests = new LinkedList<>();
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		while (in.readableBytes() > 0) {
			serveRequest(ctx, in, in.readByte() & 0xFF);
		}
		requests.clear();
	}
	
	/**
	 * Serves the request
	 *
	 * @param ctx
	 * 		The channel context
	 * @param in
	 * 		The buffer
	 * @param priority
	 * 		The priority of the request
	 */
	private void serveRequest(ChannelHandlerContext ctx, ByteBuf in, int priority) {
		final int indexId = in.readByte() & 0xFF;
		final int archiveId = in.readShort() & 0xFFFF;
		
		final Store store = CacheFileStore.STORE;
		final Index[] indexes = store.getIndexes();
		if (indexId != 255) {
			if (indexes.length <= indexId || indexes[indexId] == null || !store.getIndexes()[indexId].archiveExists(archiveId)) {
				System.out.println("Unable to find index " + indexId + ".");
				return;
			}
		} else if (archiveId != 255) {
			if (indexes.length <= archiveId || indexes[archiveId] == null) {
				System.out.println("Unable to find archive " + archiveId + ".");
				return;
			}
		}
		final DownloadRequest request = new DownloadRequest(indexId, archiveId, priority);
		switch (priority) {
			case 0:
				requests.addLast(request);
				break;
			case 1:
				requests.addFirst(request);
				break;
			case 2:
			case 3:
				requests.clear();
				break;
			default:
				break;
		}
		while (!requests.isEmpty()) {
			DownloadRequest downloadRequest = requests.poll();
			downloadRequest.push(ctx);
		}
	}
	
}