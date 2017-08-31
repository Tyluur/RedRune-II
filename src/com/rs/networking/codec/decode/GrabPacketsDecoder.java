package com.rs.networking.codec.decode;

import com.rs.cache.Cache;
import com.rs.networking.codec.Decoder;
import com.rs.networking.io.InputStream;
import com.rs.networking.Session;

public final class GrabPacketsDecoder extends Decoder {
	
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
	
	private void decodeRequestCacheContainer(InputStream stream, int packetId) {
		int indexId = stream.readUnsignedByte();
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
		}
	}
	
}
