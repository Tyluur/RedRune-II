package org.redrune.game.content.combat.player.registry.wrapper.magic;

import org.redrune.game.content.combat.player.registry.wrapper.context.CombatSpellContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/23/2017
 */
public interface CombatSpellEvent extends MagicSpellEvent<CombatSpellContext> {
	
	/**
	 * The delay on the spell, only used to find the next combat swing time
	 */
	int delay();
	
	/**
	 * The animation id for the spell
	 */
	int animationId();
	
	/**
	 * The id of the graphics applied when the hit lands
	 */
	int hitGfx();
	
	/**
	 * The base damage of the spell
	 */
	int maxHit();
	
	/**
	 * The default height of the hit land gfx
	 */
	default int gfxHeight() {
		return 96;
	}
	
}
