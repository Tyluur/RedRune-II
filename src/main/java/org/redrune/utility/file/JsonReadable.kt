package org.redrune.utility.file

import com.google.gson.GsonBuilder
import java.io.File
import java.io.FileWriter
import java.io.IOException

/**
 * Classes that are readable by gson will implement this class, this is due to the non-generic loading of gson objects.
 *
 * @author Tyluur <itstyluur@icloud.com>
 * @since 6/16/2017
 */
interface JsonReadable<K> {

    /**
     * Handles the loading from a file
     *
     * @param file The file
     */
    fun load(file: File?): K

    /**
     * Saves the object
     *
     * @param file The file to save to
     * @param k    The object to save
     */
    fun save(file: File, k: K) {
        try {
            FileWriter(file.absolutePath).use { writer ->
                val builder = GsonBuilder().setPrettyPrinting().disableHtmlEscaping()
                val gson = builder.create()
                gson.toJson(k, writer)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}