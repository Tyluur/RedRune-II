package utility.functions;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-01
 */
public class DateFunctions {
	
	/**
	 * Gets the week number we're on
	 *
	 * @return The week number we're on
	 */
	public static int getWeekNumber() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getDate());
		return cal.get(Calendar.WEEK_OF_MONTH);
	}
	
	/**
	 * Gets the date instance in toronto time
	 */
	public static Date getDate() {
		TimeZone.setDefault(TimeZone.getTimeZone("America/Toronto"));
		return new Date();
	}
	
	/**
	 * Gets the name of the date
	 */
	public static String getDayName() {
		return getDayName(getDate());
	}
	
	/**
	 * Gets the name of the day
	 *
	 * @param date
	 * 		The date instance
	 */
	private static String getDayName(Date date) {
		DateFormat f = new SimpleDateFormat("EEEE");
		try {
			return f.format(date);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * Gets the name of the month we're on
	 */
	@SuppressWarnings("deprecation")
	public static String getMonthName() {
		return DateFormatSymbols.getInstance().getMonths()[getDate().getMonth()];
	}
	
	/**
	 * Gets the name of a number
	 *
	 * @param number
	 * 		The number
	 */
	public static String getNumberName(int number) {
		String[] names = { "Zero", "First", "Second", "Third", "Fourth", "Fifth", "Sixth", "Seventh", "Eigth", "Ninth" };
		return names[number];
	}
}
