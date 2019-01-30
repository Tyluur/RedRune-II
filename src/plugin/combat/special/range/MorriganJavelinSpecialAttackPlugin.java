package plugin.combat.special.range;

import org.redrune.game.content.combat.player.AbstractCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.mask.Hit;
import org.redrune.game.entity.actor.mask.HitSplat;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.combat.RangeWeaponPlugin;
import org.redrune.game.content.plugin.combat.SpecialAttackPlugin;
import org.redrune.game.entity.projectile.Projectile;
import org.redrune.game.entity.projectile.ProjectileManager;
import org.redrune.engine.tick.task.WorldTask;
import org.redrune.engine.tick.task.WorldTasksManager;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/13/2017
 */
public class MorriganJavelinSpecialAttackPlugin extends SpecialAttackPlugin {
	
	private static final Animation ANIMATION = new Animation(10501);
	
	private static final Graphics GRAPHICS = new Graphics(1836);
	
	@Override
	public int[] getWeaponIds() {
		return arguments(13879, 13880, 13881, 13882);
	}
	
	@Override
	public void fire(Player source, Actor target, AbstractCombatStyle style) {
		source.setNextGraphics(GRAPHICS);
		source.setNextAnimation(ANIMATION);
		int randomDamage = style.getRandomDamage(source, target, 1);
		style.sendHit(source, target, style.getCalculator().getMaximumHit(source, 1), randomDamage, ProjectileManager.getProjectileDelay(source, target));
		ProjectileManager.sendProjectile(new Projectile(source, target, 1837, 41, 36, 20, (int) (27.0D + source.getDistance(target) * 5.0D), 0, 0));
		RangeWeaponPlugin.dropAmmo(source, target, -1);
		RangeWeaponPlugin.dropAmmo(source, target, -1);
		fireSpecialModifier(source, target, randomDamage);
	}
	
	private void fireSpecialModifier(Player source, Actor target, int randomDamage) {
		if (randomDamage > 0) {
			final Actor finalTarget = target;
			WorldTasksManager.schedule(new WorldTask() {
				int damage = randomDamage;
				
				@Override
				public void run() {
					if (finalTarget.isDead() || finalTarget.isFinished()) {
						stop();
						return;
					}
					if (damage > 50) {
						damage -= 50;
						
						if (!target.getTemporaryAttribute("teleporting", false)) {
							finalTarget.applyHit(new Hit(source, 50, HitSplat.REGULAR_DAMAGE));
						}
					} else {
						if (!target.getTemporaryAttribute("teleporting", false)) {
							finalTarget.applyHit(new Hit(source, damage, HitSplat.REGULAR_DAMAGE));
						}
						stop();
					}
				}
			}, 4, 2);
		}
	}
}
