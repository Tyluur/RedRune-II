package game.content.entity.actor.combat.player.calc;

import game.content.entity.actor.combat.CombatAlgorithm;
import game.content.entity.actor.combat.player.AbstractCombatCalculator;
import game.entity.actor.Actor;
import game.entity.actor.npc.NPC;
import game.entity.actor.player.Player;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/8/2017
 */
public class MagicCombatCalculator extends AbstractCombatCalculator {
	
	@Override
	public double getAttackBonus(Actor actor) {
		// the prayer level bonus
		final int level = actor.isPlayer() ? actor.toPlayer().getSkills().getLevel(MAGIC) : actor.toNPC().getCombatDefinitions().getMagicLevel();
		// the prayer bonus
		final double prayer = actor.isPlayer() ? actor.toPlayer().getPrayer().getMageMultiplier() : 1.0D;
		// the calculated boost
		double effective = Math.floor(level * prayer);
		// the bonus from your equipment
		int bonus = actor.isPlayer() ? actor.toPlayer().getCombatDefinitions().getBonus(MAGIC_ATTACK) : actor.toNPC().getBonus(MAGIC_ATTACK);
		double voidAccuracy = 1.0;
		if (actor.isPlayer() && CombatAlgorithm.fullVoidEquipped(actor.toPlayer(), 11663, 11674)) {
			voidAccuracy = 1.45;
		}
		return (int) Math.floor(((effective + 8) * (bonus + 64)) / 10) * voidAccuracy;
	}
	
	@Override
	public double getDefenceBonus(Actor actor, int weaponId, int attackStyle) {
		// the targets defence level
		int defenceLevel;
		// the targets magic level
		int magicLevel;
		// the targets prayer boost
		double prayer;
		// the targets magic defence bonus
		double bonus;
		if (actor.isPlayer()) {
			Player player = actor.toPlayer();
			defenceLevel = player.getSkills().getLevel(DEFENCE);
			magicLevel = player.getSkills().getLevel(MAGIC);
			prayer = actor.toPlayer().getPrayer().getMageMultiplier();
			bonus = player.getCombatDefinitions().getBonus(MAGIC_DEFENCE);
		} else {
			NPC npc = actor.toNPC();
			int combatLevel = npc.getCombatLevel();
			defenceLevel = combatLevel / 2;
			magicLevel = combatLevel / 2;
			prayer = 1.0;
			bonus = npc.getBonus(MAGIC_DEFENCE);
		}
		// the effective calculation
		double effective = Math.floor((defenceLevel * prayer) * 0.3) + (magicLevel * 0.7);
		// the equipment calculation [based on magic defence]
		int equipment = (int) (bonus + 5);
		return (int) Math.floor(((effective + 8) * (equipment + 64)) / 10);
	}
	
	@Override
	public int getMaximumHit(Actor actor, double multiplier) {
		return -1;
	}
}
