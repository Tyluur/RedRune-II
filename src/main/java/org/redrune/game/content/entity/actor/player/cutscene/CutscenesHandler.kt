package org.redrune.game.content.entity.actor.player.cutscene

import com.github.michaelbull.logging.InlineLogger
import org.redrune.game.content.entity.actor.player.cutscene.impl.*

object CutscenesHandler {
    private val HANDLED_CUTSCENES = HashMap<Any, Class<Cutscene>>()
    fun reload() {
        HANDLED_CUTSCENES.clear()
        init()
    }

    fun init() {
        try {
            HANDLED_CUTSCENES["EdgeWilderness"] =
                Class.forName(EdgeWilderness::class.java.canonicalName) as Class<Cutscene>
            HANDLED_CUTSCENES["DTPreview"] = Class.forName(DTPreview::class.java.canonicalName) as Class<Cutscene>
            HANDLED_CUTSCENES["NexCutScene"] = Class.forName(NexCutScene::class.java.canonicalName) as Class<Cutscene>
            HANDLED_CUTSCENES["TowersPkCutscene"] =
                Class.forName(TowersPkCutscene::class.java.canonicalName) as Class<Cutscene>
            HANDLED_CUTSCENES["HomeCutScene"] = Class.forName(HomeCutScene::class.java.canonicalName) as Class<Cutscene>
            HANDLED_CUTSCENES["NewStartTutorial"] =
                Class.forName(NewStartTutorial::class.java.canonicalName) as Class<Cutscene>
            logger.info { ("Loaded " + HANDLED_CUTSCENES.size + " game cutscenes") }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun getCutscene(key: Any): Cutscene? {
        val classC = HANDLED_CUTSCENES[key] ?: return null
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