package org.redrune.networking.codec.js5;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.redrune.cache.Cache;
import org.redrune.engine.factory.JS5ThreadFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-02
 */
public class UpdateServerDecoder extends ByteToMessageDecoder {
	
	/**
	 * The service used explicitly for update server transmission
	 */
	private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new JS5ThreadFactory("JS5-Worker"));
	
	/**
	 * The list of requests
	 */
	private final LinkedList<UpdateServerRequest> requests = new LinkedList<>();
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		while (in.readableBytes() >= 4) {
			int priority = in.readByte() & 0xFF;
			serveRequest(ctx, in, priority);
		}
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
		if (indexId != 255) {
			if (Cache.STORE.getIndexes().length <= indexId || Cache.STORE.getIndexes()[indexId] == null || !Cache.STORE.getIndexes()[indexId].archiveExists(archiveId)) {
				return;
			}
		} else if (archiveId != 255) {
			if (Cache.STORE.getIndexes().length <= archiveId || Cache.STORE.getIndexes()[archiveId] == null) {
				return;
			}
		}
		switch (priority) {
			case 0:
				requests.add(new UpdateServerRequest(indexId, archiveId, false));
				break;
			case 1:
				EXECUTOR_SERVICE.submit(() -> ctx.writeAndFlush(Cache.getCacheArchive(indexId, archiveId, true)));
				break;
			case 2:
			case 3:
				requests.clear();
				break;
		}
		while (requests.size() > 0) {
			UpdateServerRequest request = requests.removeFirst();
			ctx.writeAndFlush(Cache.getCacheArchive(request.getIndexId(), request.getArchiveId(), request.isPriority()));
		}
	}
}
