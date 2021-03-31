package org.redrune.utility.game.entity.actor.player

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.constants.GameConstants
import org.redrune.utility.functions.Misc
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.lang.reflect.Modifier

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 2019-01-29
 */
object PlayerSaving {
    /**
     * The suffix of the file
     */
    private const val SUFFIX = ".json"

    /**
     * The location in which player files are saved
     */
    private val FILES_LOCATION = GameConstants.FILES_PATH + "saves/players/accounts/"

    /**
     * The gson instance for reading from files
     */
    private val GSON = Gson()

    /**
     * Saves the player to the json file
     *
     * @param player The player
     */
    @JvmStatic
    fun savePlayer(player: Player) {
        try {
            FileWriter(FILES_LOCATION + player.username + SUFFIX).use { writer ->
                val builder = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().excludeFieldsWithModifiers(
                    Modifier.TRANSIENT, Modifier.STATIC
                )
                val gson = builder.create()
                gson.toJson(player, writer)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Creating a player object from a saved player file
     *
     * @param name The name of the file
     */
    @JvmStatic
    fun fromFile(name: String): Player? {
        return try {
            val file = File(getFileLocation(name))
            // The file is too big; its nulled. Instead of dedicating resources we will return a null player
            // which will stop the login
            if (file.length() <= 0 || file.length() > 1000000) {
                System.err.println("Error reading file: " + file.absolutePath)
                return null
            }
            GSON.fromJson(Misc.getText(getFileLocation(name)), Player::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * The location of the file for the player
     *
     * @param name The name of the player
     */
    private fun getFileLocation(name: String): String {
        return FILES_LOCATION + name + SUFFIX
    }

    /**
     * @param name The name of the player to check for
     */
    @JvmStatic
    fun playerExists(name: String): Boolean {
        return File(getFileLocation(name)).exists()
    }
}