package plugin.combat.special.range;

import org.redrune.game.content.combat.player.AbstractCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.combat.RangeWeaponPlugin;
import org.redrune.game.content.plugin.combat.SpecialAttackPlugin;
import org.redrune.game.entity.projectile.Projectile;
import org.redrune.game.entity.projectile.ProjectileManager;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/13/2017
 */
public class MorriganThrownAxeSpecialAttackPlugin extends SpecialAttackPlugin {
	
	private static final Animation ANIMATION = new Animation(10504);
	
	private static final Graphics GRAPHICS = new Graphics(1838);
	
	@Override
	public int[] getWeaponIds() {
		return arguments(13883, 13957);
	}
	
	@Override
	public void fire(Player source, Actor target, AbstractCombatStyle style) {
		source.setNextGraphics(GRAPHICS);
		source.setNextAnimation(ANIMATION);
		ProjectileManager.sendProjectile(new Projectile(source, target, 1839, 29, 15, 20, (int) (27.0D + source.getDistance(target) * 5.0D), 0, 0));
		int randomDamage = style.getRandomDamage(source, target, 1);
		style.sendHit(source, target, style.getCalculator().getMaximumHit(source, 1), randomDamage, ProjectileManager.getProjectileDelay(source, target));
		
		RangeWeaponPlugin.dropAmmo(source, target, -1);
	}
}
