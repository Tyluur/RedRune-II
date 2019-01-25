package org.redrune.engine.tick.task;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/6/2017
 */
final class WorldTaskInformation {
	
	/**
	 * The task to run
	 */
	@Getter
	@Setter
	private WorldTask task;
	
	/**
	 * The initial delay, in ticks, that the task waits before starting.
	 */
	@Getter
	@Setter
	private int initialTickDelay;
	
	/**
	 * The delay, in ticks, that the task waits after it has ran once.
	 */
	@Getter
	@Setter
	private int repeatTickDelay;
	
	public WorldTaskInformation(WorldTask task, int initialTickDelay, int repeatTickDelay) {
		this.task = task;
		this.initialTickDelay = initialTickDelay;
		this.repeatTickDelay = repeatTickDelay;
		if (repeatTickDelay == -1) {
			task.needRemove = true;
		}
	}
}
