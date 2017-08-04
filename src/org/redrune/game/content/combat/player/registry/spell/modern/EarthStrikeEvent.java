package org.redrune.game.content.combat.player.registry.spell.modern;

import org.redrune.game.content.combat.player.registry.wrapper.context.CombatSpellContext;
import org.redrune.game.content.combat.player.registry.wrapper.magic.CombatSpellEvent;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.constant.MagicConstants.MagicBook;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/2/2017
 */
public class EarthStrikeEvent implements CombatSpellEvent {
	
	@Override
	public int delay() {
		return 0;
	}
	
	@Override
	public int animationId() {
		return 0;
	}
	
	@Override
	public int hitGfx() {
		return 0;
	}
	
	@Override
	public int maxHit() {
		return 0;
	}
	
	@Override
	public int spellId() {
		return 0;
	}
	
	@Override
	public double exp() {
		return 0;
	}
	
	@Override
	public MagicBook book() {
		return null;
	}
	
	@Override
	public void cast(Player player, CombatSpellContext context) {
	
	}
}
