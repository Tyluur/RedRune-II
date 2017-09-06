package plugin.combat.range;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.content.combat.CombatAlgorithm;
import com.rs.game.content.combat.player.AbstractCombatStyle;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.combat.RangeWeaponPlugin;
import com.rs.game.world.projectile.Projectile;
import com.rs.game.world.projectile.ProjectileManager;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/6/2017
 */
public class DartKnifePlugin extends RangeWeaponPlugin {
	
	@Override
	public String[] getWeaponNames() {
		return arguments("* dart", "* knife");
	}
	
	@Override
	public void fire(Player source, Actor target, AbstractCombatStyle style) {
		int weaponId = source.getEquipment().getWeaponId();
		final String name = ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();
		// diff. projectile types
		if (name.contains("knife")) {
			int speed = 46 + (ProjectileManager.getLocation(source).getDistance(target) * 5);
			ProjectileManager.sendProjectile(new Projectile(source, target, CombatAlgorithm.getKnifeThrowGfxId(weaponId), 30, 26, 32, speed, 15, 1));
		} else if (name.contains("dart")) {
			ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(source, target, CombatAlgorithm.getKnifeThrowGfxId(weaponId), 40, 36, 32, 15, 0));
		}
		dropAmmo(source, target, 1);
		style.sendHit(source, target, style.getCalculator().getMaximumHit(source,1), style.getRandomDamage(source, target, 1), ProjectileManager.getProjectileDelay(source, target));
	}
}
