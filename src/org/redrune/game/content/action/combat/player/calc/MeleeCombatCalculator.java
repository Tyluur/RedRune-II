package org.redrune.game.content.action.combat.player.calc;

import org.redrune.game.content.action.combat.StaticCombatFormulae;
import org.redrune.game.content.action.combat.player.CombatTypeCalculator;
import org.redrune.game.node.entity.player.Player;

/**
 * This class handles all of the melee combat formula calculations
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/21/2017
 */
public final class MeleeCombatCalculator implements CombatTypeCalculator{
	
	@Override
	public double totalAggressiveBoost(Player player, Object... params) {
		final int attackStyle = (int) params[0];
		final int weaponId = (int) params[1];
		
		final int style = attackStyle == 0 ? 3 : attackStyle == 2 ? 1 : 0;
		int attackLevel = player.getSkills().getLevel(ATTACK);
		int attackBonus = player.getEquipment().getBonus(StaticCombatFormulae.getMeleeBonusStyle(weaponId, attackStyle));
		double attackMultiplier = 1.0; // TODO: prayer multipliers [* e.getPrayer().getAttackMultiplier()]
		double accuracyMultiplier = 1.0;
		// if we hae full void equipped, 15% higher damage...
		if (StaticCombatFormulae.fullVoidEquipped(player, 11665, 11676)) {
			accuracyMultiplier *= 0.15;
		}
		double cumulativeAttack = attackLevel * attackMultiplier + style;
		return (14 + cumulativeAttack + (attackBonus / 8) + ((cumulativeAttack * attackBonus) / 64)) * accuracyMultiplier;
	}
	
	@Override
	public double totalDefensiveBoost(Player player, Object... params) {
		final int attackStyle = (int) params[0];
		final int weaponId = (int) params[1];
		
		int style = player.getCombatDefinitions().getAttackStyle();
		style = style == 2 ? 1 : style == 3 ? 3 : 0;
		final int defenceLevel = player.getSkills().getLevel(DEFENCE);
		final int defenceBonus = player.getEquipment().getBonus(StaticCombatFormulae.getMeleeDefenceBonus(StaticCombatFormulae.getMeleeBonusStyle(weaponId, attackStyle)));
		final double defenceMultiplier = 1.0; // TODO: changing attack styles [* .getPrayer().getDefenceMultiplier()]
		final double cumulativeDef = defenceLevel * defenceMultiplier + style;
		return 14 + cumulativeDef + (defenceBonus / 8) + ((cumulativeDef * (defenceBonus)) / 64);
	}
	
	@Override
	public double maximumDamageAppendable(Player player, Object... params) {
		final int attackStyle = (int) params[0];
		final int weaponId = (int) params[1];
		
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
		double max = Math.floor(baseDamage * otherBonus);
		
		// if we use the berserk effect
		if (berserk) {
			max = max * 1.26;
		}
		// uf we have the dharoks armour set equipped
		if (StaticCombatFormulae.armourSetEquipped(player, new int[] { SLOT_HAT, SLOT_AMULET, SLOT_LEGS, SLOT_WEAPON }, "dharok", "dharok", "dharok", "dharok")) {
			int hpLost = player.getMaxHitpoints() - player.getHitpoints();
			max += (hpLost * 0.60) + 1;
			System.out.println("Dharok armour set was equipped.");
		}
		return (int) max;
	}
}
