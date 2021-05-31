package org.redrune.utility.functions

import org.redrune.utility.functions.DateFunctions.dayName
import org.redrune.utility.functions.DateFunctions.monthName
import org.redrune.utility.functions.DateFunctions.weekNumber
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 2019-02-01
 */
object DebugFunctions {
    /**
     * The directory the logs will be stored in
     */
    private const val DIRECTORY = "data/debug/logs/"

    /**
     * How the date will be formatted in the file
     */
    private val DATE_FORMAT = SimpleDateFormat("MM.dd.yyyy hh:mm:ss.SSS")

    /**
     * Writes the log to a text file
     *
     * @param text
     * The text to write
     */
    fun writeLogText(text: String) {
        try {
            FileWriter(logFile, true).use { fw ->
                val pretext = "[" + formattedDate + "]\t" + text
                fw.write(
                    """
    $pretext
    
    """.trimIndent()
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Gets the log file in a `File` instance
     */
    private val logFile: File
        private get() {
            val file = File(directory + dayName + ".txt")
            if (!file.exists()) {
                try {
                    file.parentFile.mkdirs()
                    file.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return file
        }

    /**
     * Gets the date in a formatted string.
     *
     * @return The date
     */
    private val formattedDate: String
        private get() = DATE_FORMAT.format(Date())

    /**
     * @return the directory
     */
    private val directory: String
        private get() = DIRECTORY + monthName + "/Week_" + weekNumber + "/"
}