package plugin.interaction.combat.range;

import org.redrune.cache.loaders.ItemDefinitions;
import org.redrune.game.content.combat.CombatAlgorithm;
import org.redrune.game.content.combat.player.AbstractCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.combat.RangeWeaponPlugin;
import org.redrune.game.entity.projectile.Projectile;
import org.redrune.game.entity.projectile.ProjectileManager;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/6/2017
 */
public class ThrownAmmoPlugin extends RangeWeaponPlugin {
	
	@Override
	public String[] getWeaponNames() {
		return arguments("toktz-xil-ul", "* throwing axe", "* thrownaxe", "* javelin", "* dart", "* knife");
	}
	
	@Override
	public void fire(Player source, Actor target, AbstractCombatStyle style) {
		int weaponId = source.getEquipment().getWeaponId();
		final String name = ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();
		if (!name.contains("javelin") && !name.contains("throwing axe") && !name.contains("thrownaxe") && !name.contains("toktz")) {
			source.setNextGraphics(new Graphics(CombatAlgorithm.getKnifeThrowGfxId(weaponId), 0, 96));
		}
		// diff. projectile types
		if (name.contains("knife")) {
			int speed = 46 + (ProjectileManager.getLocation(source).getDistance(target) * 5);
			ProjectileManager.sendProjectile(new Projectile(source, target, CombatAlgorithm.getKnifeThrowGfxId(weaponId), 30, 26, 32, speed, 15, 1));
		} else if (name.contains("dart") || name.contains("toktz")) {
			ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(source, target, CombatAlgorithm.getKnifeThrowGfxId(weaponId), 40, 36, 32, 15, 0));
		} else if (name.contains("throwing axe")) {
			ProjectileManager.sendProjectile(new Projectile(source, target, 1839, 29, 15, 20, (int) (27.0D + source.getDistance(target) * 5.0D), 0, 0));
		}
		style.sendHit(source, target, style.getCalculator().getMaximumHit(source,1), style.getRandomDamage(source, target, 1), ProjectileManager.getProjectileDelay(source, target));
		dropAmmo(source, target, -1);
	}
}
