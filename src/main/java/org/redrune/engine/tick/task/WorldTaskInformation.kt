package org.redrune.engine.tick.task

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/6/2017
 */
internal class WorldTaskInformation(
    /**
     * The task to run
     */
    var task: WorldTask,
    /**
     * The initial delay, in ticks, that the task waits before starting.
     */
    var initialTickDelay: Int,
    /**
     * The delay, in ticks, that the task waits after it has ran once.
     */
    var repeatTickDelay: Int,
) {

    init {
        if (repeatTickDelay == -1) {
            task.isNeedRemove = true
        }
    }
}