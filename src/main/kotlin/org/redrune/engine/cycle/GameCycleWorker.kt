package org.redrune.engine.cycle

import com.github.michaelbull.logging.InlineLogger
import org.redrune.engine.SystemManager
import org.redrune.game.global.World
import org.redrune.utility.functions.Misc
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class GameCycleWorker : Runnable {

    /**
     * The instance of the update sequence
     */
    private val updateSequence = UpdateSequence()

    /**
     * The executor.
     */
    private val executor: Executor = Executors.newSingleThreadExecutor()

    /**
     * If the major update worker has started.
     */
    private var started = false

    override fun run() {
        while (!SystemManager.shutdown) {
            val currentTime = Misc.currentTimeMillis()
            try {
                updateSequence.fire(World.getLobbyPlayers(), World.getPlayers(), World.getNPCs())
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            sleepThread(currentTime)
        }
    }

    /**
     * Handles the sleeping of the thread
     */
    private fun sleepThread(startTime: Long) {
        lastCycleTime = Misc.currentTimeMillis()
        val sleepTime = 600 + (startTime - lastCycleTime)
        if (sleepTime <= 0) {
            return
        }
        ticksPassed++
        try {
            Thread.sleep(sleepTime)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    /**
     * Starts the worker
     */
    fun start() {
        if (started) {
            return
        }
        started = true
        executor.execute(this@GameCycleWorker)
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
            private set
    }
}