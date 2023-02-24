package org.redrune.utility.game.repository.`object`.door

import com.github.michaelbull.logging.InlineLogger
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.redrune.utility.file.JsonFileManager
import org.redrune.utility.functions.Misc
import java.io.File
import java.sql.SQLException

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-20
 */
object DoorRepository {

    /**
     * The doors mapping.
     */
    private val DOORS: MutableMap<Int, Door> = HashMap()

    /**
     * The location of the door configuration file
     */
    private const val CONFIGURATION_FILE = "./data/repository/object/doors.json"

    /**
     * The gson instance
     */
    private val GSON = GsonBuilder().setPrettyPrinting().create()

    @JvmStatic
    fun main(args: Array<String>) {
        val doors = doorsFromFile
            ?: throw IllegalStateException("Unable to parse doors from file {" + CONFIGURATION_FILE + "}, recheck running directory!")
        saveDoors(doors)
    }

    /**
     * Initializes all of the doors from the file
     */
    fun initialize() {
        val doors = doorsFromFile
            ?: throw IllegalStateException("Unable to parse doors from file {" + CONFIGURATION_FILE + "}, recheck running directory!")
        for (door in doors) {
            DOORS[door.id] = door
            val replaced = Door(door.replaceId)
            replaced.replaceId = door.id
            DOORS[replaced.id] = replaced
        }
        logger.info { ("Loaded " + DOORS.size + " doors") }
    }

    /**
     * Gets the ids of all the doors that have been registered
     */
    val doorIds: Set<Int>
        get() = DOORS.keys

    /**
     * Finds a door by the id
     *
     * @param objectId
     * The id of the door to find
     */
    @JvmStatic
    fun forId(objectId: Int): Door? {
        return DOORS[objectId]
    }

    private fun dumpDoorsFromArios() {
        val config = HikariConfig()
        var ds: HikariDataSource
        run {
            config.jdbcUrl = "jdbc:mysql://localhost/arios"
            config.username = "debug"
            config.password = "debug"
            config.driverClassName =
                "com.mysql.cj.jdbc.Driver" //alternative is Class.forName("com.mysql.cj.jdbc.Driver")
            config.addDataSourceProperty("cachePrepStmts", "true")
            config.addDataSourceProperty("prepStmtCacheSize", "250")
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
            ds = HikariDataSource(config)
        }
        val query = "SELECT * from `door_configs`"
        val doors: MutableList<Door> = ArrayList()
        try {
            ds.connection.use { con ->
                con.prepareStatement(query).use { pst ->
                    pst.executeQuery().use { rs ->
                        while (rs.next()) {
                            val doorId: Int = rs.getInt("id")
                            val replaceId: Int = rs.getInt("replaceId")
                            val fence: Int = rs.getInt("fence")
                            val door = Door(doorId)
                            door.replaceId = replaceId
                            doors.add(door)
                        }
                    }
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        saveDoors(doors)
    }

    /**
     * Loads all the data for doors from the file
     */
    private val doorsFromFile: List<Door>?
        private get() {
            val file = File(CONFIGURATION_FILE)
            if (!file.exists()) {
                return null
            }
            val text: String = Misc.getText(CONFIGURATION_FILE)
            return GSON.fromJson(text, object : TypeToken<List<Door?>?>() {}.type)
        }

    /**
     * Saves the list of doors to file
     *
     * @param doors
     * The list of doors to save
     */
    private fun saveDoors(doors: List<Door>) {
        JsonFileManager.save<List<Door>>(doors, CONFIGURATION_FILE)
    }

    private val logger = InlineLogger()
}