package org.redrune.utility.constants

import org.redrune.game.global.WorldTile
import java.util.*

/**
 * All constants for the game are stored here
 */
object GameConstants {
    /**
     * The name of the server
     */
    const val SERVER_NAME = "Dusk"

    /**
     * The path that the cache is at
     */
    const val CACHE_PATH = "./data/cache/"

    /**
     * The initial tile which players spawn at
     */
    @JvmField
    val START_TILE = WorldTile(1894, 3172, 0)

    /**
     * The home teleport tile
     */
    @JvmField
    val HOME_TILE = WorldTile(1906, 3172, 0)

    /**
     * The location that players who die spawn at
     */
    @JvmField
    val RESPAWN_TILE = WorldTile(3209, 3219, 0)

    /**
     * If we're hosted on linux
     */
    val LINUX_HOST = System.getProperty("os.name").lowercase(Locale.getDefault()).contains("linux")

    /**
     * The server is on hosted mode if the main user name contains 'Administrator'
     */
    val HOSTED =
        System.getProperty("user.home").lowercase(Locale.getDefault())
            .contains("administrator") || System.getProperty("user.home")
            .lowercase(Locale.getDefault()).contains("root") || LINUX_HOST

    /**
     * The path for files to be saved at
     */
    val FILES_PATH = if (HOSTED) if (LINUX_HOST) "/root/gamedata/" else "C:/gamedata/" else "data/"

    /**
     * The maximum amount of players online
     */
    const val PLAYERS_LIMIT = 2000

    /**
     * The maximum amount of npcs online
     */
    const val NPCS_LIMIT = Short.MAX_VALUE.toInt()

    /**
     * The maximum amount of npcs we can see
     */
    const val LOCAL_NPCS_LIMIT = 1000

    /**
     * The [Runtime.freeMemory] cap
     */
    const val MIN_FREE_MEM_ALLOWED = 30000000

    /**
     * The controller players get on creation
     */
    const val DEFAULT_CONTROLLER = ""
}