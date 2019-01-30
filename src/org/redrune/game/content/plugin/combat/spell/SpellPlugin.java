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
public interface SpellPlugin extends Plugin, SkillConstants, MagicConstants {
	
	/**
	 * The id of the spell
	 */
	 int spellId();
	
	/**
	 * The base experience of the spell
	 */
	 double exp();
	
	/**
	 * The book the spell is on
	 */
	MagicBook book();
	
	/**
	 * Handles the casting of a spell
	 *
	 * @param player
	 * 		The player casting
	 * @param target
	 * 		The target of the spell, only set for combat spells
	 */
	 void cast(Player player, Actor target);
	
}
