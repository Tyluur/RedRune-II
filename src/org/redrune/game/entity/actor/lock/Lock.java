package org.redrune.game.entity.actor.lock;

import org.redrune.engine.thread.WorldThread;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a lock.
 *
 * @author Emperor
 * @author Aero
 * @author Tyluur <itstyluur@gmail.com>
 */
public class Lock {
	
	/**
	 * The expiration of the lock.
	 */
	private int expiration;
	
	/**
	 * The custom lock elapse.
	 */
	@Getter
	@Setter
	private LockElapse elapse;
	
	/**
	 * The message to be sent when the lock is called upon.
	 */
	@Getter
	@Setter
	private String message;
	
	/**
	 * Constructs a new {@code Lock} {@code Object}.
	 */
	public Lock() {
		this(null);
	}
	
	/**
	 * Constructs a new {@code Lock} {@code Object}.
	 *
	 * @param message
	 * 		The message.
	 */
	public Lock(String message) {
		this.message = message;
	}
	
	/**
	 * Locks for an indefinite time.
	 */
	public void lock() {
		lock(Integer.MAX_VALUE - WorldThread.getTicksPassed());
	}
	
	/**
	 * Locks this lock.
	 *
	 * @param ticks
	 * 		The amount of ticks to lock for.
	 */
	public void lock(int ticks) {
		if (ticks > expiration - WorldThread.getTicksPassed()) {
			this.expiration = WorldThread.getTicksPassed() + ticks;
		}
	}
	
	/**
	 * Unlocks the lock.
	 */
	public void unlock() {
		this.expiration = 0;
	}
	
	/**
	 * Checks if this lock is locked.
	 *
	 * @return {@code True} if so.
	 */
	public boolean isLocked() {
		return expiration > WorldThread.getTicksPassed();
	}
	
}