package plugin.interaction.combat.range;

import org.redrune.game.content.entity.actor.combat.player.AbstractCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.combat.RangeWeaponPlugin;
import org.redrune.game.entity.projectile.ProjectileManager;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/6/2017
 */
public class CrystalBowPlugin extends RangeWeaponPlugin {
	
	@Override
	public String[] getWeaponNames() {
		return arguments("crystal bow");
	}
	
	@Override
	public void fire(Player source, Actor target, AbstractCombatStyle style) {
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(source, target, 249, 40, 30, 41, 15, 0));
		style.sendHit(source, target, style.getCalculator().getMaximumHit(source,1), style.getRandomDamage(source, target, 1), ProjectileManager.getProjectileDelay(source, target));
	}
}
