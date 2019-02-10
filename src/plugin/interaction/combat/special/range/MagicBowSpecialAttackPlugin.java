package plugin.interaction.combat.special.range;

import org.redrune.game.content.entity.actor.combat.player.AbstractCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.combat.RangeWeaponPlugin;
import org.redrune.game.content.plugin.combat.SpecialAttackPlugin;
import org.redrune.game.entity.projectile.Projectile;
import org.redrune.game.entity.projectile.ProjectileManager;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/6/2017
 */
public class MagicBowSpecialAttackPlugin extends SpecialAttackPlugin {
	
	private static final Animation ANIMATION = new Animation(1074);
	
	@Override
	public int[] getWeaponIds() {
		return arguments(859, 861, 10284, 18332);
	}
	
	@Override
	public void fire(Player source, Actor target, AbstractCombatStyle style) {
		source.setNextAnimation(ANIMATION);
		
		style.sendHit(source, target, style.getCalculator().getMaximumHit(source, 1), style.getRandomDamage(source, target, 1), ProjectileManager.getProjectileDelay(source, target));
		style.sendHit(source, target, style.getCalculator().getMaximumHit(source, 1), style.getRandomDamage(source, target, 1), ProjectileManager.getProjectileDelay(source, target));
		
		visualize(source, target);
		
		RangeWeaponPlugin.dropAmmo(source, target, 1);
		RangeWeaponPlugin.dropAmmo(source, target, 1);
	}
	
	/**
	 * Visualizes the projectiles
	 *
	 * @param source
	 * 		The projectile from
	 * @param target
	 * 		The projectile to
	 */
	private void visualize(Player source, Actor target) {
		int speed = (int) (27.0D + source.getDistance(target) * 5.0D);
		ProjectileManager.sendProjectile(new Projectile(source, target, 249, 41, 36, 20, speed, 15, 0));
		speed = (int) (20.0D + source.getDistance(target) * 10.0D);
		ProjectileManager.sendProjectile(new Projectile(source, target, 249, 41, 36, 40, speed, 10, 0));
	}
}
