package org.redrune.global.wordlist

import com.github.michaelbull.logging.InlineLogger
import java.util.*

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 8/30/2017
 */
object WorldList {
    /**
     * The map of world entries
     */
    val worlds = HashMap<Int, WorldEntry>()

    /**
     * Registers all worlds
     */
    fun initialize() {
        worlds[1] = WorldEntry(
            "Game Server",
            "127.0.0.1",
            38,
            WorldListConstants.FLAG_MEMBERS or WorldListConstants.FLAG_LOOTSHARE or WorldListConstants.FLAG_HIGHLIGHT,
            "Canada",
            true
        )
        worlds[2] = WorldEntry(
            "PvP World",
            "127.0.0.1",
            38,
            WorldListConstants.FLAG_MEMBERS or WorldListConstants.FLAG_LOOTSHARE or WorldListConstants.FLAG_HIGH_RISK,
            "Canada",
            true
        )
        logger.info { "Loaded " + worlds.size + " worlds" }
    }

    private val logger = InlineLogger()

}
