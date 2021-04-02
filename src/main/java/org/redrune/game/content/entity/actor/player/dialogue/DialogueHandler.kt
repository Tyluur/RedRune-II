package org.redrune.game.content.entity.actor.player.dialogue

import com.github.michaelbull.logging.InlineLogger
import org.redrune.utility.functions.Misc
import java.util.*

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @author Matrix Team
 * @since 2/9/19
 */
object DialogueHandler {
    /**
     * The map of cached dialogues
     */
    private val DIALOGUES = HashMap<Any, Dialogue>()

    /**
     * Reloads all dialogues
     */
    fun reload() {
        DIALOGUES.clear()
        initialize()
    }

    /**
     * Initializes all game dialogues
     */
    fun initialize() {
        Misc.getClasses(DialogueHandler::class.java.getPackage().name + ".impl").stream()
            .filter { obj: Any? -> Dialogue::class.java.isInstance(obj) }
            .forEach { clazz: Any ->
                try {
                    DIALOGUES[clazz.javaClass.simpleName] = clazz as Dialogue
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        logger.info { "Loaded " + DIALOGUES.size + " game dialogues" }
    }

    /**
     * Gets a dialogue by the key
     *
     * @param key
     * The dialogue
     */
    @JvmStatic
    fun getDialogue(key: Any): Dialogue? {
        if (key is Dialogue) {
            return key
        }
        val dialogue = DIALOGUES[key]
        if (dialogue == null) {
            logger.error { "Unable to find a dialogue for key'$key'" }
            return null
        }
        return try {
            Class.forName(dialogue.javaClass.name).newInstance() as Dialogue
        } catch (e: InstantiationException) {
            e.printStackTrace()
            null
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            null
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    private val logger = InlineLogger()
}