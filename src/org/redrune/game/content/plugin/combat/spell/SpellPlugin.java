package org.redrune.game.content.plugin.combat.spell;

import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.Plugin;
import org.redrune.utility.constants.MagicConstants;
import org.redrune.utility.constants.SkillConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/8/2017
 */
public abstract class SpellPlugin extends Plugin implements SkillConstants, MagicConstants {
	
	/**
	 * The id of the spell
	 */
	public abstract int spellId();
	
	/**
	 * The base experience of the spell
	 */
	public abstract double exp();
	
	/**
	 * The book the spell is on
	 */
	public abstract MagicBook book();
	
	/**
	 * Handles the casting of a spell
	 *
	 * @param player
	 * 		The player casting
	 * @param target
	 * 		The target of the spell, only set for combat spells
	 */
	public abstract void cast(Player player, Actor target);
	
}
