package org.redrune.game.content.action.combat;

import org.redrune.game.content.action.Action;
import org.redrune.game.content.action.combat.player.CombatType;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.Misc;
import org.redrune.utility.rs.constant.EquipConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/20/2017
 */
public final class PlayerCombatAction implements Action {
	
	/**
	 * The target we are in combat with
	 */
	private final Entity target;
	
	/**
	 * The type of combat we're engaging in.
	 */
	private CombatType type;
	
	public PlayerCombatAction(Entity target) {
		this.target = target;
	}
	
	@Override
	public boolean start(Player player) {
		type = StaticCombatFormulae.getCombatType(player);
		if (!verifyContinuation(player)) {
			return false;
		}
		player.turnTo(target);
		return true;
	}
	
	@Override
	public boolean process(Player player) {
		type = StaticCombatFormulae.getCombatType(player);
		player.turnTo(target);
		return verifyContinuation(player);
	}
	
	@Override
	public int processOnTicks(Player player) {
		if (!StaticCombatFormulae.isWithinDistance(player, target, type)) {
			return 0;
		}
		final int weaponId = player.getEquipment().getIdInSlot(EquipConstants.SLOT_WEAPON);
		int spellId = -1; // get the player's spell id
		int delay = type.getDelay(player, type == CombatType.MAGIC ? spellId : weaponId);
		boolean usingSpecial = false; // TODO: activate special and use the registry for them.
		
		switch (type) {
			case MELEE:
			case RANGE:
				type.getSwing().run(player, target, weaponId, player.getCombatDefinitions().getAttackStyle());
				break;
			case MAGIC:
				type.getSwing().run(player, target, spellId, player.getCombatDefinitions().getAttackStyle());
				break;
		}
		StaticCombatFormulae.fireCombatListeners(player, target);
		return delay;
	}
	
	@Override
	public void stop(Player player) {
		player.turnTo(null);
	}
	
	/**
	 * Verifies that combat can continue by checking multiple states
	 *
	 * @param player
	 * 		The player in combat
	 */
	private boolean verifyContinuation(Player player) {
		Entity target = this.target;
		// we couldn't find a combat type
		if (type == null) {
			return false;
		}
		// if we are invalid to fight
		if (target == null || (target.isDead() || !target.isRenderable() || !target.attackable(player)) || (player.isDead() || !player.isRenderable() || !player.attackable(target)) || !player.getLocation().withinDistance(target.getLocation(), 16)) {
			return false;
		}
		// TODO: add player freezing/stunning checks
		/*
		//if player is frozen and under, stops attacking, else stands waiting
		if (player.isStunned() || player.isBound())
		    return !Utils.colides(player, target);
		 */
		// if we are on the same position
		if (Misc.colides(player, target)) {
			player.getMovement().resetWalkSteps();
			// if the target is moving [must be going from the collision tile]
			if (target.getMovement().isMoving()) {
				return true;
			}
			player.getMovement().calcFollow(target, true);
			return true;
		}
		// diagonal check
		if (type == CombatType.MELEE && Math.abs(player.getLocation().getX() - target.getLocation().getX()) == 1 && Math.abs(player.getLocation().getY() - target.getLocation().getY()) == 1 && !target.getMovement().hasWalkSteps() && target.getSize() == 1) {
			player.getMovement().resetWalkSteps();
			if (!player.getMovement().addWalkSteps(target.getLocation().getX(), player.getLocation().getY(), 1, true)) {
				player.getMovement().resetWalkSteps();
				player.getMovement().addWalkSteps(player.getLocation().getX(), target.getLocation().getY(), 1, true);
			}
			return true;
		}
		// we're too far away or we can't clip to the target
		if (!Misc.isOnRange(player, target, StaticCombatFormulae.getMinimumDistance(player, type)) || !player.getMovement().clippedProjectileToNode(target, type == CombatType.MELEE && !StaticCombatFormulae.checkAttackPathAsRange(target))) {
			if (!player.getMovement().hasWalkSteps() || target.getMovement().hasWalkSteps()) {
				player.getMovement().resetWalkSteps();
				player.getMovement().calcFollow(target, player.getMovement().isRunning() ? 2 : 1, true);
			}
		} else {
			player.getMovement().resetWalkSteps();
		}
		return true;
	}
	
}
