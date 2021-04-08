package org.redrune.engine.tick.task

import java.util.*

/**
 * This class manages all world tasks
 */
object WorldTasksManager {
    /**
     * The list of tasks that are being processed
     */
    private val TASKS = Collections.synchronizedList(LinkedList<WorldTaskInformation>())

    /**
     * Processes all the tasks in the world
     */
    fun processTasks() {
        for (taskInformation in TASKS.toTypedArray()) {
            try {
                if (taskInformation.initialTickDelay > 0) {
                    taskInformation.initialTickDelay = taskInformation.initialTickDelay - 1
                    continue
                }
                // so we can store the ticks passed in the info class
                taskInformation.task.ticksPassed = taskInformation.task.ticksPassed + 1
                taskInformation.task.run()
                if (taskInformation.task.isNeedRemove) {
                    TASKS.remove(taskInformation)
                } else {
                    taskInformation.initialTickDelay = taskInformation.repeatTickDelay
                }
            } catch (e: Exception) {
                TASKS.remove(taskInformation)
                e.printStackTrace()
            }
        }
    }
    /**
     * Schedules a task with a set delay count and a set repeat count
     */
    /**
     * Schedules a new task with a 0 initial delay and never repeating
     */
    /**
     * Schedules a task with a set delay count but never repeating
     */
    @JvmStatic
	@JvmOverloads
    fun schedule(task: WorldTask?, delayCount: Int = 0, periodCount: Int = -1) {
        if (task == null || delayCount < 0) {
            return
        }
        TASKS.add(WorldTaskInformation(task, delayCount, periodCount))
    }
}