package engine.tick.schedule

import utility.functions.Misc
import java.util.*
import java.util.concurrent.LinkedBlockingQueue

/**
 * A class which manages [ScheduledTask]s.
 *
 * @author Graham
 */
class Scheduler {
    /**
     * The Queue of tasks that are pending execution.
     */
    private val pending: Queue<ScheduledTask> = LinkedBlockingQueue()

    /**
     * The List of currently active tasks.
     */
    private val active: MutableList<ScheduledTask> = ArrayList()

    /**
     * Pulses the [Queue] of [ScheduledTask]s, removing those that are no longer running.
     */
    fun pulse() {
        try {
            Misc.pollAll(pending) { e: ScheduledTask -> active.add(e) }
            val iterator = active.iterator()
            while (iterator.hasNext()) {

                // the task from the list
                val task = iterator.next()
                try {
                    // pulsing the task
                    task.pulse()
                } catch (e: Exception) {
                    e.printStackTrace()
                    iterator.remove()
                }

                // so if we've reached the amount of ticks to stop
                // or if the task was forced to stop
                val shouldRemove = task.goalTicks != -1 && task.delayedTickCount >= task.goalTicks || !task.isRunning

                // removes the task from the list if its time
                if (shouldRemove) {
                    iterator.remove()
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    /**
     * Schedules a new task.
     *
     * @param task
     * The task to schedule.
     */
    fun schedule(task: ScheduledTask) {
        check(pending.add(task)) { "Unable to add task " + task.javaClass.simpleName + " to the pending queue." }
    }
}