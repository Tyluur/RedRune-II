package org.redrune.game.node.entity.player.data;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.rs666.packet.outgoing.impl.ConfigPacketBuilder;
import org.redrune.network.rs666.packet.outgoing.impl.SkillPacketBuilder;
import org.redrune.utility.rs.constant.SkillConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public class PlayerSkills implements SkillConstants {
	
	/**
	 * The array of levels the player has in the skills
	 */
	private final short[] level = new short[25];
	
	/**
	 * The array of experience the player has in the skills
	 */
	private final double[] experience = new double[25];
	
	/**
	 * The amount of experience we have obtained
	 */
	@Getter
	@Setter
	private double counterExperience;
	
	/**
	 * The player who owns this class
	 */
	@Setter
	private transient Player player;
	
	/**
	 * Constructs a new {@code PlayerSkills} {@code Object}
	 */
	public PlayerSkills() {
		for (int i = 0; i < level.length; i++) {
			level[i] = 1;
		}
		level[HITPOINTS] = 10;
		experience[HITPOINTS] = getXPForLevel(10);
		level[HERBLORE] = 3;
		experience[HERBLORE] = getXPForLevel(3);
	}
	
	/**
	 * Gets the required experience for a level
	 *
	 * @param level
	 * 		The level
	 */
	public static int getXPForLevel(int level) {
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			if (lvl >= level) {
				return output;
			}
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}
	
	/**
	 * Gets the level in a skill by the desired amount of experience
	 *
	 * @param experience
	 * 		The experience
	 * @param skill
	 * 		The skill
	 */
	public static int getLevelByExperience(double experience, int skill) {
		int points = 0;
		int output;
		for (int lvl = 1; lvl <= (skill == DUNGEONEERING ? 120 : 99); lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if ((output - 1) >= experience) {
				return lvl;
			}
		}
		return skill == DUNGEONEERING ? 120 : 99;
	}
	
	/**
	 * Refreshes all skill components
	 */
	public void refreshAll() {
		for (int skill = 0; skill < level.length; skill++) {
			updateSkill(skill);
		}
		updateExperienceCounter();
	}
	
	/**
	 * Updates the skill details in the client for the parameterized skill
	 *
	 * @param skill
	 * 		The skill
	 */
	private void updateSkill(int skill) {
		player.getTransmitter().send(new SkillPacketBuilder(skill).build(player));
	}
	
	/**
	 * Updates the experience counter with the amount of experience we've obtained
	 */
	private void updateExperienceCounter() {
		player.getTransmitter().send(new ConfigPacketBuilder(1801, (int) (counterExperience * 10D)).build(player));
	}
	
	/**
	 * Gets the combat level in a skill
	 */
	public int getCombatLevel() {
		int attack = getLevelForXp(0);
		int defence = getLevelForXp(1);
		int strength = getLevelForXp(2);
		int hp = getLevelForXp(3);
		int prayer = getLevelForXp(5);
		int ranged = getLevelForXp(4);
		int magic = getLevelForXp(6);
		double base = 0.25 * (defence + hp + Math.floor(prayer / 2));
		double meleeC = 0.325 * (attack + strength);
		double rangeC = 0.325 * (Math.floor(ranged / 2) + ranged);
		double mageC = 0.325 * (Math.floor(magic / 2) + magic);
		return (int) Math.floor(base + Math.max(meleeC, Math.max(rangeC, mageC)));
	}
	
	/**
	 * Gets the level the player has in a skill, by the amount of experience the player has. This is used in cases where
	 * the level has reduced due to draining or other modifications, and the original level is still important.
	 *
	 * @param skill
	 * 		The skill
	 */
	public int getLevelForXp(int skill) {
		double exp = experience[skill];
		int points = 0;
		int output;
		for (int lvl = 1; lvl <= (skill == DUNGEONEERING ? 120 : 99); lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if ((output - 1) >= exp) {
				return lvl;
			}
		}
		return skill == DUNGEONEERING ? 120 : 99;
	}
	
	/**
	 * Gets the summoning additive combat level
	 */
	public int getSummoningCombatLevel() {
		return getLevelForXp(SUMMONING) / 8;
	}
	
	/**
	 * Gets the level in a certain skillId
	 *
	 * @param skillId
	 * 		The id of the skillId
	 */
	public int getLevel(int skillId) {
		return level[skillId];
	}
	
	/**
	 * Gets the amount of experience in a skillId
	 *
	 * @param skillId
	 * 		The id of the skillId
	 */
	public double getExperience(int skillId) {
		return experience[skillId];
	}
	
	/**
	 * Sets the experience in a skillId
	 *
	 * @param skillId
	 * 		The id of the skillId
	 * @param newExp
	 * 		The new experience amount to set
	 */
	public void setXp(int skillId, double newExp) {
		experience[skillId] = newExp;
		updateSkill(skillId);
	}
	
	/**
	 * Sets the level of a skillId
	 *
	 * @param skillId
	 * 		The id of the skillId
	 * @param newLevel
	 * 		The new level of the skillId to set
	 */
	public void setLevel(int skillId, int newLevel) {
		level[skillId] = (short) newLevel;
		updateSkill(skillId);
	}
	
}
