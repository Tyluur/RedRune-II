package org.redrune.game.node.entity.player.link;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.action.Action;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
public final class ActionManager {
	
	/**
	 * The player
	 */
	@Setter
	private Player player;
	
	/**
	 * The action
	 */
	@Getter
	private Action action;
	
	/**
	 * The delay until the action is processed
	 */
	@Setter
	private int delay;
	
	/**
	 * Handles the processing of the action
	 */
	public void process() {
		if (action != null) {
			if (!action.process(player)) {
				forceStop();
			}
		}
		if (delay > 0) {
			delay--;
			return;
		}
		if (action == null) {
			return;
		}
		int delay = action.processOnTicks(player);
		if (delay == -1) {
			forceStop();
			return;
		}
		this.delay += delay;
	}
	
	/**
	 * Forces the action to stop
	 */
	public void forceStop() {
		if (action == null) {
			return;
		}
		action.stop(player);
		action = null;
	}
	
	/**
	 * Sets the action
	 *
	 * @param action
	 * 		The action
	 * @return True if the action was started successfully, based on {@link Action#start(Player)}
	 */
	public boolean setAction(Action action) {
		forceStop();
		if (!action.start(player)) {
			return false;
		}
		this.action = action;
		return true;
	}
	
	/**
	 * Adds onto the action's processing delay
	 *
	 * @param delay
	 * 		The amount to add
	 */
	public void addDelay(int delay) {
		this.delay += delay;
	}
	
	/**
	 * If there is currently an action
	 */
	public boolean actionExists() {
		return action != null;
	}
	
}
