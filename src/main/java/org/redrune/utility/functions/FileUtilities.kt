package org.redrune.utility.functions

import java.io.*
import java.nio.ByteBuffer
import java.util.*

/**
 * @author 'Mystic Flow
 */
object FileUtilities {

    const val BUFFER = 1024

    fun exists(name: String?): Boolean {
        val file = File(name)
        return file.exists()
    }

    @Throws(IOException::class)
    fun fileBuffer(name: String?): ByteBuffer? {
        val file = File(name)
        if (!file.exists()) {
            return null
        }
        var `in`: FileInputStream? = FileInputStream(name)
        val data = ByteArray(BUFFER)
        var read: Int
        return try {
            val buffer = ByteBuffer.allocate(`in`!!.available() + 1)
            while (`in`.read(data, 0, BUFFER).also { read = it } != -1) {
                buffer.put(data, 0, read)
            }
            buffer.flip()
            buffer
        } finally {
            `in`?.close()
            `in` = null
        }
    }

    @Throws(IOException::class)
    fun writeBufferToFile(name: String?, buffer: ByteBuffer) {
        val file = File(name)
        if (!file.exists()) {
            file.createNewFile()
        }
        val out = FileOutputStream(name)
        out.write(buffer.array(), 0, buffer.remaining())
        out.flush()
        out.close()
    }

    @Throws(IOException::class)
    fun readFile(directory: String?): LinkedList<String> {
        val fileLines = LinkedList<String>()
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader(directory))
            var string: String
            while (reader.readLine().also { string = it } != null) {
                fileLines.add(string)
            }
        } finally {
            if (reader != null) {
                reader.close()
                reader = null
            }
        }
        return fileLines
    }
}