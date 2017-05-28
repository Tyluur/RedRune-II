package org.redrune.rs2.system;

import org.redrune.engine.MajorUpdateWorker;
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
	 * The update worker instance
	 */
	private static final MajorUpdateWorker MAJOR_UPDATE_WORKER = new MajorUpdateWorker();
	
	/**
	 * The system finalization instance
	 */
	private static final SystemFinalization FINALIZATION = new SystemFinalization();
	
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
		NetworkHandler.bind();
		MAJOR_UPDATE_WORKER.start();
		Runtime.getRuntime().addShutdownHook(FINALIZATION);
	}
	
}