package game.entity.actor.lock;

import engine.cycle.GameCycleWorker;

/**
 * Represents a lock.
 *
 * @author Emperor
 * @author Aero
 * @author Tyluur <itstyluur@icloud.com>
 */
public class Lock {
	
	/**
	 * The expiration of the lock.
	 */
	private int expiration;
	
	/**
	 * The custom lock elapse.
	 */
	private LockElapse elapse;
	
	/**
	 * The message to be sent when the lock is called upon.
	 */
	private String message;
	
	/**
	 * Constructs a new {@code Lock} {@code Object}.
	 */
	Lock() {
		this(null);
	}
	
	/**
	 * Constructs a new {@code Lock} {@code Object}.
	 *
	 * @param message
	 * 		The message.
	 */
	private Lock(String message) {
		this.message = message;
	}
	
	/**
	 * Locks for an indefinite time.
	 */
	public void lock() {
		lock(Integer.MAX_VALUE - GameCycleWorker.getTicksPassed());
	}
	
	/**
	 * Locks this lock.
	 *
	 * @param ticks
	 * 		The amount of ticks to lock for.
	 */
	public void lock(int ticks) {
		if (ticks > expiration - GameCycleWorker.getTicksPassed()) {
			this.expiration = GameCycleWorker.getTicksPassed() + ticks;
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
	boolean isLocked() {
		return expiration > GameCycleWorker.getTicksPassed();
	}

    public LockElapse getElapse() {
        return this.elapse;
    }

    public String getMessage() {
        return this.message;
    }

    public void setElapse(LockElapse elapse) {
        this.elapse = elapse;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}