package org.redrune.game.content.combat.player.registry.spell;

import org.redrune.game.content.combat.player.registry.MagicSpellContext;
import org.redrune.game.content.combat.player.registry.MagicSpellEvent;
import org.redrune.game.node.entity.Entity;
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
	public int animationId() {
		return 1979;
	}
	
	// we don't store a static gfx because it is modifiable
	@Override
	public int hitGfx() {
		return -1;
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
		// storing vars before spell is cast
		final Entity target = context.getTarget();
		final boolean freezeDelayed = target.freezeDelayed();
		final boolean frozenTarget = target.isFrozen();
		context.getSwing().sendSpell(player, context.getTarget(), this, () -> {
			if (frozenTarget || freezeDelayed) {
				return;
			}
			// only freeze the player if they are unfreezeable when the spell is cast.
			context.getTarget().freeze(player, 30, "You have been frozen!");
		}, () -> {
			int gfx;
			int height;
			if (target.getSize() >= 2 || freezeDelayed || frozenTarget) {
				gfx = 1677;
				height = 100;
			} else {
				gfx = 369;
				height = 0;
			}
			target.sendGraphics(gfx, height, 0);
		});
		
	}
}
