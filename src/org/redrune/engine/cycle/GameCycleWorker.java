package org.redrune.engine.cycle;

import lombok.Getter;
import org.redrune.engine.SystemManager;
import org.redrune.game.global.World;
import org.redrune.utility.functions.Misc;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public final class GameCycleWorker implements Runnable {
	
	/**
	 * The timestamp of the end of the last cycle
	 */
	public static long lastCycleTime;
	
	/**
	 * The amount of ticks that have passed since the game started
	 */
	@Getter
	private static int ticksPassed = 0;
	
	/**
	 * The instance of the update sequence
	 */
	private final UpdateSequence updateSequence = new UpdateSequence();
	
	/**
	 * The executor.
	 */
	private final Executor EXECUTOR = Executors.newSingleThreadExecutor();
	
	/**
	 * If the major update worker has started.
	 */
	private boolean started;
	
	public GameCycleWorker() {
	
	}
	
	@Override
	public final void run() {
		while (!SystemManager.shutdown) {
			long currentTime = Misc.currentTimeMillis();
			try {
				updateSequence.fire(World.getPlayers(), World.getNPCs());
			} catch (Throwable e) {
				e.printStackTrace();
			}
			sleepThread(currentTime);
		}
	}
	
	/**
	 * Handles the sleeping of the thread
	 */
	private void sleepThread(long currentTime) {
		lastCycleTime = Misc.currentTimeMillis();
		long sleepTime = 600 + currentTime - lastCycleTime;
		if (sleepTime <= 0) {
			return;
		}
		ticksPassed++;
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Starts the worker
	 */
	public void start() {
		if (started) {
			return;
		}
		started = true;
		EXECUTOR.execute(GameCycleWorker.this);
		System.out.println("Main game cycle worker started.");
	}
}
