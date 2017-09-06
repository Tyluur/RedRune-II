package com.rs.game.world.task;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * This class manages all world tasks
 */
public class WorldTasksManager {
	
	/**
	 * The list of tasks that are being processed
	 */
	private static final List<WorldTaskInformation> TASKS = Collections.synchronizedList(new LinkedList<WorldTaskInformation>());
	
	/**
	 * Processes all the tasks in the world
	 */
	public static void processTasks() {
		for (WorldTaskInformation taskInformation : TASKS.toArray(new WorldTaskInformation[TASKS.size()])) {
			try {
				if (taskInformation.getInitialTickDelay() > 0) {
					taskInformation.setInitialTickDelay(taskInformation.getInitialTickDelay() - 1);
					continue;
				}
				// so we can store the ticks passed in the info class
				taskInformation.getTask().setTicksPassed(taskInformation.getTask().getTicksPassed() + 1);
				taskInformation.getTask().run();
				if (taskInformation.getTask().needRemove) {
					TASKS.remove(taskInformation);
				} else {
					taskInformation.setInitialTickDelay(taskInformation.getRepeatTickDelay());
				}
			} catch (Exception e) {
				TASKS.remove(taskInformation);
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Schedules a new task with a 0 initial delay and never repeating
	 */
	public static void schedule(WorldTask task) {
		schedule(task, 0, -1);
	}
	
	/**
	 * Schedules a task with a set delay count but never repeating
	 */
	public static void schedule(WorldTask task, int delayCount) {
		schedule(task, delayCount, -1);
	}
	
	/**
	 * Schedules a task with a set delay count and a set repeat count
	 */
	public static void schedule(WorldTask task, int delayCount, int periodCount) {
		if (task == null || delayCount < 0 || periodCount < 0) {
			return;
		}
		TASKS.add(new WorldTaskInformation(task, delayCount, periodCount));
	}
	
}
