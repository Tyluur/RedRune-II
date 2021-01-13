package org.redrune.engine.tick.task;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/6/2017
 */
final class WorldTaskInformation {
	
	/**
	 * The task to run
	 */
	private WorldTask task;
	
	/**
	 * The initial delay, in ticks, that the task waits before starting.
	 */
	private int initialTickDelay;
	
	/**
	 * The delay, in ticks, that the task waits after it has ran once.
	 */
	private int repeatTickDelay;
	
	public WorldTaskInformation(WorldTask task, int initialTickDelay, int repeatTickDelay) {
		this.task = task;
		this.initialTickDelay = initialTickDelay;
		this.repeatTickDelay = repeatTickDelay;
		if (repeatTickDelay == -1) {
			task.needRemove = true;
		}
	}

    public WorldTask getTask() {
        return this.task;
    }

    public int getInitialTickDelay() {
        return this.initialTickDelay;
    }

    public int getRepeatTickDelay() {
        return this.repeatTickDelay;
    }

    public void setTask(WorldTask task) {
        this.task = task;
    }

    public void setInitialTickDelay(int initialTickDelay) {
        this.initialTickDelay = initialTickDelay;
    }

    public void setRepeatTickDelay(int repeatTickDelay) {
        this.repeatTickDelay = repeatTickDelay;
    }
}
