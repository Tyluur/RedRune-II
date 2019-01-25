package org.redrune.game.content.combat.player.calc;

import org.redrune.game.content.combat.player.AbstractCombatCalculator;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/8/2017
 */
public class MagicCombatCalculator extends AbstractCombatCalculator {
	
	@Override
	public double getAttackBonus(Player player) {
		// the prayer level bonus
		final int level = player.getSkills().getLevel(MAGIC);
		// the prayer bonus
		final double prayer = player.getPrayer().getMageMultiplier();
		// the calculated boost
		double effective = Math.floor(level * prayer);
		// the bonus from your equipment
		int bonus = player.getCombatDefinitions().getBonus(MAGIC_ATTACK);
		return (int) Math.floor(((effective + 8) * (bonus + 64)) / 10);
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
	public int getMaximumHit(Player player, double multiplier) {
		return -1;
	}
}
