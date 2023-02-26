package com.alex.scripts

import com.alex.store.Store
import java.io.IOException

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 1/4/2017
 */
object MapReplacer {

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        // this is where the map should be placed to
        val replaced = Store("./data/essentials/cache/") // 667 cache
        val origonal = Store("C:\\Users\\Tyler\\Downloads\\Dementhium 637\\Dementhium 637\\data\\cache\\") // 639 cache

        /*int count = 1, regionId = 12598;
		
		int regionX = (regionId >> 8) * 64;
		int regionY = (regionId & 0xff) * 64;
		String replacedName = "l" + ((regionX >> 3) / 8) + "_" + ((regionY >> 3) / 8);
		int replacedArchive = replaced.getIndexes()[5].getArchiveId(replacedName);
		if (replacedArchive == -1) {
			System.out.println("Bad replaced archive:" + regionId);
			return;
		}
		
		String orgName = "l" + ((regionX >> 3)) + "_" + ((regionY >> 3));
		
		int orgArchive = origonal.getIndexes()[5].getArchiveId(replacedName);
		if (orgArchive == -1) {
			System.out.println("Bad original archive: " + regionId);
			return;
		}
		// 637 keys
		byte[] data = origonal.getIndexes()[5].getFile(replacedArchive, 0, new int[] { 6023912, -1398996940, -1850857481, -1428087612 });
		// 667 keys
		replaced.getIndexes()[5].putFile(orgArchive, 0, Constants.GZIP_COMPRESSION, data, new int[] { 1251254967, -652948165, 1911744550, -595699947 }, false, false, Utils.getNameHash(replacedName), -1);
		replaced.getIndexes()[5].rewriteTable(); // Now we are done, test it out.*/
        replaced.indexes[2].putArchive(2, origonal)
        replaced.indexes[2].putArchive(4, origonal)
        replaced.indexes[5].packIndex(5, origonal, false)
        replaced.indexes[7].packIndex(origonal)
        replaced.indexes[18].packIndex(origonal)
        replaced.indexes[18].putArchive(18, origonal)
        replaced.indexes[7].putArchive(7, origonal)

        //replaced.getIndexes()[2].packIndex(2, origonal, false);
        println("Completed successfully.")
    }
}