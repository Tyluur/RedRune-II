package org.redrune.game.content.action.combat.player.registry.spell;

import org.redrune.game.content.action.combat.player.registry.MagicSpellContext;
import org.redrune.game.content.action.combat.player.registry.MagicSpellEvent;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.constant.MagicConstants.MagicBook;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/28/2017
 */
public class IceBarrageEvent implements MagicSpellEvent<MagicSpellContext> {
	
	@Override
	public int spellId() {
		return 23;
	}
	
	@Override
	public int delay() {
		return 4;
	}
	
	@Override
	public int hitGfx() {
		return 369;
	}
	
	@Override
	public int maxHit() {
		return 300;
	}
	
	@Override
	public double exp() {
		return 52;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
	
	@Override
	public void cast(Player player, MagicSpellContext context) {
		player.sendAnimation(1979);
		context.getSwing().sendSpell(player, context.getTarget(), this, () -> {
			context.getTarget().freeze(player, 4, "You have been frozen!");
		}, null);
		
	}
}
