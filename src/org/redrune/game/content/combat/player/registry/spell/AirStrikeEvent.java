package org.redrune.game.content.combat.player.registry.spell;

import org.redrune.game.content.ProjectileManager;
import org.redrune.game.content.combat.player.registry.MagicSpellContext;
import org.redrune.game.content.combat.player.registry.MagicSpellEvent;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.constant.MagicConstants.MagicBook;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/23/2017
 */
public class AirStrikeEvent implements MagicSpellEvent<MagicSpellContext> {
	
	@Override
	public int spellId() {
		return 25;
	}
	
	@Override
	public int delay() {
		return 5;
	}
	
	@Override
	public int animationId() {
		return 14221;
	}
	
	@Override
	public int hitGfx() {
		return 2700;
	}
	
	@Override
	public int maxHit() {
		return 20;
	}
	
	@Override
	public double exp() {
		return 5.5;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
	
	@Override
	public void cast(Player player, MagicSpellContext context) {
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(player, context.getTarget(), 2699, 30, 26, 52, 0, 0));
		context.getSwing().sendSpell(player, context.getTarget(), this);
	}
}
