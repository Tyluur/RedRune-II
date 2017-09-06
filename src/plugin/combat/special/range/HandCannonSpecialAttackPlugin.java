package plugin.combat.special.range;

import com.rs.game.content.combat.player.AbstractCombatStyle;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.combat.SpecialAttackPlugin;
import com.rs.game.world.projectile.ProjectileManager;
import com.rs.game.world.task.WorldTask;
import com.rs.game.world.task.WorldTasksManager;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/6/2017
 */
public class HandCannonSpecialAttackPlugin extends SpecialAttackPlugin {
	
	private static final Graphics GRAPHICS = new Graphics(2138);
	
	private static final Animation FIRE_ANIMATION = new Animation(12174);
	
	@Override
	public int[] getWeaponIds() {
		return arguments(15241);
	}
	
	@Override
	public void fire(Player source, Actor target, AbstractCombatStyle style) {
		source.setNextAnimation(new Animation(12175));
		WorldTasksManager.schedule(new WorldTask() {
			int loop = 0;
			
			@Override
			public void run() {
				if ((target.isDead() || source.isDead() || loop > 1)) {
					stop();
					return;
				}
				if (loop == 0) {
					source.setNextAnimation(FIRE_ANIMATION);
					source.setNextGraphics(GRAPHICS);
					ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(source, target, 2143, 18, 36, 41, 5, 0));
					style.sendHit(source, target, style.getCalculator().getMaximumHit(source,1), style.getRandomDamage(source, target, 1), ProjectileManager.getProjectileDelay(source, target));
				} else if (loop == 1) {
					source.setNextAnimation(FIRE_ANIMATION);
					source.setNextGraphics(GRAPHICS);
					ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(source, target, 2143, 18, 36, 41, 5, 0));
					style.sendHit(source, target, style.getCalculator().getMaximumHit(source,1), style.getRandomDamage(source, target, 1), ProjectileManager.getProjectileDelay(source, target));
					stop();
				}
				loop++;
			}
		}, 0, (int) 0.25);
	}
}
