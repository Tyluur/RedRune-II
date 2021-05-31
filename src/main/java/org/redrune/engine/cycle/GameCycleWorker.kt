package org.redrune.engine.cycle

import com.github.michaelbull.logging.InlineLogger
import java.util.concurrent.Executors

class GameCycleWorker {

    /**
     * The instance of the update sequence
     */
    private val updateSequence = UpdateSequence()

    /**
     * The executor.
     */
    private val executor = Executors.newSingleThreadExecutor()

    /**
     * If the major update worker has started.
     */
    private var started = false

    /**
     * Starts the worker
     */
    fun start() {
        if (started) {
            return
        }
        started = true
        executor.execute(updateSequence)
        logger.info { "Main game cycle worker started." }
    }

    companion object {

        private val logger = InlineLogger()

        /**
         * The timestamp of the end of the last cycle
         */
        var lastCycleTime: Long = 0

        /**
         * The amount of ticks that have passed since the game started
         */
        @JvmStatic
        var ticksPassed = 0
    }
}