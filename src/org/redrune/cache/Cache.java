package org.redrune.cache;

import com.alex.io.OutputStream;
import com.alex.store.Store;
import com.alex.util.whirlpool.Whirlpool;
import lombok.Getter;
import org.redrune.rs2.GameConstants;

import java.io.IOException;

public final class Cache {

	@Getter
	public static Store store;

	private Cache() {

	}

	public static void init() throws IOException {
		store = new Store(GameConstants.CACHE_PATH);
	}

	public static byte[] generateUkeysFile() {
		OutputStream stream = new OutputStream();
		stream.writeByte(store.getIndexes().length);
		for (int index = 0; index < store.getIndexes().length; index++) {
			if (store.getIndexes()[index] == null) {
				stream.writeInt(0);
				stream.writeInt(0);
				stream.writeBytes(new byte[64]);
				continue;
			}
			stream.writeInt(store.getIndexes()[index].getCRC());
			stream.writeInt(store.getIndexes()[index].getTable().getRevision());
			stream.writeBytes(store.getIndexes()[index].getWhirlpool());
		}
		byte[] archive = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.getBytes(archive, 0, archive.length);
		OutputStream hashStream = new OutputStream(65);
		hashStream.writeByte(0);
		hashStream.writeBytes(Whirlpool.getHash(archive, 0, archive.length));
		byte[] hash = new byte[hashStream.getOffset()];
		hashStream.setOffset(0);
		hashStream.getBytes(hash, 0, hash.length);
		stream.writeBytes(hash);
		archive = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.getBytes(archive, 0, archive.length);
		return archive;
	}

}
