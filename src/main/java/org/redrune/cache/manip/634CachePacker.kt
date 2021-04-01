package org.redrune.cache.manip

import com.alex.store.Store
import org.redrune.cache.Cache

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since April 01, 2021
 */
fun main() {
    val FROM_CACHE_PATH = "./data/634cache/"

    val fromCache = Store(FROM_CACHE_PATH)

    val xteas = hashMapOf<Int, IntArray>()

    xteas[12342] = intArrayOf(
        461494502,
        1692498230,
        -1263351240,
        1628478856
    )
    xteas[12598] = intArrayOf(
        6023912,
        -1398996940,
        -1850857481,
        -1428087612
    )
    xteas[12599] = intArrayOf(
        -851268296,
        -1881238983,
        -865361909,
        -1679324594
    )
    xteas[12343] = intArrayOf(
        -288891831,
        -2086480984,
        810591370,
        -131418701
    )
    xteas[12086] = intArrayOf(
        -1633321492,
        1601075772,
        582544019,
        -643423676
    )
    xteas[13110] = intArrayOf(
        214591005,
        -1562708636,
        -840026806,
        -1971120246
    )

    Cache.initialize()

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
            println("Land file successfully packed.");
            if (Cache.STORE.indexes[5].encryptArchive(landArchiveId, keys))
                println("Land file successfully encrypted.");
            else
                System.err.println("Failed to encrypt land file!")
        } else {
            System.err.println("Failed to pack land file!");
        }

        println("\nOperation completed [id=$regionId].");
    }


}