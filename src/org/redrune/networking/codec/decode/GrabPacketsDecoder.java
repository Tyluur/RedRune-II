package org.redrune.networking.codec.decode;

import org.redrune.cache.Cache;
import org.redrune.engine.thread.factory.JS5ThreadFactory;
import org.redrune.networking.Session;
import org.redrune.networking.codec.Decoder;
import org.redrune.networking.stream.InputStream;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class GrabPacketsDecoder extends Decoder {
	
	@SuppressWarnings("unused")
	private static ExecutorService updateService = Executors.newFixedThreadPool(1);
	
	private static ExecutorService worker = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new JS5ThreadFactory("JS5-Worker"));
	
	private LinkedList<String> requests = new LinkedList<String>();
	
	public GrabPacketsDecoder(Session session) {
		super(session);
	}
	
	@Override
	public final void decode(InputStream stream) {
		while (stream.getRemaining() > 0 && session.getChannel().isConnected()) {
			int packetId = stream.readUnsignedByte();
			decodeRequestCacheContainer(stream, packetId);
		}
	}
	
	private void decodeRequestCacheContainer(InputStream stream, final int priority) {
		final int indexId = stream.readUnsignedByte();
		final int archiveId = stream.readUnsignedShort();
		if (archiveId < 0) {
			return;
		}
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
				requests.add(indexId + "," + archiveId);
				break;
			case 1:
				worker.submit(() -> session.getGrabPackets().sendCacheArchive(indexId, archiveId, true));
				break;
			case 2:
			case 3:
				requests.clear();
				break;
			default:
				System.out.println("[priority=" + priority + "]");
				break;
		}
		while (requests.size() > 0) {
			String[] request = requests.removeFirst().split(",");
			session.getGrabPackets().sendCacheArchive(Integer.parseInt(request[0]), Integer.parseInt(request[1]), false);
		}
/*		int indexId = stream.readUnsignedByte();
		int archiveId = stream.readUnsignedShort();
		if (indexId != 255) {
			if (Cache.STORE.getIndexes().length <= indexId || Cache.STORE.getIndexes()[indexId] == null || !Cache.STORE.getIndexes()[indexId].archiveExists(archiveId)) {
				return;
			}
		} else if (archiveId != 255) {
			if (Cache.STORE.getIndexes().length <= archiveId || Cache.STORE.getIndexes()[archiveId] == null) {
				return;
			}
		}
		if (packetId == 0 || packetId == 1) {
			session.getGrabPackets().sendCacheArchive(indexId, archiveId, true);
		}*/
	}
	
}
