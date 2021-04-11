package org.redrune.engine.tick.schedule

import com.google.common.base.Preconditions

/**
 * A game-related task that is scheduled to run in the future.
 *
 * @author Graham
 * @author Tyluur <itstyluur@icloud.com>
 * @since 5/26/2017
 */
abstract class ScheduledTask @JvmOverloads constructor(delay: Int = 1, goalTicks: Int = 1) {

    /**
     * The maximum amount of ticks that can be ran on this task
     */
    val goalTicks: Int

    /**
     * The delay between executions of the task, in ticks.
     */
    private val delay: Int

    /**
     * The number of ticks remaining until the task is next executed.
     */
    var ticks: Int
        private set

    /**
     * The amount of times this task has been pulsed
     */
    var ticksPassed = 0
        private set

    /**
     * The delayed tick count, this is incremented each time the task is pulsed. The task is pulsed based on the delay
     * set in the constructor.
     */
    var delayedTickCount = 0
        private set
    /**
     * Checks if this task is running.
     *
     * @return `true` if so, `false` if not.
     */
    /**
     * A flag indicating if the task is running.
     */
    var isRunning = true
        private set

    override fun toString(): String {
        return "ScheduledTask{" + "goalTicks=" + goalTicks + ", delay=" + delay + ", ticks=" + ticks + ", ticksPassed=" + ticksPassed + ", running=" + isRunning + '}'
    }

    /**
     * Stops the task.
     */
    protected fun stop() {
        isRunning = false
    }

    /**
     * Pulses this task: updates the delay and calls [Runnable.run] )} if necessary.
     */
    fun pulse() {
        // task wasnt forced to stop
        if (!isRunning) {
            return
        }
        // reduce delay
        ticks--

        // ticks passed increments
        ticksPassed++

        // time until the next one has lapsed
        if (ticks <= 0) {
            // increases the delayed tick count
            delayedTickCount++
            // reset in the case of infinite looping pulse
            ticks = delay
            // runs the task
            run()
        }
    }

    /**
     * Runs the task
     */
    abstract fun run()
    /**
     * Creates a new scheduled task.
     *
     * @param delay
     * The delay between executions of the task, in ticks.
     * @param goalTicks
     * The maximum amount of ticks that this task can go through
     * @throws IllegalArgumentException
     * If the delay is less than or equal to zero.
     */
    /**
     * Creates a new scheduled task.
     *
     * @param delay
     * The delay between executions of the task, in ticks.
     * @throws IllegalArgumentException
     * If the delay is less than or equal to zero.
     */
    /**
     * Constructs a scheduled task with a tick delay
     */
    init {
        Preconditions.checkArgument(delay >= 0, "Delay cannot be negative")
        this.delay = delay
        ticks = delay
        this.goalTicks = goalTicks
    }
}