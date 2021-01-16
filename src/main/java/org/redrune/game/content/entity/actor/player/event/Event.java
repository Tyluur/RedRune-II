package org.redrune.game.content.entity.actor.player.event;

import org.redrune.game.entity.actor.player.Player;

/**
 * This class provides the structure for any event performed by the player
 *
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-01-25
 */
public abstract class Event {
	
	/**
	 * Runs the event
	 *
	 * @param player
	 * 		The player running the event
	 */
	public abstract void run(Player player);
	
	/**
	 * The policies of the event
	 */
	public abstract EventPolicy[] policies();
	
	/**
	 * If the event has started
	 */
	protected EventState state = EventState.CREATED;
	
	/**
	 * Converts varags arguments into an array
	 *
	 * @param policies
	 * 		The policies
	 */
	protected EventPolicy[] arguments(EventPolicy... policies) {
		return policies;
	}
	
	/**
	 * Fires the preconditonal policies for the event
	 *
	 * @param player
	 * 		The player to fire them for
	 */
	public void firePolicy(Player player) {
		for (EventPolicy policy : policies()) {
			switch (policy) {
				case CLOSE_INTERFACE:
					player.stopAll(false, true);
					break;
				case STOP_WALK:
					player.stopAll(true, false);
					break;
			}
		}
	}

    public EventState getState() {
        return this.state;
    }

    public void setState(EventState state) {
        this.state = state;
    }

    /**
	 * @author Tyluur <itstyluur@icloud.com>
	 * @since 2019-01-25
	 */
	public enum EventState {
		CREATED,
		STARTED,
		FINISHED
	}
	
	/**
	 * The policies for the event that must be followed prior to execution
	 */
	public enum EventPolicy {
		CLOSE_INTERFACE,
		STOP_WALK,
	}
}
