package org.redrune.core;

import org.redrune.core.system.SystemManager;
import org.redrune.utility.backend.RS2ThreadFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * A working set containing all the main threads, and thread-related factories.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public class EngineWorkingSet {
	
	/**
	 * The pool used for cache work
	 */
	private static final ExecutorService CACHE_SERVICE_POOL = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new RS2ThreadFactory("JS5-Worker"));
	
	/**
	 * The service that handles incoming packets
	 */
	private static final ExecutorService PACKET_SERVICE = Executors.newSingleThreadExecutor();
	
	/**
	 * The logic worker.
	 */
	private static final Executor LOGIC_SERVICE = Executors.newSingleThreadExecutor(new RS2ThreadFactory("GameLogic"));
	
	/**
	 * The executor used for the update server
	 */
	private static final ExecutorService UPDATE_SERVICE = Executors.newFixedThreadPool(SystemManager.PROCESSOR_COUNT);
	
	/**
	 * The scheduled executor service
	 */
	private static ScheduledExecutorService scheduledExecutorService;
	
	/**
	 * Submits a new js5 task to execute.
	 *
	 * @param runnable
	 * 		The js5 task.
	 */
	public static void submitJs5Work(Runnable runnable) {
		CACHE_SERVICE_POOL.execute(runnable);
	}
	
	/**
	 * Submits a new task to execute.
	 *
	 * @param runnable
	 * 		The logic task.
	 */
	public static void submitLogic(Runnable runnable) {
		LOGIC_SERVICE.execute(runnable);
	}
	
	/**
	 * This submits work to the {@link #UPDATE_SERVICE} WORKER. This worker is exclusively for game engine.
	 *
	 * @param runnable
	 * 		The work
	 */
	public static void submitEngineWork(Runnable runnable) {
		UPDATE_SERVICE.execute(runnable);
	}
	
	/**
	 * Gets the scheduled executor service
	 */
	public static ScheduledExecutorService getScheduledExecutorService() {
		if (scheduledExecutorService == null) {
			scheduledExecutorService = SystemManager.PROCESSOR_COUNT >= 6 ? Executors.newScheduledThreadPool(SystemManager.PROCESSOR_COUNT >= 12 ? 4 : 2, new RS2ThreadFactory("Scheduled-Worker")) : Executors.newSingleThreadScheduledExecutor(new RS2ThreadFactory("Scheduled-Worker"));
		}
		return scheduledExecutorService;
	}
	
	/**
	 * Submits work to the packet service thread factory
	 *
	 * @param work
	 * 		The work
	 */
	public static void executePacketWork(Runnable work) {
		PACKET_SERVICE.execute(work);
	}
	
}
