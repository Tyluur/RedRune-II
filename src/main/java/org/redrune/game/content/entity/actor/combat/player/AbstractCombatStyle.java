package org.redrune.game.content.entity.actor.combat.player;

import org.redrune.game.content.entity.actor.combat.CombatSwingDetail;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Hit;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.map.region.Region;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.utility.constants.SkillConstants;

/**
 * @author Tyluur <itstyluur@icloud.com>
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
	 *
	 * @param source
	 * 		The source
	 */
	public abstract int getRandomDamage(Actor source, Actor target, double multiplier);
	
	/**
	 * Sends the hit to the target
	 */
	public abstract CombatSwingDetail sendHit(Player source, Actor target, int maxHit, int damage, int delay);
	
	/**
	 * The combat style calculator
	 */
	protected final AbstractCombatCalculator calculator;
	
	/**
	 * Constructs a new combat style enumeration instance
	 */
	protected AbstractCombatStyle(AbstractCombatCalculator calculator) {
		this.calculator = calculator;
	}
	
	/**
	 * Plays a sound to a single player
	 */
	public void playSingleSound(Player player, int soundId) {
		if (soundId == -1) {
			return;
		}
		player.getPackets().sendSound(soundId, 0, 1);
	}
	
	/**
	 * Plays a sound to all players in the source's area, within a 5 tile radius
	 */
	public void playAreaSound(Player player, int soundId) {
		if (soundId == -1) {
			return;
		}
		Region region = RegionManager.getRegion(player.getRegionId());
		if (region == null) {
			return;
		}
		for (Player p : region.getPlayersWithinDistance(player, 5)) {
			if (p == null) {
				continue;
			}
			p.getPackets().sendSound(soundId, 0, 1);
		}
	}

	/**
	 * Handles effects like protection prayers, soulsplit, spirit shields etc.
	 */

	public abstract void handleEffects(Player source, Actor target, Hit hit);

    public AbstractCombatCalculator getCalculator() {
        return this.calculator;
    }
}
