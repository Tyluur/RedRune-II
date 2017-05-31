package org.redrune.core.task;

import com.google.common.base.Preconditions;
import lombok.Getter;

/**
 * A game-related task that is scheduled to run in the future.
 *
 * @author Graham
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/26/2017
 */
public abstract class ScheduledTask {
	
	/**
	 * The maximum amount of pulses that can be ran on this task
	 */
	@Getter
	private final int maxPulses;
	
	/**
	 * The delay between executions of the task, in pulses.
	 */
	private int delay;
	
	/**
	 * The number of pulses remaining until the task is next executed.
	 */
	@Getter
	private int pulses;
	
	/**
	 * The amount of times this task has been pulsed
	 */
	@Getter
	private int pulseCount = 0;
	
	/**
	 * A flag indicating if the task is running.
	 */
	private boolean running = true;
	
	/**
	 * Creates a new scheduled task.
	 *
	 * @param delay
	 * 		The delay between executions of the task, in pulses.
	 * @param immediate
	 * 		Indicates whether or not this task should be executed immediately, or after the {@code delay}.
	 * @throws IllegalArgumentException
	 * 		If the delay is less than or equal to zero.
	 */
	public ScheduledTask(int delay, boolean immediate) {
		this(delay, 0, immediate);
	}
	
	/**
	 * Creates a new scheduled task.
	 *
	 * @param delay
	 * 		The delay between executions of the task, in pulses.
	 * @param maxPulses
	 * 		The maximum amount of pulses that this task can go through
	 * @param immediate
	 * 		Indicates whether or not this task should be executed immediately, or after the {@code delay}.
	 * @throws IllegalArgumentException
	 * 		If the delay is less than or equal to zero.
	 */
	public ScheduledTask(int delay, int maxPulses, boolean immediate) {
		setDelay(delay);
		this.pulses = immediate ? 0 : delay;
		this.maxPulses = maxPulses;
	}
	
	/**
	 * Sets the delay.
	 *
	 * @param delay
	 * 		The delay.
	 * @throws IllegalArgumentException
	 * 		If the delay is less than zero.
	 */
	public final void setDelay(int delay) {
		Preconditions.checkArgument(delay >= 0, "Delay cannot be less than 0.");
		this.delay = delay;
	}
	
	/**
	 * Checks if this task is running.
	 *
	 * @return {@code true} if so, {@code false} if not.
	 */
	public final boolean isRunning() {
		return running;
	}
	
	/**
	 * Stops the task.
	 */
	public void stop() {
		running = false;
	}
	
	/**
	 * Pulses this task: updates the delay and calls {@link #execute()} if necessary.
	 */
	final void pulse() {
		if (running && --pulses <= 0) {
			execute();
			pulseCount++;
			pulses = delay;
		}
	}
	
	/**
	 * Executes this task.
	 */
	public abstract void execute();
	
}