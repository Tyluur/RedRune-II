package org.redrune.utility.functions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-01
 */
public class DebugFunctions {

    /**
     * The directory the logs will be stored in
     */
    private static final String DIRECTORY = "data/debug/logs/";

    /**
     * How the date will be formatted in the file
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM.dd.yyyy hh:mm:ss.SSS");

    /**
     * Writes the log to a text file
     *
     * @param text The text to write
     */
    public static void writeLogText(String text) {
        try (FileWriter fw = new FileWriter(getLogFile(), true)) {
            String pretext = "[" + getFormattedDate() + "]\t" + text;
            fw.write(pretext + "\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the log file in a {@code File} instance
     */
    private static File getLogFile() {
        File file = new File(getDirectory() + DateFunctions.getDayName() + ".txt");
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * Gets the date in a formatted string.
     *
     * @return The date
     */
    private static String getFormattedDate() {
        return DATE_FORMAT.format(new Date());
    }

    /**
     * @return the directory
     */
    private static String getDirectory() {
        return DIRECTORY + DateFunctions.getMonthName() + "/Week_" + DateFunctions.getWeekNumber() + "/";
    }

}
