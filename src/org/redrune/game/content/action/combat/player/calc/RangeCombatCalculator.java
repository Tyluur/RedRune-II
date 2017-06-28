package org.redrune.game.content.action.combat.player.calc;

import org.redrune.cache.parse.ItemDefinitionParser;
import org.redrune.game.content.action.combat.StaticCombatFormulae;
import org.redrune.game.content.action.combat.player.CombatTypeCalculator;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.constant.BonusConstants;
import org.redrune.utility.rs.constant.SkillConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/22/2017
 */
public class RangeCombatCalculator implements CombatTypeCalculator {
	
	@Override
	public double totalAggressiveBoost(Player player, Object... params) {
		final int attackStyle = (int) params[0];
		
		int style = attackStyle == 0 ? 3 : attackStyle == 2 ? 1 : 0;
		int attLvl = player.getSkills().getLevel(SkillConstants.RANGE);
		int attackBonus = player.getEquipment().getBonus(RANGE_ATTACK);
		double attackMultiplier = 1.0 + player.getManager().getPrayers().getBasePrayerBoost(SkillConstants.RANGE);
		double accuracyMultiplier = 1.00;
		if (StaticCombatFormulae.fullVoidEquipped(player, 11664, 11675)) {
			accuracyMultiplier += 0.10;
		}
		double cumulativeAtt = attLvl * attackMultiplier + style;
		return (14 + cumulativeAtt + (attackBonus / 8) + ((cumulativeAtt * attackBonus) / 64)) * accuracyMultiplier;
	}
	
	@Override
	public double totalDefensiveBoost(org.redrune.game.node.entity.Entity entity, Object... params) {
		if (entity.isPlayer()) {
			Player player = entity.toPlayer();
			int style = player.getCombatDefinitions().getAttackStyle();
			style = style == 2 ? 1 : style == 3 ? 3 : 0;
			int defLvl = player.getSkills().getLevel(SkillConstants.DEFENCE);
			int defBonus = player.getEquipment().getBonus(BonusConstants.RANGE_DEFENCE);
			double defenceMultiplier = 1.0 + player.getManager().getPrayers().getBasePrayerBoost(SkillConstants.DEFENCE);
			double cumulativeDef = defLvl * defenceMultiplier + style;
			return 14 + cumulativeDef + (defBonus / 8) + ((cumulativeDef * defBonus) / 64);
		} else {
			// TODO: entity defense bonuses
			return 0;
		}
	}
	
	@Override
	public double maximumDamageAppendable(Player player, Object... params) {
		final int attackStyle = (int) params[0];
		final int weaponId = (int) params[1];
		final double multiplier = (double) params[2];
		
		double rangedLvl = player.getSkills().getLevel(SkillConstants.RANGE);
		double styleBonus = attackStyle == 0 ? 3 : attackStyle == 1 ? 0 : 1;
		double effectiveStrength = Math.floor(rangedLvl + player.getManager().getPrayers().getBasePrayerBoost(SkillConstants.PRAYER)) + styleBonus; // TODO: prayer multiplier
		
		// void range equipped?
		if (StaticCombatFormulae.fullVoidEquipped(player, 11664, 11675)) {
			effectiveStrength += Math.floor((player.getSkills().getLevelForXp(SkillConstants.RANGE) / 5) + 1.6);
		}
		
		double strengthBonus = player.getEquipment().getBonus(BonusConstants.RANGED_STRENGTH_BONUS);
		
		String weaponName = ItemDefinitionParser.forId(weaponId).getName().toLowerCase();
		if (weaponName.toLowerCase().contains("crystal bow")) {
			strengthBonus = 0;
		}
		double baseDamage = 5 + (((effectiveStrength + 8) * (strengthBonus + 64)) / 64);
		int maxHit = (int) Math.floor(baseDamage * multiplier);
		
		// full pernix equipped?
		if (StaticCombatFormulae.armourSetEquipped(player, new int[] { SLOT_HAT, SLOT_CHEST, SLOT_LEGS }, "pernix", "pernix", "pernix")) {
			maxHit += 150;
		}
		return maxHit;
	}
}
