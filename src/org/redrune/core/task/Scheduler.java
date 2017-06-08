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
			
			for (final Iterator<ScheduledTask> iterator = active.iterator(); iterator.hasNext(); ) {
				final ScheduledTask task = iterator.next();
				task.pulse();
				final boolean shouldRemove = (task.getMaxPulses() > 0 && task.getMaxPulses() == task.getPulseCount()) || !task.isRunning();
				if (shouldRemove) {
					iterator.remove();
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Schedules a new task.
	 *
	 * @param task
	 * 		The task to schedule.
	 */
	public void schedule(ScheduledTask task) {
		if (!pending.add(task)) {
			throw new IllegalStateException("Unable to add task " + task.getClass().getSimpleName() + " to the pending queue.");
		}
	}
	
}