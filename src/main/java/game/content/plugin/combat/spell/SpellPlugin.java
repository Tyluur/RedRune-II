package game.content.plugin.combat.spell;

import game.content.plugin.Plugin;
import game.entity.actor.Actor;
import game.entity.actor.player.Player;
import utility.constants.MagicConstants;
import utility.constants.SkillConstants;

/**
 * @author Tyluur <itstyluur@icloud.com>
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
