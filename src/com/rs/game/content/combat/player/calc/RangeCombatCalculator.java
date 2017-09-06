package com.rs.game.content.combat.player.calc;

import com.rs.game.content.combat.CombatAlgorithm;
import com.rs.game.content.combat.player.AbstractCombatCalculator;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.player.Player;
import com.rs.utility.constants.BonusConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/5/2017
 */
public class RangeCombatCalculator extends AbstractCombatCalculator {
	
	@Override
	public double getAttackBonus(Player player) {
		final int attackStyle = player.getCombatDefinitions().getAttackStyle();
		final boolean specialAttack = player.getCombatDefinitions().isUsingSpecialAttack();
		final int weaponId = player.getEquipment().getWeaponId();
		int baseLevel = player.getSkills().getLevelForXp(RANGE);
		int weaponRequirement = player.getEquipment().getWeaponRequirement(RANGE);
		double weaponBonus = 0.0;
		if (baseLevel > weaponRequirement) {
			weaponBonus = (baseLevel - weaponRequirement) * .3;
		}
		int level = player.getSkills().getLevel(RANGE);
		double prayer = player.getPrayer().getRangeMultiplier();
		double additional = 1.0; // Slayer helmet/salve/...
		if (specialAttack) {
			additional += CombatAlgorithm.getSpecialAccuracyModifier(weaponId == -1 ? player.getEquipment().getIdInSlot(SLOT_ARROWS) : weaponId);
		}
		int styleBonus = 0;
		if (attackStyle == 0) {
			styleBonus = 3;
		}
		double effective = Math.floor(((level * prayer) * additional) + styleBonus + weaponBonus);
		int bonus = player.getCombatDefinitions().getBonus(BonusConstants.RANGE_ATTACK);
		return (int) Math.floor(((effective + 8) * (bonus + 64)) / 10);
	}
	
	@Override
	public double getDefenceBonus(Actor actor, int weaponId, int attackStyle) {
		int styleBonus = (attackStyle == 2 ? 1 : attackStyle == 3 ? 3 : 0);
		int defenceLevel;
		int bonus;
		double prayer;
		if (actor.isPlayer()) {
			Player player = actor.toPlayer();
			defenceLevel = player.getSkills().getLevel(DEFENCE);
			prayer = player.getPrayer().getDefenceMultiplier();
			bonus = player.getCombatDefinitions().getBonus(RANGE_DEFENCE);
		} else {
			NPC npc = actor.toNPC();
			defenceLevel = npc.getCombatLevel() / 2;
			prayer = 1.0;
			bonus = npc.getBonus(RANGE_DEFENCE);
		}
		double effective = Math.floor((defenceLevel * prayer) + styleBonus);
		return (int) Math.floor(((effective + 8) * (bonus + 64)) / 10);
	}
	
	@Override
	public int getMaximumHit(Player player, double multiplier) {
		final int attackStyle = player.getCombatDefinitions().getAttackStyle();
		final boolean voidEquipped = CombatAlgorithm.fullVoidEquipped(player, 11664, 11675);
		final boolean pernixEquipped = CombatAlgorithm.armourSetEquipped(player, new int[] { SLOT_HAT, SLOT_CHEST, SLOT_LEGS }, "pernix", "pernix", "pernix");
		
		int level = player.getSkills().getLevel(RANGE);
		int bonus = player.getCombatDefinitions().getBonus(RANGED_STRENGTH_BONUS);
		double prayer = player.getPrayer().getRangeMultiplier();
		
		double cumulativeStr = Math.floor(level * prayer);
		double styleBonus = attackStyle == 0 ? 3 : attackStyle == 1 ? 0 : 1;
		cumulativeStr += (8 + styleBonus);
		if (voidEquipped) {
			cumulativeStr *= CombatAlgorithm.fullVoidEquipped(player, 11675) ? 1.125 : 1.1;
		}
		if (pernixEquipped) {
			cumulativeStr += 150;
		}
		final double effective = (((14 + cumulativeStr + (bonus / 8) + ((cumulativeStr * bonus) * 0.016865))) / 10 + 1) * multiplier;
		double maxHit = (Math.round(effective) * 10);
		return (int) maxHit;
	}
}
