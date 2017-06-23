package org.redrune.game.content.action.combat;

import lombok.Getter;
import org.redrune.game.content.action.Action;
import org.redrune.game.content.action.combat.player.CombatRegistry;
import org.redrune.game.content.action.combat.player.CombatType;
import org.redrune.game.content.action.combat.player.registry.SpecialAttackEvent;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.Misc;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/20/2017
 */
public final class PlayerCombatAction implements Action {
	
	/**
	 * The target we are in combat with
	 */
	@Getter
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
		if (!verifyContinuation(player)) {
			return false;
		}
		checkSpecials(player);
		return true;
	}
	
	/**
	 * Checks the special attacks, this sends instant specs as well as toggles the queued special attack on before we
	 * use the weapon.
	 *
	 * @param player
	 * 		The player.
	 */
	private void checkSpecials(Player player) {
		// if the special attack was toggled on and is queued.
		StaticCombatFormulae.checkSpecialToggle(player, 0);
	}
	
	/**
	 * Gets the special attack event
	 *
	 * @param player
	 * 		The player
	 * @param weaponId
	 * 		The id of the weapon we're using
	 * @param usingSpecial
	 * 		If we're using the special attack
	 */
	private Optional<SpecialAttackEvent> getSpecialAttackEvent(Player player, int weaponId, boolean usingSpecial) {
		Optional<SpecialAttackEvent> specialOptional = Optional.empty();
		if (usingSpecial) {
			specialOptional = CombatRegistry.getSpecial(weaponId);
			if (!specialOptional.isPresent()) {
				player.getCombatDefinitions().setSpecialActivated(false);
				player.getTransmitter().sendMessage("Unregistered special attack event, please report this on the forum.");
			}
		}
		return specialOptional;
	}
	
	@Override
	public int processOnTicks(Player player) {
		// we are too far away, combat is halted [but not quit]
		if (!StaticCombatFormulae.isWithinDistance(player, target, type)) {
			return 0;
		}
		player.turnTo(target);
		// the id of the weapon equipped
		final int weaponId = player.getEquipment().getWeaponId();
		// the spell we're casting
		final int spellId = -1; // get the player's spell id
		// the id of the combat flag [weapon or spell id]
		final int id = type == CombatType.MAGIC ? spellId : weaponId;
		// the delay we will have
		final int delay = type.getDelay(player, id);
		// if we're using special
		final boolean usingSpecial = player.getCombatDefinitions().isSpecialActivated();
		// the special attack event
		Optional<SpecialAttackEvent> specialOptional = getSpecialAttackEvent(player, weaponId, usingSpecial);
		// sends the swing
		if (!type.getSwing().run(player, target, id, player.getCombatDefinitions().getAttackStyle(), specialOptional.orElse(null))) {
			return -1;
		}
		// after combat has been sent, we must send listeners
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
