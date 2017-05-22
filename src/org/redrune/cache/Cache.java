package org.redrune.cache;

import java.io.IOException;

import org.redrune.rs2.GameConstants;

import com.alex.io.OutputStream;
import com.alex.store.Store;
import com.alex.util.whirlpool.Whirlpool;

import lombok.Getter;

public final class Cache {

	@Getter
	public static Store STORE;

	private Cache() {

	}

	public static void init() throws IOException {
		STORE = new Store(GameConstants.CACHE_PATH);
	}

	public static void main(String... args) throws IOException {
		init();
		StringBuilder bldr = new StringBuilder();
		int total = 0;
		for (byte b : STORE.generateIndex255Archive255()) {
			bldr.append(b + ",");
			System.out.println(total = total + b);
		}
		System.out.println(bldr);
	}

	public static final byte[] generateUkeysFile() {
		OutputStream stream = new OutputStream();
		stream.writeByte(STORE.getIndexes().length);
		for (int index = 0; index < STORE.getIndexes().length; index++) {
			if (STORE.getIndexes()[index] == null) {
				stream.writeInt(0);
				stream.writeInt(0);
				stream.writeBytes(new byte[64]);
				continue;
			}
			stream.writeInt(STORE.getIndexes()[index].getCRC());
			stream.writeInt(STORE.getIndexes()[index].getTable().getRevision());
			stream.writeBytes(STORE.getIndexes()[index].getWhirlpool());
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
