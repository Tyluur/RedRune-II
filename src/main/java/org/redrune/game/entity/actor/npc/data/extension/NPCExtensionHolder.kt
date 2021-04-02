package org.redrune.game.entity.actor.npc.data.extension

import com.github.michaelbull.logging.InlineLogger
import org.redrune.game.entity.actor.npc.data.extension.impl.TalkingNPCExtension
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/13/2017
 */
object NPCExtensionHolder {
    /**
     * The map of extensions
     */
    private val EXTENSIONS: MutableMap<Int, NPCExtension> = HashMap()

    /**
     * Registers all extensions
     */
    fun initialize() {
        store(13281, TalkingNPCExtension("Exchange your points into rewards here!", TimeUnit.SECONDS.toMillis(3)))
        store(2290, TalkingNPCExtension("Manage your account here!", TimeUnit.SECONDS.toMillis(3)))
        logger.info { "Initialized " + EXTENSIONS.size + " npc extensions" }
    }

    /**
     * Stores an extension
     */
    private fun store(npcId: Int, extension: NPCExtension) {
        EXTENSIONS[npcId] = extension
    }

    /**
     * Gets an extension by the id of the npc
     */
    @JvmStatic
    fun getExtension(npcId: Int): Optional<NPCExtension> {
        return Optional.ofNullable(EXTENSIONS[npcId])
    }

    private val logger = InlineLogger()
}