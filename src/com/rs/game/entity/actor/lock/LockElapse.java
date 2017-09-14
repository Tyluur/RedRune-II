package com.rs.game.entity.actor.lock;

import com.rs.game.entity.actor.Actor;

/**
 * Called after the expiration of a custom action lock.
 *
 * @author Aero
 * @author Tyluur <itstyluur@gmail.com>
 */
public interface LockElapse {
	
	/**
	 * Called when a custom action lock has elapsed.
	 *
	 * @param actor
	 * 		The actor.
	 * @param lock
	 * 		The custom action lock.
	 */
	void elapse(Actor actor, Lock lock);
	
}