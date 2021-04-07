package org.redrune.cache.manip

import com.alex.store.Store
import org.redrune.cache.Cache
import org.redrune.utility.game.map.MapArchiveKeys

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since April 01, 2021
 */
fun main() {

    val src = "./data/634cache/"

    val srcCache = Store(src)

    val localXteas = hashMapOf<Int, IntArray>()

    Cache.initialize()
    MapArchiveKeys.initialize()

    pack634ObjectDefinitions(srcCache)

//    pack634Maps(localXteas, fromCache)

}

private fun pack634ObjectDefinitions(fromCache: Store) {
    val cache = Cache.STORE

    val index = Indices.OBJECTS
    cache.indexes[index].packIndex(fromCache)

    println("Completed packing [index=$index] ")
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