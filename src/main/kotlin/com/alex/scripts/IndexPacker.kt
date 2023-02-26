package com.alex.scripts

import com.alex.store.Store
import java.io.IOException

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 5/15/2016
 */
object IndexPacker {

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val toPackTo = Store("C:\\Users\\Tyler\\Desktop\\cache\\", false)
        val toPackFrom =
            Store("E:\\3.5.16 - Muth backup\\Me\\Programming\\- SERVERS -\\600+\\Alotic\\update_server\\cache\\", false)
    }
}