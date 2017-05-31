package org.redrune.core.task;

import org.redrune.utility.Misc;

import java.util.*;

/**
 * A class which manages {@link ScheduledTask}s.
 *
 * @author Graham
 */
public final class Scheduler {
	
	/**
	 * The Queue of tasks that are pending execution.
	 */
	private final Queue<ScheduledTask> pending = new ArrayDeque<>();
	
	/**
	 * The List of currently active tasks.
	 */
	private final List<ScheduledTask> active = new ArrayList<>();
	
	/**
	 * Pulses the {@link Queue} of {@link ScheduledTask}s, removing those that are no longer running.
	 */
	public void pulse() {
		try {
			Misc.pollAll(pending, active::add);
			
			for (Iterator<ScheduledTask> iterator = active.iterator(); iterator.hasNext();) {
				ScheduledTask task = iterator.next();
				task.pulse();
				
				final boolean shouldRemove = (task.getMaxPulses() > 0 && task.getMaxPulses() == task.getPulseCount()) || !task.isRunning();
				if (shouldRemove) {
					iterator.remove();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Schedules a new task.
	 *
	 * @param task The task to schedule.
	 * @return {@code true} if the task was added successfully.
	 */
	public boolean schedule(ScheduledTask task) {
		return pending.add(task);
	}
	
}