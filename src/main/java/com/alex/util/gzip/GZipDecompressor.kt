package com.alex.util.gzip

import com.alex.io.Stream
import java.util.zip.Inflater

object GZipDecompressor {

    private val inflaterInstance = Inflater(true)
    @JvmStatic
    fun decompress(stream: Stream, data: ByteArray?): Boolean {
        synchronized(inflaterInstance) {
            if (stream.buffer[stream.offset].toInt() != 31 || stream.buffer[stream.offset + 1].toInt() != -117) {
                return false
            }
            //throw new RuntimeException("Invalid GZIP header!");
            try {
                inflaterInstance.setInput(stream.buffer, stream.offset + 10, -stream.offset - 18 + stream.buffer.size)
                inflaterInstance.inflate(data)
            } catch (e: Exception) {
                inflaterInstance.reset()
                return false
                //throw new RuntimeException("Invalid GZIP compressed data!");
            }
            inflaterInstance.reset()
            return true
        }
    }
}