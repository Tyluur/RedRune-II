package game.content.entity.actor.player.action.impl;

import game.content.entity.actor.combat.CombatAlgorithm;
import game.content.entity.actor.combat.player.CombatStyle;
import game.content.entity.actor.player.action.Action;
import game.entity.actor.Actor;
import game.entity.actor.player.Player;
import utility.constants.key.AttributeKey;
import utility.functions.Misc;

/**
 * This class handles the player combat action.
 *
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/4/17
 */
public class PlayerCombatAction extends Action {
	
	/**
	 * The target of our combat action
	 */
	private final Actor target;
	
	/**
	 * The combat style
	 */
	private CombatStyle style;
	
	/**
	 * Constructs a new combat action
	 */
	public PlayerCombatAction(Actor target) {
		this.target = target;
	}
	
	@Override
	public boolean start(Player player) {
		style = CombatAlgorithm.findCombatStyle(player);
		player.setNextFaceActor(target);
		if (!checkAll(player)) {
			return false;
		}
		player.setNextFaceActor(null);
		return true;
	}
	
	@Override
	public boolean process(Player player) {
		style = CombatAlgorithm.findCombatStyle(player);
		if (!checkAll(player)) {
			return false;
		}
		player.setNextFaceActor(target);
		if (target != null) {
			target.putTemporaryAttribute("last_target", player);
			target.putTemporaryAttribute("last_time_combatted", System.currentTimeMillis());
			player.putTemporaryAttribute("combat_target", target);
		}
		checkSpecials(player);
		player.putTemporaryAttribute("last_time_combatted", System.currentTimeMillis());
		return checkAll(player);
	}
	
	@Override
	public int processWithDelay(Player player) {
		// we are too far away, combat is halted [but not quit]
		if (!CombatAlgorithm.isWithinDistance(player, target, style)) {
			return 0;
		}
		player.setNextFaceActor(target);
		// the id of the weapon equipped
		final int weaponId = player.getEquipment().getWeaponId();
		// the spell we're casting
		final int spellId = player.getCombatDefinitions().getRealSpellId();
		// the id of the combat flag [weapon or spell id]
		final int id = style == CombatStyle.MAGIC ? spellId : weaponId;
		// the delay we will have
		final int delay = style.getDelay(player);
		// the multiplier on the speed of the combat action
		double multiplier = 1.0;
		// the delay wasn't found [this is only possible when we don't have a magic spell
		// otherwise, delays are calculated in the swing
		if (delay == -1) {
			player.getPackets().sendMessage((style == CombatStyle.MAGIC ? "Spell #" + id + "" : "Weapon #" + weaponId) + " has not yet been added, please report this on the forums.");
			player.getCombatDefinitions().resetSpells(true);
			return -1;
		}
		if (player.getTemporaryAttribute(AttributeKey.MIASMIC_EFFECT) == Boolean.TRUE) {
			multiplier = 1.5;
		}
		if (!style.getStyle().fireSwing(player, target)) {
			return -1;
		}
		// after combat has been sent, we must send listeners
		CombatAlgorithm.fireCombatListeners(player, target);
		return (int) (delay * multiplier);
	}
	
	@Override
	public void stop(Player player) {
		// otherwise we don't face when casting magic
		if (style != null && style != CombatStyle.MAGIC) {
			player.setNextFaceActor(null);
		}
	}
	
	private boolean checkAll(Player player) {
		Actor target = this.target;
		// we couldn't find a combat type
		if (style == null) {
			return false;
		}
		// if we are invalid to fight
		if (!CombatAlgorithm.canFight(player, target)) {
			return false;
		}
		// if player is frozen and under, stops attacking, else stands waiting
		if (player.isFrozen()) { // TODO stunned [should it be same flag as frozen?]
			return !Misc.colides(player, target);
		}
		// if we are on the same position
		if (Misc.colides(player, target)) {
			player.resetWalkSteps();
			// if the target is moving [must be going from the collision tile]
			if (target.isMoving()) {
				return true;
			}
			player.calcFollow(target, true);
			return true;
		}
		if (!player.getControllerManager().keepCombating(target)) {
			return false;
		}
		// we're too far away or we can't clip to the target
		if (!Misc.isOnRange(player, target, CombatAlgorithm.getMinimumDistance(player, style)) || !player.clipedProjectile(target, style == CombatStyle.MELEE && !CombatAlgorithm.checkAttackPathAsRange(target))) {
			if (!player.isMoving() || target.isMoving()) {
				player.resetWalkSteps();
				player.calcFollow(target, player.isRunModeOn() ? 2 : 1, true, true);
			}
		} else {
			player.resetWalkSteps();
		}
		// diagonal check
		if (style == CombatStyle.MELEE && Math.abs(player.getX() - target.getX()) == 1 && Math.abs(player.getY() - target.getY()) == 1 && !target.hasWalkSteps() && target.getSize() == 1) {
			player.resetWalkSteps();
			if (!player.addWalkSteps(target.getX(), player.getY(), 1, true)) {
				player.resetWalkSteps();
				player.addWalkSteps(player.getX(), target.getY(), 1, true);
			}
			return true;
		}
		if (!(target.isNPC() && target.toNPC().isForceMultiAttacked())) {
			if (!target.isInMultiArea() || !player.isInMultiArea()) {
				if (player.getAttackedBy() != target && player.getAttackedByDelay() > System.currentTimeMillis()) {
					player.getPackets().sendMessage("I'm already under attack.");
					return false;
				}
				if (target.getAttackedBy() != player && target.getAttackedByDelay() > System.currentTimeMillis()) {
					player.getPackets().sendMessage("Someone else is already fighting " + (target.isNPC() ? "that." : "your opponent."));
					return false;
				}
			}
		}
		if (player.getAttributes().getPolDelay() >= Misc.currentTimeMillis() && !(player.getEquipment().getWeaponId() == 15486 || player.getEquipment().getWeaponId() == 22207 || player.getEquipment().getWeaponId() == 22209 || player.getEquipment().getWeaponId() == 22211 || player.getEquipment().getWeaponId() == 22213)) {
			player.getAttributes().setPolDelay(0);
		}
		// anything else ?
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
		CombatAlgorithm.checkSpecialToggle(player, 0);
	}

    public Actor getTarget() {
        return this.target;
    }
}
