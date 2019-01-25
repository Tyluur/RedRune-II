package org.redrune.game.plugin.combat.spell.type;

import org.redrune.game.content.combat.player.style.MagicCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.plugin.PluginRepository;
import org.redrune.game.plugin.combat.spell.SpellPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/8/2017
 */
public abstract class CombatSpellPlugin extends SpellPlugin {
	
	/**
	 * The delay on the spell, only used to find the next combat swing time
	 */
	public abstract int delay(Player player);
	
	/**
	 * The animation id for the spell
	 */
	public abstract int animationId();
	
	/**
	 * The id of the graphics applied when the hit lands
	 */
	public abstract int hitGfx();
	
	/**
	 * The base damage of the spell
	 *
	 * @param player
	 * 		The player casting
	 * @param target
	 * 		The target of the spell
	 */
	public abstract int maxHit(Player player, Actor target);
	
	/**
	 * Handles the casting of the spell
	 */
	public abstract void cast(Player source, Actor target, MagicCombatStyle style);
	
	@Override
	public final void cast(Player player, Actor target) {
		throw new RuntimeException("Unable to cast a spell without the style");
	}
	
	@Override
	public void register() {
		PluginRepository.register(this, book(), spellId());
	}
	
	/**
	 * The minimum damage the spell will do. If the spell splashes it must do atleast this damage.
	 *
	 * @param player
	 * 		The player
	 */
	public int minimumHit(Player player) {
		return -1;
	}
	
	/**
	 * The default height of the hit land gfx
	 */
	public int gfxHeight() {
		return 96;
	}
}
