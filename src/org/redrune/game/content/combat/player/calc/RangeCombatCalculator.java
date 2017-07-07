package org.redrune.game.content.combat.player.calc;

import org.redrune.cache.parse.ItemDefinitionParser;
import org.redrune.game.content.combat.StaticCombatFormulae;
import org.redrune.game.content.combat.player.CombatTypeCalculator;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.constant.BonusConstants;
import org.redrune.utility.rs.constant.EquipConstants;
import org.redrune.utility.rs.constant.SkillConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/22/2017
 */
public class RangeCombatCalculator implements CombatTypeCalculator {
	
	@Override
	public double totalAggressiveBoost(Player player, Object... params) {
		final int attackStyle = (int) params[0];
		final boolean specialAttack = (boolean) params[1];
		final int weaponId = player.getEquipment().getWeaponId();
		int baseLevel = player.getSkills().getLevelForXp(RANGE);
		int weaponRequirement = player.getEquipment().getWeaponRequirement(RANGE);
		double weaponBonus = 0.0;
		if (baseLevel > weaponRequirement) {
			weaponBonus = (baseLevel - weaponRequirement) * .3;
		}
		int level = player.getSkills().getLevel(RANGE);
		double prayer = player.getManager().getPrayers().getBasePrayerBoost(RANGE);
		double additional = 1.0; // Slayer helmet/salve/...
		if (specialAttack) {
			additional += StaticCombatFormulae.getSpecialAccuracyModifier(weaponId == -1 ? player.getEquipment().getIdInSlot(EquipConstants.SLOT_ARROWS) : weaponId);
		}
		int styleBonus = 0;
		if (attackStyle == 0) {
			styleBonus = 3;
		}
		double effective = Math.floor(((level * prayer) * additional) + styleBonus + weaponBonus);
		int bonus = player.getEquipment().getBonus(RANGE_ATTACK);
		return (int) Math.floor(((effective + 8) * (bonus + 64)) / 10);
	}
	
	@Override
	public double totalDefensiveBoost(Entity entity, Object... params) {
		if (entity.isPlayer()) {
			Player player = entity.toPlayer();
			int style = (int) params[0];
			int styleBonus = (style == 2 ? 1 : style == 3 ? 3 : 0);
			int level = player.getSkills().getLevel(DEFENCE);
			double prayer = player.getManager().getPrayers().getBasePrayerBoost(SkillConstants.DEFENCE);
			double effective = Math.floor((level * prayer) + styleBonus);
			int equipment = player.getEquipment().getBonus(BonusConstants.RANGE_DEFENCE);
			return (int) Math.floor(((effective + 8) * (equipment + 64)) / 10);
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
		double effectiveStrength = Math.floor(rangedLvl + player.getManager().getPrayers().getBasePrayerBoost(SkillConstants.PRAYER)) + styleBonus;
		
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
