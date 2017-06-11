package org.redrune.network.master.client;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/10/2017
 */
final class MasterClientConnectionListener {
	
	/**
	 * The scheduler
	 */
	private static final ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);
	
	/**
	 * Schedules a reconnection to the server
	 */
	static void scheduleReconnection() {
		System.out.println("Scheduling a reconnection...");
		schedule.schedule(() -> {
			if (!MasterClientHandler.isConnected()) {
				MasterClientHandler.connect();
			} else {
				System.out.println("Avoided a false reconnection...");
			}
		}, 1L, TimeUnit.SECONDS);
	}
	
}

