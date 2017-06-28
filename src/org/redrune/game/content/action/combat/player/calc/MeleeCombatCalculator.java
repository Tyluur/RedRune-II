package org.redrune.game.content.action.combat.player.calc;

import org.redrune.game.content.action.combat.StaticCombatFormulae;
import org.redrune.game.content.action.combat.player.CombatTypeCalculator;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.constant.SkillConstants;

/**
 * This class handles all of the melee combat formula calculations
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/21/2017
 */
public final class MeleeCombatCalculator implements CombatTypeCalculator {
	
	@Override
	public double totalAggressiveBoost(Player player, Object... params) {
		final int attackStyle = (int) params[0];
		final int weaponId = (int) params[1];
		final int style = attackStyle == 0 ? 3 : attackStyle == 2 ? 1 : 0;
		// the attack level
		final int attackLevel = player.getSkills().getLevel(ATTACK);
		// the attack bonus from the weapon
		final int attackBonus = player.getEquipment().getBonus(StaticCombatFormulae.getMeleeBonusStyle(weaponId, attackStyle));
		
		// the prayer attack bonus
		double attackMultiplier = 1.0 + player.getManager().getPrayers().getBasePrayerBoost(SkillConstants.ATTACK);
		
		// if we have full void equipped, 15% higher damage...
		if (StaticCombatFormulae.fullVoidEquipped(player, 11665, 11676)) {
			attackMultiplier += 0.15;
		}
		double cumulativeAttack = attackLevel * attackMultiplier + style;
		return (14 + cumulativeAttack + (attackBonus / 8) + ((cumulativeAttack * attackBonus) / 64)) * attackMultiplier;
	}
	
	@Override
	public double totalDefensiveBoost(org.redrune.game.node.entity.Entity entity, Object... params) {
		if (entity.isPlayer()) {
			// the attack style of the player
			final int attackStyle = (int) params[0];
			// the weapon id of the player
			final int weaponId = (int) params[1];
			// the attack style of the receiver
			final int targetStyle = entity.getCombatDefinitions().getAttackStyle();
			
			int styleType = targetStyle == 2 ? 1 : targetStyle == 3 ? 3 : 0;
			final int defenceLevel = entity.toPlayer().getSkills().getLevel(DEFENCE);
			final int defenceBonus = entity.toPlayer().getEquipment().getBonus(StaticCombatFormulae.getMeleeDefenceBonus(StaticCombatFormulae.getMeleeBonusStyle(weaponId, attackStyle)));
			final double defenceMultiplier = 1.0 + entity.toPlayer().getManager().getPrayers().getBasePrayerBoost(SkillConstants.DEFENCE);
			final double defenceCumulation = defenceLevel * defenceMultiplier + styleType;
			return 14 + defenceCumulation + (defenceBonus / 8) + ((defenceCumulation * (defenceBonus)) / 64);
		} else {
			// TODO: npc defence bonuses
			return 0;
		}
	}
	
	@Override
	public double maximumDamageAppendable(Player player, Object... params) {
		final int attackStyle = (int) params[0];
		final int weaponId = (int) params[1];
		final double multiplier = (double) params[2];
		
		double strengthLvl = player.getSkills().getLevel(STRENGTH);
		int xpStyle = StaticCombatFormulae.getXpStyle(weaponId, attackStyle);
		double styleBonus = xpStyle == STRENGTH ? 3 : xpStyle == -1 ? 1 : 0;
		double otherBonus = 1;
		double effectiveStrength = 8 + Math.floor((strengthLvl * 1/*player.getPrayer().getStrengthMultiplier()*/) + styleBonus); // TODO: prayer str boosts
		if (StaticCombatFormulae.fullVoidEquipped(player, 11665, 11676)) {
			effectiveStrength = Math.floor(effectiveStrength * 1.1);
		}
		double strengthBonus = player.getEquipment().getBonus(STRENGTH_BONUS);
		double baseDamage = 5 + effectiveStrength * (1 + (strengthBonus / 64));
		boolean berserk = player.getEquipment().getIdInSlot(SLOT_AMULET) == 11128 && (weaponId == 6528 || weaponId == 6527 || weaponId == 6523 || weaponId == 6526);
		double max = Math.floor(baseDamage * multiplier * otherBonus);
		
		// if we use the berserk effect
		if (berserk) {
			max = max * 1.26;
		}
		// uf we have the dharoks armour set equipped
		if (StaticCombatFormulae.armourSetEquipped(player, new int[] { SLOT_HAT, SLOT_CHEST, SLOT_LEGS, SLOT_WEAPON }, "dharok", "dharok", "dharok", "dharok")) {
			int hpLost = player.getMaxHitpoints() - player.getHitpoints();
			max += (hpLost * 0.60) + 1;
		}
		return (int) max;
	}
}
