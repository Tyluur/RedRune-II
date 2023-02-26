package com.alex.utils

import com.alex.io.OutputStream
import com.alex.store.Store
import java.math.BigInteger
import java.util.*

object Utils {
    @JvmStatic
    fun cryptRSA(data: ByteArray?, exponent: BigInteger?, modulus: BigInteger?): ByteArray {
        return BigInteger(data).modPow(exponent, modulus).toByteArray()
    }

    fun getArchivePacketData(indexId: Int, archiveId: Int, archive: ByteArray): ByteArray {
        val stream = OutputStream(archive.size + 4)
        stream.writeByte(indexId)
        stream.writeShort(archiveId)
        stream.writeByte(0) // priority, no compression
        stream.writeInt(archive.size)
        var offset = 8
        for (index in archive.indices) {
            if (offset == 512) {
                stream.writeByte(-1)
                offset = 1
            }
            stream.writeByte(archive[index].toInt())
            offset++
        }
        val packet = ByteArray(stream.offset)
        stream.offset = 0
        stream.getBytes(packet, 0, packet.size)
        return packet
    }

    @JvmStatic
    fun getNameHash(name: String): Int {
        return name.lowercase(Locale.getDefault()).hashCode()
    }

    fun getInterfaceDefinitionsSize(store: Store): Int {
        return store.indexes[3].lastArchiveId
    }

    fun getInterfaceDefinitionsComponentsSize(store: Store, interfaceId: Int): Int {
        return store.indexes[3].getLastFileId(interfaceId)
    }

    fun getItemDefinitionsSize(store: Store): Int {
        val lastArchiveId = store.indexes[19].lastArchiveId
        return lastArchiveId * 256 + store.indexes[19].getValidFilesCount(lastArchiveId)
    }
}