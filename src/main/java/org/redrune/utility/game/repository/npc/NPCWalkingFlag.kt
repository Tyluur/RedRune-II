package org.redrune.utility.game.repository.npc

import com.github.michaelbull.logging.InlineLogger
import org.redrune.game.entity.actor.npc.NPC
import java.util.*

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/1/2017
 */
object NPCWalkingFlag {
    /**
     * The map of walk flags
     */
    private val WALK_FLAGS: MutableMap<Int, Boolean?> = HashMap()

    /**
     * Registers all the flags
     */
    fun registerFlags() {
        WALK_FLAGS[1597] = false
        WALK_FLAGS[3385] = false
        WALK_FLAGS[4361] = false
        WALK_FLAGS[11248] = true
        WALK_FLAGS[2024] = false
        WALK_FLAGS[1918] = false
        WALK_FLAGS[1334] = false
        WALK_FLAGS[1411] = false
        WALK_FLAGS[33] = false
        WALK_FLAGS[1288] = false
        WALK_FLAGS[945] = false
        WALK_FLAGS[2998] = false
        WALK_FLAGS[1699] = false
        WALK_FLAGS[1658] = false
        WALK_FLAGS[550] = false
        WALK_FLAGS[599] = false
        WALK_FLAGS[549] = false
        WALK_FLAGS[1866] = false
        WALK_FLAGS[554] = false
        WALK_FLAGS[5112] = false
        WALK_FLAGS[278] = false
        WALK_FLAGS[519] = false
        WALK_FLAGS[6370] = false
        WALK_FLAGS[211] = false
        WALK_FLAGS[3001] = false
        WALK_FLAGS[4516] = false
        WALK_FLAGS[2258] = false
        WALK_FLAGS[2892] = false
        WALK_FLAGS[4293] = false
        WALK_FLAGS[2894] = false
        WALK_FLAGS[2896] = false
        WALK_FLAGS[2328] = false
        WALK_FLAGS[4288] = false
        WALK_FLAGS[682] = false
        WALK_FLAGS[1303] = false
        WALK_FLAGS[608] = false
        WALK_FLAGS[588] = false
        WALK_FLAGS[1778] = false
        WALK_FLAGS[6970] = false
        WALK_FLAGS[599] = false
        WALK_FLAGS[6539] = false
        WALK_FLAGS[6537] = false
        WALK_FLAGS[4653] = false
        WALK_FLAGS[14332] = true
        WALK_FLAGS[961] = false
        WALK_FLAGS[4247] = false
        WALK_FLAGS[4375] = false
        WALK_FLAGS[2824] = false
        WALK_FLAGS[946] = false
        WALK_FLAGS[947] = false
        WALK_FLAGS[949] = false
        logger.info { "Registered " + WALK_FLAGS.size + " custom walk flags." }
    }

    /**
     * Checks if the map flags contains a key
     */
    @JvmStatic
    fun containsKey(npcId: Int): Boolean {
        return WALK_FLAGS.containsKey(npcId)
    }

    /**
     * Gets the flag for the npc walking data
     *
     * @param npcId
     * The npc
     */
    @JvmStatic
    fun getWalkingFlag(npcId: Int): Int {
        return if (!WALK_FLAGS.containsKey(npcId)) {
            NPC.NO_WALK
        } else {
            val walking = WALK_FLAGS[npcId]
            if (walking!!) {
                NPC.NORMAL_WALK
            } else {
                NPC.NO_WALK
            }
        }
    }

    private val logger = InlineLogger()
}