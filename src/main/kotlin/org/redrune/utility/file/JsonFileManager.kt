package org.redrune.utility.file

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.redrune.utility.functions.Misc
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.lang.reflect.Modifier

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/30/2017
 */
object JsonFileManager {

    /**
     * The gson instance
     */
    private val GSON = GsonBuilder().setPrettyPrinting().create()

    /**
     * Loads the file data
     *
     * @param file The file to load data from
     */
    fun <K> loadJsonData(file: File): K? {
        return if (!file.exists()) {
            null
        } else GSON.fromJson(
            Misc.getText(file.absolutePath),
            object : TypeToken<K>() {}.type
        )
    }

    /**
     * Saves the data to the file
     *
     * @param data     The list to save
     * @param location The location to save to
     */
    fun <T> save(data: T, location: String?): Boolean {
        try {
            FileWriter(location).use { writer ->
                val builder = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().excludeFieldsWithModifiers(
                    Modifier.TRANSIENT, Modifier.STATIC
                )
                val gson = builder.create()
                gson.toJson(data, writer)
                return true
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
    }
}