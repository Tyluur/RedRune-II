package org.redrune.game.content.entity.actor.player.action;

import org.redrune.game.entity.actor.player.Player;

public abstract class Action {
	
	public abstract boolean start(Player player);
	
	public abstract boolean process(Player player);
	
	public abstract int processWithDelay(Player player);
	
	public abstract void stop(Player player);
	
	protected final void setActionDelay(Player player, int delay) {
		player.getActionManager().setActionDelay(delay);
	}
}
