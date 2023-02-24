package org.redrune.engine.worker.boot

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 3/25/2016
 */
internal class BootTask(
    /**
     * The task to run
     */
    val task: Runnable,
    /**
     * The identification number of the task
     */
    var taskNumber: Int
)