package org.redrune.utility.game.map

import com.github.michaelbull.logging.InlineLogger
import org.redrune.game.global.WorldTile
import org.redrune.utility.functions.Misc
import java.io.*
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.util.*
import kotlin.experimental.and

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 8/30/2017
 */
object MapArchiveKeys {
    /**
     * The path to packed xteas
     */
    private const val PACKED_FILE_PATH = "data/repository/map/packedKeys.bin"

    /**
     * The path to unpacked exteas
     */
    private const val UNPACKED_FILE_PATH = "data/repository/map/containersXteas/workingkeys/"

    /**
     * MapKeys.
     */
    private val mapKeys: MutableMap<Int, IntArray> = HashMap()
    fun isAtArea(areaName: String?, tile: WorldTile): Boolean {
        return isAtArea(Misc.getNameHash(areaName), tile)
    }

    @JvmStatic
    fun isAtArea(areaNameHash: Int, tile: WorldTile): Boolean {
        val coordsList = mapKeys[areaNameHash] ?: return false
        var index = 0
        while (index < coordsList.size) {
            if (tile.plane == coordsList[index] && tile.x >= coordsList[index + 1] && tile.x <= coordsList[index + 2] && tile.y >= coordsList[index + 3] && tile.y <= coordsList[index + 4]) return true
            index += 5
        }
        return false
    }

    /**
     * Initiating void.
     */
    fun initialize() {
        try {
            if (!loadPackedFile()) {
                loadUnpacked()
            }
            logger.info { ("Loaded " + mapKeys.size + " map XTEA key(s)") }
        } catch (e: Throwable) {
            System.err.println("Failed to load map xtea(s)!")
            e.printStackTrace()
        }
    }

    /**
     * Loads xteas from the packed file
     *
     * @return True if we could load
     * @throws IOException
     * In the case of an exception in parsing
     */
    @Throws(IOException::class)
    private fun loadPackedFile(): Boolean {
        val file = File(PACKED_FILE_PATH)
        if (!file.exists()) {
            return false
        }
        val raf = RandomAccessFile(file, "rw")
        val buffer: ByteBuffer = raf.channel.map(FileChannel.MapMode.READ_ONLY, 0, raf.length())
        while (buffer.remaining() > 0) {
            val id: Int = (buffer.short and 0xFFFF.toShort()).toInt()
            val key = IntArray(4)
            for (i2 in 0..3) {
                key[i2] = buffer.int
            }
            mapKeys[id] = key
        }
        raf.close()
        return true
    }

    /**
     * Loads xteas from the unpacked file location and packs them into the file [.PACKED_FILE_PATH]
     */
    @Throws(IOException::class)
    private fun loadUnpacked() {
        val directory = File(UNPACKED_FILE_PATH)
        val output = DataOutputStream(FileOutputStream(PACKED_FILE_PATH))
        if (directory.isDirectory) {
            val files = directory.listFiles() ?: return
            for (file in files) {
                if (file.isFile) {
                    val input = BufferedReader(FileReader(file))
                    val id = file.name.substring(0, file.name.indexOf(".")).toInt()
                    val keys = IntArray(4)
                    output.writeShort(id)
                    for (i in 0..3) {
                        val line = input.readLine()
                        try {
                            if (line != null) {
                                keys[i] = line.toInt()
                            } else {
                                logger.info { "Corrupted XTEA file : $id" }
                                keys[i] = 0
                            }
                        } catch (e: NumberFormatException) {
                            logger.info { "Corrupted XTEA file : $id; line: $line" }
                            keys[i] = 0
                        }
                        output.writeInt(keys[i])
                    }
                    input.close()
                    mapKeys[id] = keys
                }
            }
        }
        output.close()
    }

    /**
     * Gets the keys of a regionId
     *
     * @param regionId
     * The region id
     */
    @JvmStatic
    fun getKey(regionId: Int): IntArray? {
        return mapKeys[regionId]
    }

    private val logger = InlineLogger()
}