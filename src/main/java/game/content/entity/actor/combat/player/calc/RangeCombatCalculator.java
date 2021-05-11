package game.content.entity.actor.combat.player.calc;

import game.content.entity.actor.combat.CombatAlgorithm;
import game.content.entity.actor.combat.player.AbstractCombatCalculator;
import game.entity.actor.Actor;
import game.entity.actor.npc.NPC;
import game.entity.actor.player.Player;
import utility.constants.BonusConstants;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/5/2017
 */
public class RangeCombatCalculator extends AbstractCombatCalculator {
	
	@Override
	public double getAttackBonus(Actor actor) {
		final int weaponId = actor.isPlayer() ? actor.toPlayer().getEquipment().getWeaponId() : 0;
		final int attackStyle = actor.isPlayer() ? actor.toPlayer().getCombatDefinitions().getAttackStyle() : actor.toNPC().getCombatDefinitions().getAttackStyle();
		final boolean specialAttack = actor.isPlayer() && actor.toPlayer().getCombatDefinitions().isUsingSpecialAttack();
		
		int baseLevel = actor.isPlayer() ? actor.toPlayer().getSkills().getLevelForXp(RANGE) : actor.toNPC().getCombatDefinitions().getRangeLevel();
		int weaponRequirement = actor.isPlayer() ? actor.toPlayer().getEquipment().getWeaponRequirement(RANGE) : 0;
		double weaponBonus = 0.0;
		if (baseLevel > weaponRequirement) {
			weaponBonus = (baseLevel - weaponRequirement) * .3;
		}
		final int level = actor.isPlayer() ? actor.toPlayer().getSkills().getLevel(RANGE) : actor.toNPC().getCombatDefinitions().getRangeLevel();
		final double prayer = actor.isPlayer() ? actor.toPlayer().getPrayer().getRangeMultiplier() : 1.0;
		double additional = 1.0; // Slayer helmet/salve/...
		if (actor.isPlayer() && specialAttack) {
			additional += CombatAlgorithm.getSpecialAccuracyModifier(weaponId == -1 ? actor.toPlayer().getEquipment().getIdInSlot(SLOT_ARROWS) : weaponId);
		}
		int styleBonus = 0;
		if (attackStyle == 0) {
			styleBonus = 3;
		}
		double effective = Math.floor(((level * prayer) * additional) + styleBonus + weaponBonus);
		
		int bonus = actor.isPlayer() ? actor.toPlayer().getCombatDefinitions().getBonus(BonusConstants.RANGE_ATTACK) : actor.toNPC().getBonus(BonusConstants.RANGE_ATTACK);
		double voidAccuracy = 1.0;
		if (actor.isPlayer() && CombatAlgorithm.fullVoidEquipped(actor.toPlayer(), 11664, 11675)) {
			voidAccuracy = 1.1;
		}
		return (int) Math.floor(((effective + 8) * (bonus + 64)) / 10) * voidAccuracy;
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
	public int getMaximumHit(Actor actor, double multiplier) {
		final int attackStyle = actor.isPlayer() ? actor.toPlayer().getCombatDefinitions().getAttackStyle() : actor.toNPC().getCombatDefinitions().getAttackStyle();
		final boolean voidEquipped = actor.isPlayer() && CombatAlgorithm.fullVoidEquipped(actor.toPlayer(), 11664, 11675);
		final boolean pernixEquipped = actor.isPlayer() && CombatAlgorithm.armourSetEquipped(actor.toPlayer(), new int[] { SLOT_HAT, SLOT_CHEST, SLOT_LEGS }, "pernix", "pernix", "pernix");
		
		double level = actor.isPlayer() ? actor.toPlayer().getSkills().getLevel(RANGE) : actor.toNPC().getCombatDefinitions().getRangeLevel();
		int bonus = actor.isPlayer() ? actor.toPlayer().getCombatDefinitions().getBonus(RANGED_STRENGTH_BONUS) : actor.toNPC().getBonus(RANGED_STRENGTH_BONUS);
		double prayer = actor.isPlayer() ? actor.toPlayer().getPrayer().getRangeMultiplier() : 1.0D;
		
		double cumulativeStr = Math.floor(level * prayer);
		double styleBonus = attackStyle == 0 ? 3 : attackStyle == 1 ? 0 : 1;
		cumulativeStr += (8 + styleBonus);
		if (voidEquipped) {
			cumulativeStr *= actor.isPlayer() && CombatAlgorithm.fullVoidEquipped(actor.toPlayer(), 11675) ? 1.125 : 1.1;
		}
		if (pernixEquipped) {
			cumulativeStr += 150;
		}
		final double effective = (((14 + cumulativeStr + (bonus / 8) + ((cumulativeStr * bonus) * 0.016865))) / 10 + 1) * multiplier;
		double maxHit = (Math.round(effective) * 10);
		return (int) maxHit;
	}
}
