package org.redrune.game.content.entity.actor.player.action;

import org.redrune.game.entity.actor.player.Player;

public final class ActionManager {
	
	private Player player;
	
	private Action action;
	
	private int actionDelay;
	
	public ActionManager(Player player) {
		this.player = player;
	}
	
	public void process() {
		if (action != null) {
			if (player.isDead()) {
				forceStop();
			} else if (!action.process(player)) {
				forceStop();
			}
		}
		if (actionDelay > 0) {
			actionDelay--;
			return;
		}
		if (action == null) {
			return;
		}
		int delay = action.processWithDelay(player);
		if (delay == -1) {
			forceStop();
			return;
		}
		actionDelay += delay;
	}
	
	public void forceStop() {
		if (action == null) {
			return;
		}
		action.stop(player);
		action = null;
	}
	
	public boolean setAction(Action action) {
		forceStop();
		if (!action.start(player)) {
			return false;
		}
		this.action = action;
		return true;
	}
	
	public int getActionDelay() {
		return actionDelay;
	}
	
	public void setActionDelay(int skillDelay) {
		this.actionDelay = skillDelay;
	}
	
	public void addActionDelay(int skillDelay) {
		this.actionDelay += skillDelay;
	}
	
	public boolean hasSkillWorking() {
		return action != null;
	}

    public Action getAction() {
        return this.action;
    }
}
