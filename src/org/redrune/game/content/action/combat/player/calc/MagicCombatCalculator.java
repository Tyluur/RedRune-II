package org.redrune.game.content.action.combat.player.calc;

import org.redrune.game.content.action.combat.player.CombatTypeCalculator;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.constant.SkillConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/23/2017
 */
public class MagicCombatCalculator implements CombatTypeCalculator {
	
	@Override
	public double totalAggressiveBoost(Player player, Object... params) {
		// the prayer level bonus
		final int level = player.getSkills().getLevel(SkillConstants.MAGIC);
		// the prayer bonus
		final double prayer = player.getManager().getPrayers().getBasePrayerBoost(SkillConstants.MAGIC);
		// the calculated boost
		double effective = Math.floor(level * prayer);
		// the bonus from your equipment
		int bonus = player.getEquipment().getBonus(MAGIC_ATTACK);
		return (int) Math.floor(((effective + 8) * (bonus + 64)) / 10);
	}
	
	@Override
	public double totalDefensiveBoost(Entity entity, Object... params) {
		if (entity.isPlayer()) {
			Player p2 = entity.toPlayer();
			// the targets defence level
			int level = p2.getSkills().getLevel(SkillConstants.DEFENCE);
			// the targets prayer boost
			double prayer = entity.toPlayer().getManager().getPrayers().getBasePrayerBoost(SkillConstants.MAGIC);
			// the effective calculation
			double effective = Math.floor((level * prayer) * 0.3) + (p2.getSkills().getLevel(SkillConstants.MAGIC) * 0.7);
			// the equipment calculation [based on magic defence]
			int equipment = p2.getEquipment().getBonus(MAGIC_DEFENCE) + 5;
			return (int) Math.floor(((effective + 8) * (equipment + 64)) / 10);
		} else {
			// TODO npc defense bonus
			return 0;
		}
	}
	
	/**
	 * The magic max damage is set by the spell
	 *
	 * @param player
	 * 		The player
	 * @param params
	 * 		The parameters
	 * @return -1
	 */
	@Override
	public double maximumDamageAppendable(Player player, Object... params) {
		return -1;
	}
}
