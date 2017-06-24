package org.redrune.game.content.action.combat.player.registry;

import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.constant.MagicConstants.MagicBook;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/23/2017
 */
public interface MagicSpellEvent<T extends MagicSpellContext> extends CombatRegistryEvent {
	
	/**
	 * The id of the spell
	 */
	int spellId();
	
	/**
	 * The delay on the spell
	 */
	int delay();
	
	/**
	 * The id of the graphics applied when the hit lands
	 */
	int hitGfx();
	
	/**
	 * The base damage of the spell
	 */
	int maxHit();
	
	/**
	 * The base experience of the spell
	 */
	double exp();
	
	/**
	 * The book the spell is on
	 */
	MagicBook book();
	
	/**
	 * Casts the magic spell
	 *
	 * @param player
	 * 		The player
	 * @param context
	 * 		The context
	 */
	void cast(Player player, T context);
	
	/**
	 * The default height of the hit land gfx
	 */
	default int gfxHeight() {
		return 96;
	}
	
}
