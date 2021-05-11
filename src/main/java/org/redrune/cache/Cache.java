package org.redrune.cache;

import com.alex.store.Store;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.redrune.networking.packet.Packet;
import org.redrune.networking.packet.PacketBuilder;
import org.redrune.utility.constants.GameConstants;

import java.io.IOException;

public final class Cache {

    /**
     * The instance of the cache reading factory
     */
    public static Store STORE;

    /**
     * The byte array with update keys for the js5 initialization
     */
    private static byte[] ukeysFile;

    private Cache() {

    }

    /**
     * Initializing the cache reading factory
     */
    public static void initialize() throws IOException {
        STORE = new Store(GameConstants.CACHE_PATH);
    }

    /**
     * Gets the {@code Packet} instance of the cache archive located in the parameterized places
     *
     * @param indexId   The index to look for data in
     * @param archiveId The archive to look for data in
     * @param priority  If priority should be applied
     */
    public static ByteBuf getCacheArchive(int indexId, int archiveId, boolean priority) {
        if (indexId == 255 && archiveId == 255) {
            return getUkeysFile();
        } else {
            Packet packet = getArchivePacketData(indexId, archiveId, priority);
            if (packet == null) {
                throw new IllegalStateException("Unable to send cache archive [" + indexId + ", " + archiveId + ", " + priority + "]");
            }
            return packet.getBuffer();
        }
    }

    /**
     * Gets and returns the update key file, if it doesn't exist yet {@link #ukeysFile} is not set, we generate it from
     * the cache
     */
    private static ByteBuf getUkeysFile() {
        if (ukeysFile == null) {
            ukeysFile = generateUkeysFile();
        }
        return getContainerPacketData(255, 255, ukeysFile);
    }

    /**
     * Gets the data in the archive and index
     */
    private static Packet getArchivePacketData(int indexId, int archiveId, boolean priority) {
        byte[] archive = (indexId == 255 ? Cache.STORE.getIndex255() : Cache.STORE.getIndexes()[indexId].getMainFile()).getArchiveData(archiveId);
        if (archive == null) {
            return null;
        }
        int compression = archive[0] & 0xff;
        int length = ((archive[1] & 0xff) << 24) + ((archive[2] & 0xff) << 16) + ((archive[3] & 0xff) << 8) + (archive[4] & 0xff);
        int settings = compression;
        if (!priority) {
            settings |= 0x80;
        }
        PacketBuilder bldr = new PacketBuilder();
        bldr.writeByte(indexId);
        bldr.writeShort(archiveId);
        bldr.writeByte(settings);
        bldr.writeInt(length);
        int realLength = compression != 0 ? length + 4 : length;
        for (int index = 5; index < realLength + 5; index++) {
            if (bldr.position() % 512 == 0) {
                bldr.writeByte(255);
            }
            bldr.writeByte(archive[index]);
        }
        return bldr.toPacket();
    }

    /**
     * Generates the update keys file from the cache
     */
    public static byte[] generateUkeysFile() {
        return STORE.generateIndex255Archive255Current(null, null);
    }

    /**
     * Generates the container packet data from the cache
     */
    private static ByteBuf getContainerPacketData(int indexFileId, int containerId, byte[] archive) {
        ByteBuf buffer = Unpooled.buffer(archive.length + 4);
        buffer.writeByte(indexFileId);
        buffer.writeShort(containerId);
        buffer.writeByte(0);
        buffer.writeInt(archive.length);
        for (int index = 0; index < archive.length; index++) {
            if (buffer.writerIndex() % 512 == 0) {
                buffer.writeByte(255);
            }
            buffer.writeByte(archive[index]);
        }
        return buffer;
    }

}
