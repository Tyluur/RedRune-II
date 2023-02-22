package org.redrune.utility.constants

import org.redrune.game.global.WorldTile

/**
 * All constants for the game are stored here
 */
object GameConstants {
    /**
     * The name of the server
     */
    const val SERVER_NAME = "RedRune"

    /**
     * The path that the cache is at
     */
    const val CACHE_PATH = "./data/cache/"

    /**
     * The location that players spawn at
     */
    @JvmField
    val START_TILE = WorldTile(1890, 3164, 0)

    /**
     * The location that players spawn at
     */
    @JvmField
    val HOME_TILE = WorldTile(1893, 3179, 0)

    /**
     * The location that players who die spawn at
     */
    @JvmField
    val RESPAWN_TILE = WorldTile(1906, 3172, 0)

    /**
     * If we're hosted on linux
     */
    val LINUX_HOST = System.getProperty("os.name").toLowerCase().contains("linux")

    /**
     * The server is on hosted mode if the main user name contains 'Administrator'
     */
    val HOSTED =
        System.getProperty("user.home").toLowerCase().contains("administrator") || System.getProperty("user.home")
            .toLowerCase().contains("root") || LINUX_HOST

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