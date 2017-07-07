package org.redrune.game.content.combat.player.registry.spell;

import org.redrune.game.content.ProjectileManager;
import org.redrune.game.content.combat.player.registry.MagicSpellContext;
import org.redrune.game.content.combat.player.registry.MagicSpellEvent;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.constant.MagicConstants.MagicBook;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/29/2017
 */
public class IceBlitzEvent implements MagicSpellEvent<MagicSpellContext> {
	
	@Override
	public int spellId() {
		return 21;
	}
	
	@Override
	public int delay() {
		return 4;
	}
	
	@Override
	public int animationId() {
		return 1978;
	}
	
	@Override
	public int hitGfx() {
		return 367;
	}
	
	@Override
	public int maxHit() {
		return 260;
	}
	
	@Override
	public double exp() {
		return 46;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
	
	@Override
	public void cast(Player player, MagicSpellContext context) {
		final Entity target = context.getTarget();
		final boolean freezeDelayed = target.freezeDelayed();
		final boolean frozenTarget = target.isFrozen();
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(player, target, 368, 40, 36, 52, 15, 0));
		context.getSwing().sendSpell(player, context.getTarget(), this, () -> {
			if (frozenTarget || freezeDelayed) {
				return;
			}
			// only freeze the player if they are unfreezeable when the spell is cast.
			context.getTarget().freeze(player, 25, "You have been frozen!");
		}, null);
	}
}
