package plugin.combat.magic.modern.surge;

import org.redrune.game.content.combat.player.style.MagicCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.plugin.combat.spell.type.CombatSpellPlugin;
import org.redrune.game.entity.projectile.ProjectileManager;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/7/2017
 */
public class FireSurgeSpellPlugin extends CombatSpellPlugin {
	
	private static final Graphics GRAPHICS = new Graphics(2728);
	
	@Override
	public int delay(Player player) {
		return 5;
	}
	
	@Override
	public int animationId() {
		return 2791;
	}
	
	@Override
	public int hitGfx() {
		return 2741;
	}
	
	@Override
	public int maxHit(Player player, Actor target) {
		return 280;
	}
	
	@Override
	public void cast(Player source, Actor target, MagicCombatStyle style) {
		source.setNextGraphics(GRAPHICS);
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(source, target, 2735, 30, 26, 52, 0, 0));
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(source, target, 2736, 30, 26, 52, 0, 0));
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(source, target, 2736, 30, 26, 52, 110, 0));
		style.sendSpell(source, target, this);
	}
	
	@Override
	public int spellId() {
		return 91;
	}
	
	@Override
	public double exp() {
		return 80;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
	
}
