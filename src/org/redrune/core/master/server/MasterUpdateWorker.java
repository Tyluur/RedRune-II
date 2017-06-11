package org.redrune.core.master.server;

import lombok.Getter;
import lombok.Setter;
import org.redrune.core.EngineWorkingSet;
import org.redrune.network.NetworkConstants;
import org.redrune.utility.Misc;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * The worker for all master server operations
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/10/2017
 */
public class MasterUpdateWorker implements Runnable {
	
	/**
	 * The logger
	 */
	private static final Logger LOGGER = Misc.constructLogger(MasterUpdateWorker.class);
	
	/**
	 * The instance of the repository
	 */
	private final MasterServerRepository repository;
	
	/**
	 * If the worker is running
	 */
	@Getter
	@Setter
	private boolean running;
	
	/**
	 * The last time the world statuses have been checked
	 */
	private long lastStatusCheck = -1;
	
	MasterUpdateWorker(MasterServerRepository repository) {
		this.repository = repository;
	}
	
	/**
	 * Starts the thread
	 */
	public void start() {
		if (running) {
			return;
		}
		setRunning(true);
		EngineWorkingSet.submitEngineWork(this);
		LOGGER.info("Started the master update worker thread.");
	}
	
	@Override
	public void run() {
		while (running) {
			try {
				if (Misc.timeHasPassed(lastStatusCheck, TimeUnit.SECONDS.toMillis(10))) {
					repository.setWorldOnline(1, isWorldOnline(1));
					repository.setWorldOnline(2, isWorldOnline(2));
					lastStatusCheck = System.currentTimeMillis();
				}
				Thread.sleep(100);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		LOGGER.info("The master update worker thread stopped.");
	}
	
	private boolean isWorldOnline(int worldId) {
		return Misc.portIsOpen("localhost", NetworkConstants.BASE_PORT_ID + worldId);
	}
}
