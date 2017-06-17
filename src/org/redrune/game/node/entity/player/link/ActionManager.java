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
	 * Sets the action
	 *
	 * @param action
	 * 		The action
	 */
	public void startAction(Action action) {
		stopAction();
		if (!action.start(player)) {
			return;
		}
		this.action = action;
	}
	
	/**
	 * Forces the action to stop
	 */
	public void stopAction() {
		if (action == null) {
			return;
		}
		action.stop(player);
		action = null;
	}
	
	/**
	 * Handles the processing of the action
	 */
	public void process() {
		if (action != null) {
			if (player.isDead() || !action.process(player)) {
				stopAction();
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
			stopAction();
			return;
		}
		this.delay += delay;
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
