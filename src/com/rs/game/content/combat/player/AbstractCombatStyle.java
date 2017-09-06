package com.rs.game.content.combat.player;

import com.rs.game.content.combat.CombatSwingDetail;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.mask.Hit;
import com.rs.game.entity.actor.player.Player;
import com.rs.utility.constants.SkillConstants;
import lombok.Getter;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/4/2017
 */
public abstract class AbstractCombatStyle implements SkillConstants {
	
	/**
	 * This method handles the swing of a combat style
	 */
	public abstract boolean fireSwing(Player source, Actor target);
	
	/**
	 * Handles the addition of experience
	 */
	public abstract void addExperience(Player source, Actor target, Hit hit, int attackStyle, int weaponId);
	
	/**
	 * Gets the random damage, based on the calculated maximum values
	 *  @param source
	 * 		The source
	 * @param target
	 * @param multiplier
	 */
	public abstract int getRandomDamage(Player source, Actor target, double multiplier);
	
	/**
	 * Sends the hit to the target
	 */
	public abstract CombatSwingDetail sendHit(Player source, Actor target, int maxHit, int damage, int delay);
	
	/**
	 * The combat style calculator
	 */
	@Getter
	protected final AbstractCombatCalculator calculator;
	
	/**
	 * Constructs a new combat style enumeration instance
	 */
	protected AbstractCombatStyle(AbstractCombatCalculator calculator) {
		this.calculator = calculator;
	}
	
	/**
	 * Plays a sound to the player and the target
	 */
	public void playSound(int soundId, Player player, Actor target) {
		if (soundId == -1) {
			return;
		}
		player.getPackets().sendSound(soundId, 0, 1);
		if (target.isPlayer()) {
			Player toPlayer = target.toPlayer();
			toPlayer.getPackets().sendSound(soundId, 0, 1);
		}
	}
}
