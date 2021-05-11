package game.content.entity.actor.combat.player.calc;

import game.content.entity.actor.combat.CombatAlgorithm;
import game.content.entity.actor.combat.player.AbstractCombatCalculator;
import game.entity.actor.Actor;
import game.entity.actor.npc.NPC;
import game.entity.actor.player.Player;
import utility.constants.BonusConstants;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/4/2017
 */
public class MeleeCombatCalculator extends AbstractCombatCalculator {
	
	@Override
	public double getAttackBonus(Actor actor) {
		final int weaponId = actor.isPlayer() ? actor.toPlayer().getEquipment().getWeaponId() : 0;
		final int attackStyle = actor.isPlayer() ? actor.toPlayer().getCombatDefinitions().getAttackStyle() : actor.toNPC().getCombatDefinitions().getAttackStyle();
		final boolean specialAttack = actor.isPlayer() && actor.toPlayer().getCombatDefinitions().isUsingSpecialAttack();
		
		final int style = CombatAlgorithm.getMeleeBonusStyle(weaponId, attackStyle);
		int baseLevel = actor.isPlayer() ? actor.toPlayer().getSkills().getLevelForXp(ATTACK) : actor.toNPC().getCombatDefinitions().getAttackLevel();
		int weaponRequirement = actor.isPlayer() ? actor.toPlayer().getEquipment().getWeaponRequirement(ATTACK) : 0;
		double weaponBonus = 0.0;
		if (baseLevel > weaponRequirement) {
			weaponBonus = (baseLevel - weaponRequirement) * .3;
		}
		
		final int level = actor.isPlayer() ? actor.toPlayer().getSkills().getLevel(ATTACK) : actor.toNPC().getCombatDefinitions().getAttackLevel();
		final double prayer = actor.isPlayer() ? actor.toPlayer().getPrayer().getAttackMultiplier() : 1.0;
		double additional = 1.0; // Black mask/slayer helmet/salve/...
		// we add the spec modifier
		if (specialAttack) {
			additional += CombatAlgorithm.getSpecialAccuracyModifier(weaponId);
		}
		final int styleBonus = attackStyle == 0 ? 3 : attackStyle == 2 ? 1 : 0;
		int bonus = actor.isPlayer() ? actor.toPlayer().getCombatDefinitions().getBonus(style) : actor.toNPC().getBonus(style);
		double effective = Math.floor(((level * prayer) * additional) + styleBonus + weaponBonus);
		double voidAccuracy = 1.0;
		if (actor.isPlayer() && CombatAlgorithm.fullVoidEquipped(actor.toPlayer(), 11665, 11676)) {
			voidAccuracy = 1.1;
		}
		return (int) Math.floor((((effective + 8) * (bonus + 64)) / 10) * voidAccuracy);
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
	public int getMaximumHit(Actor actor, double multiplier) {
		final int weaponId = actor.isPlayer() ? actor.toPlayer().getEquipment().getWeaponId() : 0;
		final int attackStyle = actor.isPlayer() ? actor.toPlayer().getCombatDefinitions().getAttackStyle() : actor.toNPC().getCombatDefinitions().getAttackStyle();
		
		double strengthLvl = actor.isPlayer() ? actor.toPlayer().getSkills().getLevel(STRENGTH) : actor.toNPC().getCombatDefinitions().getStrengthLevel();
		int xpStyle = CombatAlgorithm.getXpStyle(weaponId, attackStyle);
		double styleBonus = xpStyle == STRENGTH ? 3 : xpStyle == -1 ? 1 : 0;
		// if we use the berserk effect
		boolean berserk = actor.isPlayer() && actor.toPlayer().getEquipment().getIdInSlot(SLOT_AMULET) == 11128 && (weaponId == 6528 || weaponId == 6527 || weaponId == 6523 || weaponId == 6526);
		double otherBonus = berserk ? 1.20 : 1;
		double effectiveStrength = 8 + Math.floor((strengthLvl * (actor.isPlayer() ? actor.toPlayer().getPrayer().getStrengthMultiplier() : 1)) + styleBonus);
		if (actor.isPlayer() && CombatAlgorithm.fullVoidEquipped(actor.toPlayer(), 11665, 11676)) {
			effectiveStrength = Math.floor(effectiveStrength * 1.1);
		}
		// if we have the dharoks armour set equipped
		if (actor.isPlayer() && CombatAlgorithm.armourSetEquipped(actor.toPlayer(), new int[] { SLOT_HAT, SLOT_CHEST, SLOT_LEGS, SLOT_WEAPON }, "dharok", "dharok", "dharok", "dharok")) {
			double dharokMultiplier = 2 - ((double) actor.getHitpoints() / (double) actor.getMaxHitpoints());
			// multiplying the
			otherBonus *= dharokMultiplier;
		}
		double strengthBonus = actor.isPlayer() ? actor.toPlayer().getCombatDefinitions().getBonus(STRENGTH_BONUS) : actor.toNPC().getBonus(STRENGTH);
		double baseDamage = 5 + effectiveStrength * (1 + (strengthBonus / 64));
		double max = Math.floor(baseDamage * multiplier * otherBonus);
		return (int) max;
	}
}
