package com.alex.util.gzip

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.zip.GZIPOutputStream

object GZipCompressor {

    @JvmStatic
    fun compress(data: ByteArray?): ByteArray? {
        val compressedBytes = ByteArrayOutputStream()
        try {
            val out = GZIPOutputStream(compressedBytes)
            out.write(data)
            out.finish()
            out.close()
            return compressedBytes.toByteArray()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}