package org.redrune.cache.manip

import com.alex.store.Store
import org.redrune.cache.Cache
import org.redrune.utility.game.map.MapArchiveKeys

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since April 01, 2021
 */
fun main() {
    packOSRSData()
}

fun packOSRSData() {
    Cache.initialize()

    val src = "./data/osrs/nalore/"

    val srcCache = Store(src)

    val dstCache = Cache.STORE

    with(Indices) {

        val indices = listOf(MAPS, MODELS, ITEMS)

        for (index in indices) {
            dstCache.indexes[index].packIndex(srcCache)

            println("Finished packing index $index")
        }
    }

}

private fun pack634Cache(srcCache: Store) {
    val dstCache = Cache.STORE

    with(Indices) {


        val indices = listOf(MAPS, MODELS, OBJECTS)

        for (index in indices) {
            dstCache.indexes[index].packIndex(srcCache)

            println("Finished packing index $index")
        }
    }

}

private fun pack634ObjectDefinitions(srcCache: Store) {
    val dstCache = Cache.STORE

    val index = Indices.OBJECTS


    val ids = listOf(26827)

    for (id in ids) {
        val archiveId = id ushr 8
        val fileId = id and 0xff

        val data = srcCache.indexes[index].getFile(archiveId, fileId)

        dstCache.indexes[index].putFile(archiveId, fileId, data)
    }

    println("Completed")
}

private fun pack634Maps(
    xteas: HashMap<Int, IntArray>,
    fromCache: Store
) {
    MapArchiveKeys.mapKeys.entries.forEach { entry ->
        xteas[entry.key] = entry.value
    }

    for (xtea in xteas) {
        val regionId = xtea.key
        val keys = xtea.value

        val regionX = (regionId shr 8) * 64
        val regionY = (regionId and 0xff) * 64

        val mapArchiveId =
            fromCache.indexes[5].getArchiveId("m" + ((regionX shr 3) / 8) + "_" + ((regionY shr 3) / 8))

        val landArchiveId =
            fromCache.indexes[5].getArchiveId("l" + ((regionX shr 3) / 8) + "_" + ((regionY shr 3) / 8))

        val landHashCode = (("l" + (regionX shr 3) / 8) + "_" + ((regionY shr 3) / 8)).hashCode()

        if (Cache.STORE.indexes[5].putFile(
                mapArchiveId,
                0,
                fromCache.indexes[5].getFile(mapArchiveId, 0)
            )
        ) {
            println("Map file successfully packed.")
        } else {
            System.err.println("Failed to pack map file!")
        }

        //pack land file
        if (Cache.STORE.indexes[5].putFile(
                landArchiveId, 0, 2, fromCache.indexes[5].getFile(landArchiveId, 0, keys),
                keys,
                true,
                true,
                landHashCode,
                -1
            )
        ) {
            println("Land file successfully packed.")
            if (Cache.STORE.indexes[5].encryptArchive(landArchiveId, keys))
                println("Land file successfully encrypted.")
            else
                System.err.println("Failed to encrypt land file!")
        } else {
            System.err.println("Failed to pack land file!")
        }

        println("\nOperation completed [id=$regionId].")
    }
}