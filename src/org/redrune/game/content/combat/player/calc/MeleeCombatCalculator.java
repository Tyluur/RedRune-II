package org.redrune.game.content.combat.player.calc;

import org.redrune.game.content.combat.player.AbstractCombatCalculator;
import org.redrune.game.content.combat.CombatAlgorithm;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.utility.constants.BonusConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/4/2017
 */
public class MeleeCombatCalculator extends AbstractCombatCalculator {
	
	@Override
	public double getAttackBonus(Player player) {
		final int weaponId = player.getEquipment().getWeaponId();
		final int attackStyle = player.getCombatDefinitions().getAttackStyle();
		final boolean specialAttack = player.getCombatDefinitions().isUsingSpecialAttack();
		
		final int style = CombatAlgorithm.getMeleeBonusStyle(weaponId, attackStyle);
		int baseLevel = player.getSkills().getLevelForXp(ATTACK);
		int weaponRequirement = player.getEquipment().getWeaponRequirement(ATTACK);
		double weaponBonus = 0.0;
		if (baseLevel > weaponRequirement) {
			weaponBonus = (baseLevel - weaponRequirement) * .3;
		}
		
		final int level = player.getSkills().getLevel(ATTACK);
		final double prayer = player.getPrayer().getAttackMultiplier();
		double additional = 1.0; // Black mask/slayer helmet/salve/...
		// we add the spec modifier
		if (specialAttack) {
			additional += CombatAlgorithm.getSpecialAccuracyModifier(weaponId);
		}
		final int styleBonus = attackStyle == 0 ? 3 : attackStyle == 2 ? 1 : 0;
		int bonus = player.getCombatDefinitions().getBonus(style);
		double effective = Math.floor(((level * prayer) * additional) + styleBonus + weaponBonus);
		return (int) Math.floor((((effective + 8) * (bonus + 64)) / 10) * 1.10);
	}
	
	@Override
	public double getDefenceBonus(Actor actor, int weaponId, int attackStyle) {
		// find the combat style we're on for the selected type
		int meleeBonusStyle = CombatAlgorithm.getMeleeBonusStyle(weaponId, attackStyle);
		// the bonus index
		final int bonusIndex = CombatAlgorithm.getMeleeDefenceBonusIndex(meleeBonusStyle);
		// the attack style of the receiver, npcs have default stab attack.
		int targetStyle = BonusConstants.STAB_ATTACK;
		// the defence level
		int defenceLevel;
		// the prayer boost
		double prayer;
		int bonus;
		if (actor.isPlayer()) {
			Player player = actor.toPlayer();
			targetStyle = player.getCombatDefinitions().getAttackStyle();
			defenceLevel = player.getSkills().getLevel(DEFENCE);
			prayer = player.getPrayer().getDefenceMultiplier();
			bonus = player.getCombatDefinitions().getBonus(bonusIndex);
		} else {
			NPC npc = actor.toNPC();
			prayer = 1.0;
			defenceLevel = npc.getCombatLevel() / 2;
			bonus = npc.getBonus(bonusIndex);
		}
		// calculate the bonus of the style we're on after all the setting is done
		int styleBonus = targetStyle == 2 ? 1 : targetStyle == 3 ? 3 : 0;
		double effective = Math.floor((defenceLevel * prayer) + styleBonus);
		return (int) Math.floor(((effective + 8) * (bonus + 64)) / 10);
	}
	
	@Override
	public int getMaximumHit(Player player, double multiplier) {
		final int weaponId = player.getEquipment().getWeaponId();
		final int attackStyle = player.getCombatDefinitions().getAttackStyle();
		
		double strengthLvl = player.getSkills().getLevel(STRENGTH);
		int xpStyle = CombatAlgorithm.getXpStyle(weaponId, attackStyle);
		double styleBonus = xpStyle == STRENGTH ? 3 : xpStyle == -1 ? 1 : 0;
		// if we use the berserk effect
		boolean berserk = player.getEquipment().getIdInSlot(SLOT_AMULET) == 11128 && (weaponId == 6528 || weaponId == 6527 || weaponId == 6523 || weaponId == 6526);
		double otherBonus = berserk ? 1.20 : 1;
		double effectiveStrength = 8 + Math.floor((strengthLvl * player.getPrayer().getStrengthMultiplier()) + styleBonus);
		if (CombatAlgorithm.fullVoidEquipped(player, 11665, 11676)) {
			effectiveStrength = Math.floor(effectiveStrength * 1.1);
		}
		// if we have the dharoks armour set equipped
		if (CombatAlgorithm.armourSetEquipped(player, new int[] { SLOT_HAT, SLOT_CHEST, SLOT_LEGS, SLOT_WEAPON }, "dharok", "dharok", "dharok", "dharok")) {
			double dharokMultiplier = 2 - ((double) player.getHitpoints() / (double) player.getMaxHitpoints());
			// multiplying the
			otherBonus *= dharokMultiplier;
		}
		double strengthBonus = player.getCombatDefinitions().getBonus(STRENGTH_BONUS);
		double baseDamage = 5 + effectiveStrength * (1 + (strengthBonus / 64));
		double max = Math.floor(baseDamage * multiplier * otherBonus);
		return (int) max;
	}
}
