package plugin.combat.special.range;

import com.rs.game.content.combat.player.AbstractCombatStyle;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.entity.actor.mask.Hit;
import com.rs.game.entity.actor.mask.HitSplat;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.combat.RangeWeaponPlugin;
import com.rs.game.plugin.combat.SpecialAttackPlugin;
import com.rs.game.world.projectile.Projectile;
import com.rs.game.world.projectile.ProjectileManager;
import com.rs.game.world.task.WorldTask;
import com.rs.game.world.task.WorldTasksManager;

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
					if (finalTarget.isDead() || finalTarget.hasFinished()) {
						stop();
						return;
					}
					if (damage > 50) {
						damage -= 50;
						
						if (!target.getAttribute("teleporting", false)) {
							finalTarget.applyHit(new Hit(source, 50, HitSplat.REGULAR_DAMAGE));
						}
					} else {
						if (!target.getAttribute("teleporting", false)) {
							finalTarget.applyHit(new Hit(source, damage, HitSplat.REGULAR_DAMAGE));
						}
						stop();
					}
				}
			}, 4, 2);
		}
	}
}
