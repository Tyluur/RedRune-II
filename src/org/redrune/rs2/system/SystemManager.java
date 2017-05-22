package org.redrune.rs2.system;

import org.redrune.engine.MajorUpdateWorker;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/21/2017
 */
public class SystemManager {
	
	/**
	 * The update engine
	 */
	private static final MajorUpdateWorker MAJOR_UPDATE_WORKER = new MajorUpdateWorker();
	
	/**
	 * Starts the worker
	 */
	public static void start() {
		MAJOR_UPDATE_WORKER.start();
	}
	
}
