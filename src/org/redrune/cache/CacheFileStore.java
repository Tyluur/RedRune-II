package org.redrune.cache;

import com.alex.store.Store;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.redrune.game.GameConstants;

import java.io.IOException;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/15/2017
 */
public final class CacheFileStore {
	
	/**
	 * The archive with all data.
	 */
	public static Store STORE;
	
	/**
	 * The update keys
	 */
	private static byte[] ukeysFile;
	
	/**
	 * Private constructor
	 */
	private CacheFileStore() {
	
	}
	
	/**
	 * Generates the file store repository
	 */
	public static void generateRepository() throws IOException {
		STORE = new Store(GameConstants.CACHE_PATH);
	}
	
	/**
	 * Gets the size of the item definition index
	 */
	public static int getItemDefinitionsSize() {
		int lastArchiveId = STORE.getIndexes()[19].getLastArchiveId();
		return lastArchiveId * 256 + STORE.getIndexes()[19].getValidFilesCount(lastArchiveId);
	}
	
	/**
	 * Gets the size of the interface definition index
	 */
	public static int getInterfaceDefinitionsSize() {
		return STORE.getIndexes()[3].getLastArchiveId() + 1;
	}
	
	/**
	 * Gets the size of the graphic definition index
	 */
	public static int getGraphicDefinitionsSize() {
		int lastArchiveId = STORE.getIndexes()[21].getLastArchiveId();
		return lastArchiveId * 256 + STORE.getIndexes()[21].getValidFilesCount(lastArchiveId);
	}
	
	/**
	 * Gets the size of the animation definition index
	 */
	public static int getAnimationDefinitionsSize() {
		int lastArchiveId = STORE.getIndexes()[20].getLastArchiveId();
		return lastArchiveId * 128 + STORE.getIndexes()[20].getValidFilesCount(lastArchiveId);
	}
	
	/**
	 * Gets the size of the config definitions index
	 */
	public static int getConfigDefinitionsSize() {
		int lastArchiveId = STORE.getIndexes()[22].getLastArchiveId();
		return lastArchiveId * 256 + STORE.getIndexes()[22].getValidFilesCount(lastArchiveId);
	}
	
	/**
	 * Gets the size of the object definitions index
	 */
	public static int getObjectDefinitionsSize() {
		int lastArchiveId = STORE.getIndexes()[16].getLastArchiveId();
		return lastArchiveId * 256 + STORE.getIndexes()[16].getValidFilesCount(lastArchiveId);
	}
	
	/**
	 * Gets the size of the npc definitions index
	 */
	public static int getNPCDefinitionsSize() {
		int lastArchiveId = STORE.getIndexes()[18].getLastArchiveId();
		return lastArchiveId * 128 + STORE.getIndexes()[18].getValidFilesCount(lastArchiveId);
	}
	
	/**
	 * Gets the amount of components an interface has
	 *
	 * @param interfaceId
	 * 		The id of the interface
	 */
	public static int getAmountOfComponents(int interfaceId) {
		return STORE.getIndexes()[3].getLastFileId(interfaceId) + 1;
	}
	
	/**
	 * Gets the packet data to send for a requested archive
	 *
	 * @param indexId
	 * 		The index requested
	 * @param archiveId
	 * 		The id of the archive requested
	 * @param priority
	 * 		If the request is a priority request
	 */
	public static ByteBuf getArchivePacketData(int indexId, int archiveId, boolean priority) {
		Store store = CacheFileStore.STORE;
		byte[] archive = (indexId == 255 ? store.getIndex255() : store.getIndexes()[indexId].getMainFile()).getArchiveData(archiveId);
		if (archive == null) {
			return null;
		}
		int compression = archive[0] & 0xff;
		int length = ((archive[1] & 0xff) << 24) + ((archive[2] & 0xff) << 16) + ((archive[3] & 0xff) << 8) + (archive[4] & 0xff);
		int settings = compression;
		if (!priority) {
			settings |= 0x80;
		}
		ByteBuf buffer = Unpooled.buffer();
		buffer.writeByte(indexId);
		buffer.writeShort(archiveId);
		buffer.writeByte(settings);
		buffer.writeInt(length);
		int realLength = compression != 0 ? length + 4 : length;
		for (int index = 5; index < realLength + 5; index++) {
			if (buffer.writerIndex() % 512 == 0) {
				buffer.writeByte(255);
			}
			buffer.writeByte(archive[index]);
		}
		return buffer;
	}
	
	/**
	 * Gets the update keys file and if it doesn't exist we create one
	 */
	public static ByteBuf getUkeysFile() {
		if (ukeysFile == null) {
			ukeysFile = generateUkeysFile();
		}
		return getContainerPacketData(255, 255, ukeysFile);
	}
	
	/**
	 * Generates an update keys file
	 */
	public static byte[] generateUkeysFile() {
		return STORE.generateIndex255Archive255Current(null, null);
	}
	
	/**
	 * Gets the packet data in an index and archive
	 *
	 * @param indexFileId
	 * 		The index
	 * @param archiveId
	 * 		The id of the archive
	 * @param archive
	 * 		The archive data
	 */
	public static ByteBuf getContainerPacketData(int indexFileId, int archiveId, byte[] archive) {
		ByteBuf stream = Unpooled.buffer(archive.length + 4);
		stream.writeByte(indexFileId);
		stream.writeShort(archiveId);
		stream.writeByte(0);
		stream.writeInt(archive.length);
		for (int index = 0; index < archive.length; index++) {
			if (stream.writerIndex() % 512 == 0) {
				stream.writeByte(255);
			}
			stream.writeByte(archive[index]);
		}
		return stream;
	}
}
