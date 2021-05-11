package game.content.entity.actor.player.controller

import com.github.michaelbull.logging.InlineLogger
import game.content.entity.actor.player.controller.impl.CorpBeastController
import game.content.entity.actor.player.controller.impl.activity.Wilderness
import game.content.entity.actor.player.controller.impl.activity.pvp.PvPWorld
import java.util.*

object ControllerHandler {
    /**
     * The map of cached controllers
     */
    private val CACHED_CONTROLLERS = HashMap<Any, Class<Controller>>()

    /**
     * Reloads all controllers
     */
    fun reload() {
        CACHED_CONTROLLERS.clear()
        registerAll()
    }

    /**
     * Registers all controllers to the map
     */
    fun registerAll() {
        try {
            register("Wilderness", Wilderness::class.java)
            register("CorpBeastController", CorpBeastController::class.java)
            register("PvPWorld", PvPWorld::class.java)
            logger.info { "Successfully registered ${CACHED_CONTROLLERS.size} controllers." }
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

    @Throws(ClassNotFoundException::class)
    private fun register(name: String, clazz: Class<*>) {
        CACHED_CONTROLLERS[name] =
            Class.forName(clazz.canonicalName) as Class<Controller>
    }

    /**
     * Finds a controller by a key
     *
     * @param key The key
     */
    fun getController(key: Any?): Controller? {
        if (key is Controller) {
            return key
        }
        val classC = CACHED_CONTROLLERS[key] ?: return null
        try {
            return classC.newInstance()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return null
    }

    private val logger = InlineLogger()
}