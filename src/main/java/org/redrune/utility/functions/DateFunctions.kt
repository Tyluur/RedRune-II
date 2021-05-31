package org.redrune.utility.functions

import java.text.DateFormat
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 2019-02-01
 */
object DateFunctions {
    /**
     * Gets the week number we're on
     *
     * @return The week number we're on
     */
    @JvmStatic
    val weekNumber: Int
        get() {
            val cal = Calendar.getInstance()
            cal.time = date
            return cal[Calendar.WEEK_OF_MONTH]
        }

    /**
     * Gets the date instance in toronto time
     */
    val date: Date
        get() {
            TimeZone.setDefault(TimeZone.getTimeZone("America/Toronto"))
            return Date()
        }

    /**
     * Gets the name of the date
     */
    val dayName: String
        get() = getDayName(date)

    /**
     * Gets the name of the day
     *
     * @param date
     * The date instance
     */
    private fun getDayName(date: Date): String {
        val f: DateFormat = SimpleDateFormat("EEEE")
        return try {
            f.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * Gets the name of the month we're on
     */
    @JvmStatic
    val monthName: String
        get() = DateFormatSymbols.getInstance().months[date.month]

    /**
     * Gets the name of a number
     *
     * @param number
     * The number
     */
    fun getNumberName(number: Int): String {
        val names = arrayOf("Zero", "First", "Second", "Third", "Fourth", "Fifth", "Sixth", "Seventh", "Eigth", "Ninth")
        return names[number]
    }
}