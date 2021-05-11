package utility.game.entity.actor.player

import com.github.michaelbull.logging.InlineLogger
import utility.functions.Misc
import java.io.*
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.util.*
import kotlin.experimental.and

object Censor {
    /**
     * The base location for all censor related data
     */
    private const val BASE_LOCATION = "./data/repository/chat/"

    /**
     * The location for packed data
     */
    private const val PACKED_PATH = BASE_LOCATION + "packedCensoredWords.e"

    /**
     * The location for unpacked data
     */
    private const val UNPACKED_PATH = BASE_LOCATION + "unpackedCensoredWords.txt"

    /**
     * The list of censored words
     */
    private val CENSORED_WORDS: MutableList<String> = ArrayList()
    fun initialize() {
        if (File(PACKED_PATH).exists()) {
            loadPackedCensoredWords()
        } else {
            loadUnpackedCensoredWords()
        }
        logger.info { ("Loaded " + CENSORED_WORDS.size + " censored words") }
    }

    private fun loadPackedCensoredWords() {
        try {
            val `in` = RandomAccessFile(PACKED_PATH, "r")
            val channel = `in`.channel
            val buffer: ByteBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size())
            while (buffer.hasRemaining()) {
                CENSORED_WORDS.add(readString(buffer))
            }
            channel.close()
            `in`.close()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private fun loadUnpackedCensoredWords() {
        println("Packing censored words...")
        try {
            val `in` = BufferedReader(FileReader(UNPACKED_PATH))
            val out = DataOutputStream(FileOutputStream(PACKED_PATH))
            while (true) {
                val line = `in`.readLine() ?: break
                if (line.startsWith("//") || line.startsWith("*")) {
                    continue
                }
                writeString(out, line)
                CENSORED_WORDS.add(line)
            }
            `in`.close()
            out.flush()
            out.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun readString(buffer: ByteBuffer): String {
        val count: Int = (buffer.get() and 0xff.toByte()).toInt()
        val bytes = ByteArray(count)
        buffer[bytes, 0, count]
        return String(bytes)
    }

    @Throws(IOException::class)
    fun writeString(out: DataOutputStream, string: String) {
        val bytes = string.toByteArray()
        out.writeByte(bytes.size)
        out.write(bytes)
    }

    @JvmStatic
    fun getFilteredMessage(message: String): String {
        var message = message
        message = message.toLowerCase()
        for (word in CENSORED_WORDS) {
            if (message.contains(word)) {
                val sb = StringBuilder()
                for (i in word.indices) {
                    sb.append("*")
                }
                message = message.replace(word, sb.toString())
            }
        }
        return Misc.fixChatMessage(message)
    }

    private val logger = InlineLogger()
}