package org.redrune.game.content.entity.actor.combat.npc

import com.github.michaelbull.logging.InlineLogger
import org.redrune.game.entity.actor.Actor
import org.redrune.game.entity.actor.npc.NPC
import org.redrune.utility.functions.Misc
import java.util.*

/**
 * @author Matrix Team
 * @author Tyluur <itstyluur@icloud.com>
 */
object CombatScriptsHandler {

    /**
     * The map of cached combat scripts
     */
    private val CACHED_COMBAT_SCRIPTS = HashMap<Any, CombatScript>()

    /**
     * The default combat script
     */
    private val DEFAULT_SCRIPT: CombatScript = DefaultCombatScript()

    /**
     * Dynamically registers all npc combat scripts
     */
    fun registerAll() {
        try {
            val scripts = Misc.getClasses(CombatScriptsHandler::class.java.getPackage().name + ".scripts")
            for (script in scripts) {
                if (script !is CombatScript) {
                    logger.info { "$script was not a combat script." }
                    continue
                }
                for (key in script.keys) {
                    CACHED_COMBAT_SCRIPTS[key] = script
                }
            }
            logger.info { "Loaded " + CACHED_COMBAT_SCRIPTS.size + " cached combat scripts." }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    /**
     * Fires the combat script for an npc
     *
     * @return The delay until the next swing
     */
    @JvmStatic
    fun fireCombatScript(npc: NPC, target: Actor?): Int {
        var script = CACHED_COMBAT_SCRIPTS[npc.id]
        if (script == null) {
            script = CACHED_COMBAT_SCRIPTS[npc.definitions.name]
            if (script == null) {
                script = DEFAULT_SCRIPT
            }
        }
        return script.attack(npc, target)
    }

    private val logger = InlineLogger()
}