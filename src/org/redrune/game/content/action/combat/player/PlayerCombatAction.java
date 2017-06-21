package org.redrune.game.content.action.combat.player;

import org.redrune.game.content.action.Action;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/20/2017
 */
public final class PlayerCombatAction implements Action {
	
	/**
	 * The target we are in combat with
	 */
	private final Entity target;
	
	public PlayerCombatAction(Entity target) {
		this.target = target;
	}
	
	@Override
	public boolean start(Player player) {
		return false;
	}
	
	@Override
	public boolean process(Player player) {
		return false;
	}
	
	@Override
	public int processOnTicks(Player player) {
		return 0;
	}
	
	@Override
	public void stop(Player player) {
	
	}
	
	/**
	 * Verifies that combat can continue by checking multiple states
	 *
	 * @param player
	 * 		The player verifing
	 */
	private boolean verifyContinuation(Player player) {
		return true;
	}
	
}
