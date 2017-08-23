package org.redrune.network.lobby.codec.download;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import org.redrune.cache.CacheFileStore;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/19/2017
 */
public class DownloadRequest {
	
	/**
	 * The index id of the download request
	 */
	@Getter
	private final int indexId;
	
	/**
	 * The archive the request is in
	 */
	@Getter
	private final int archiveId;
	
	/**
	 * The priority of the request
	 */
	@Getter
	private final int priority;
	
	DownloadRequest(int indexId, int archiveId, int priority) {
		this.indexId = indexId;
		this.archiveId = archiveId;
		this.priority = priority;
	}
	
	@Override
	public String toString() {
		return "DownloadRequest{" + "indexId=" + indexId + ", archiveId=" + archiveId + ", priority=" + priority + '}';
	}
	
	/**
	 * Sends the request to the channel
	 *
	 * @param ctx
	 * 		The channel handler context
	 */
	void push(ChannelHandlerContext ctx) {
		if (indexId == 255 && archiveId == 255) {
			ctx.writeAndFlush(CacheFileStore.getUkeysFile());
		} else {
			ctx.writeAndFlush(CacheFileStore.getArchivePacketData(indexId, archiveId, priority == 1));
		}
	}
}
