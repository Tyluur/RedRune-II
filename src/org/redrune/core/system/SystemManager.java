package org.redrune.core.system;

import org.redrune.core.MajorUpdateWorker;
import org.redrune.core.task.Scheduler;
import org.redrune.core.task.impl.EnergyRestorationTask;
import org.redrune.network.rs666.NetworkHandler;
import org.redrune.utility.backend.OutLogger;

import java.io.IOException;

/**
 * Manages all system operations.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/21/2017
 */
public class SystemManager {
	
	/**
	 * Gets the amount of processors on the computer
	 */
	public static final int PROCESSOR_COUNT = Runtime.getRuntime().availableProcessors();
	
	/**
	 * The update worker instance
	 */
	private static final MajorUpdateWorker MAJOR_UPDATE_WORKER = new MajorUpdateWorker();
	
	/**
	 * The system finalization instance
	 */
	private static final SystemFinalization FINALIZATION = new SystemFinalization();
	
	/**
	 * The instance of hte scheduler
	 */
	private static final Scheduler SCHEDULER = new Scheduler();
	
	/**
	 * Sets default system configuration values
	 */
	public static void setDefaults() {
		System.setOut(new OutLogger(System.out));
	}
	
	/**
	 * Starts the worker
	 */
	public static void start() throws IOException {
		MAJOR_UPDATE_WORKER.start();
		NetworkHandler.bind();
		dumpTasks();
		Runtime.getRuntime().addShutdownHook(FINALIZATION);
	}
	
	/**
	 * Dumps all the tasks
	 */
	private static void dumpTasks() {
		SCHEDULER.schedule(new EnergyRestorationTask());
	}
	
	/**
	 * Gets the scheduler
	 */
	public static Scheduler getScheduler() {
		return SCHEDULER;
	}
	
	/**
	 * Gets the update worker
	 */
	public static MajorUpdateWorker getUpdateWorker() {
		return MAJOR_UPDATE_WORKER;
	}
}