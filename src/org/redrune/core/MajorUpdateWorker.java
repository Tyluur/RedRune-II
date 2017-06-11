package org.redrune.core;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.world.World;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * The worker for all game sequence operations.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/21/2017
 */
public final class MajorUpdateWorker implements Runnable {
	
	/**
	 * The updating sequence to use.
	 */
	private final SequencialUpdate sequence = new SequencialUpdate();
	
	/**
	 * The amount of ticks that have passed
	 */
	private final AtomicInteger ticks = new AtomicInteger(0);
	
	/**
	 * If the major update worker has started.
	 */
	@Setter
	private boolean started;
	
	/**
	 * The start time of a cycle.
	 */
	private long start;
	
	/**
	 * The last time we finished
	 */
	@Getter
	@Setter
	private long lastEndTime;
	
	@Override
	public void run() {
		while (World.get().isAlive()) {
			try {
				start = System.currentTimeMillis();
				sequence.start();
				sequence.execute();
				sequence.end();
				ticks.set(ticks.get() + 1);
				sleep();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Lets the current thread sleep.
	 *
	 * @throws InterruptedException
	 * 		When the thread is interrupted.
	 */
	private void sleep() throws InterruptedException {
		long duration = 600 - ((System.currentTimeMillis() - start) % 600);
		if (duration > 0) {
			Thread.sleep(duration);
			setLastEndTime(System.currentTimeMillis());
		} else {
			System.err.println("Updating cycle duration took " + -duration + "ms too long!");
		}
	}
	
	/**
	 * Starts the engine
	 */
	public void start() {
		if (started) {
			return;
		}
		setStarted(true);
		EngineWorkingSet.submitEngineWork(this);
	}
	
	/**
	 * Gets the amount of ticks that have passed successfully
	 */
	public int getTicksElapsed() {
		return ticks.get();
	}
	
}