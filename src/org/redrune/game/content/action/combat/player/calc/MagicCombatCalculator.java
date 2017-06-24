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
		/*double attack = player.getSkills().getLevel(SkillConstants.MAGIC) + player.getEquipment().getBonus(MAGIC_ATTACK);
		//attack *= player.getPrayer().getMageMultiplier(); TODO prayer multiplier
		if (StaticCombatFormulae.fullVoidEquipped(player, 11663, 11674)) {
			attack *= 1.3;
		}*/
		
		int level = player.getSkills().getLevel(SkillConstants.MAGIC);
		double prayer = 1.0;
		/*if (entity instanceof Player) {
		// TODO:	prayer += ((Player) entity).getPrayer().getSkillBonus(Skills.MAGIC);
		}*/
		double additional = 1.0; // Slayer helmet/salve/...
		double effective = Math.floor(((level * prayer) * additional));
		int bonus = player.getEquipment().getBonus(MAGIC_ATTACK);
		return (int) Math.floor(((effective + 8) * (bonus + 64)) / 10);
	}
	
	@Override
	public double totalDefensiveBoost(Entity entity, Object... params) {
		if (entity.isPlayer()) {
			Player p2 = entity.toPlayer();
			int level = p2.getSkills().getLevel(SkillConstants.DEFENCE);
			double prayer = 1.0;
			/*if (entity instanceof Player) {
				//TODO: prayer += ((Player) entity).getPrayer().getSkillBonus(Skills.MAGIC);
			}*/
			double effective = Math.floor((level * prayer) * 0.3) + (p2.getSkills().getLevel(SkillConstants.MAGIC) * 0.7);
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
	
	/*
	
		
		if (target instanceof Player) { //old player magic formula
			double att = player.getSkills().getLevel(Skills.MAGIC) + player.getCombatDefinitions().getBonuses()[CombatDefinitions.MAGIC_ATTACK];
			att *= player.getPrayer().getMageMultiplier();
			if (CombatFormulae.fullVoidEquipped(player, 11663, 11674)) {
				att *= 1.3;
			}
			double def;
			Player p2 = (Player) target;
			def = p2.getSkills().getLevel(Skills.DEFENCE) + (p2.getCombatDefinitions().getBonuses()[CombatDefinitions.MAGIC_DEF]);
			def *= p2.getPrayer().getDefenceMultiplier();
			double prob = att / def;
			if (prob > 0.90) // max, 90% prob hit so even lvl 138 can miss at lvl 3
			{
				prob = 0.90;
			} else if (prob < 0.05) // minimun 5% so even lvl 3 can hit lvl 138
			{
				prob = 0.05;
			}
			System.out.println("--------- " + player.getDisplayName() + " MAGIC DEBUG ---------");
			System.out.println("[att=" + att + ", def=" + def + ", prob=" + prob + "]");
			if (!Combat.rollHit(att, def)) {
				return 0;
			}
		} else {
			double att = (player.getSkills().getLevel(Skills.MAGIC) / 2) + player.getCombatDefinitions().getBonuses()[CombatDefinitions.MAGIC_ATTACK];
			att *= player.getPrayer().getMageMultiplier();
			if (CombatFormulae.fullVoidEquipped(player, 11663, 11674)) {
				att *= 1.3;
			}
			double def;
			if (target instanceof Player) {
				Player p2 = (Player) target;
				def = (p2.getSkills().getLevel(Skills.DEFENCE) / 2) + p2.getCombatDefinitions().getBonuses()[CombatDefinitions.MAGIC_DEF];
				def *= p2.getPrayer().getDefenceMultiplier();
			} else {
				NPC n = (NPC) target;
				def = n.getBonuses() == null ? 0 : n.getBonuses()[CombatDefinitions.MAGIC_DEF];
			}
			if (!Combat.rollHit(att, def)) {
				return 0;
			}
		}
		max_hit = baseDamage;
		double boost = 1 + ((player.getSkills().getLevel(Skills.MAGIC) - player.getSkills().getLevelForXp(Skills.MAGIC)) * 0.03);
		if (boost > 1) { max_hit *= boost; }
		double magicPerc = player.getCombatDefinitions().getBonuses()[CombatDefinitions.MAGIC_DAMAGE];
		if (spellcasterGloves > 0) {
			if (baseDamage > 60 || spellcasterGloves == 28 || spellcasterGloves == 25) {
				magicPerc += 17;
				if (target instanceof Player) {
					Player p = (Player) target;
					p.getSkills().drainLevel(0, p.getSkills().getLevel(0) / 10);
					p.getSkills().drainLevel(1, p.getSkills().getLevel(1) / 10);
					p.getSkills().drainLevel(2, p.getSkills().getLevel(2) / 10);
					p.getPackets().sendGameMessage("Your melee skills have been drained.");
					player.getPackets().sendGameMessage("Your spell weakened your enemy.");
				}
				player.getPackets().sendGameMessage("Your magic surged with extra power.");
			}
		}
		boost = magicPerc / 100 + 1;
		max_hit *= boost;
		return (int) Math.floor(max_hit);
	 */
}
