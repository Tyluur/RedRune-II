package org.redrune.engine.worker.boot;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 3/25/2016
 */
class BootTask {
	
	/**
	 * The task to run
	 */
	private final Runnable task;
	
	/**
	 * The identification number of the task
	 */
	private int taskNumber;

	BootTask(Runnable task, int taskNumber) {
		this.task = task;
		this.taskNumber = taskNumber;
	}

    public Runnable getTask() {
        return this.task;
    }

    public int getTaskNumber() {
        return this.taskNumber;
    }

    public void setTaskNumber(int taskNumber) {
        this.taskNumber = taskNumber;
    }
}
