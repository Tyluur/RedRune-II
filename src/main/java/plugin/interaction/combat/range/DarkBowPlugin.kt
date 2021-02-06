package plugin.interaction.combat.range;

import org.redrune.game.content.entity.actor.combat.CombatAlgorithm;
import org.redrune.game.content.entity.actor.combat.player.AbstractCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.combat.RangeWeaponPlugin;
import org.redrune.game.entity.projectile.Projectile;
import org.redrune.game.entity.projectile.ProjectileManager;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/6/2017
 */
public class DarkBowPlugin extends RangeWeaponPlugin {
	
	@Override
	public String[] getWeaponNames() {
		return arguments("dark bow");
	}
	
	@Override
	public void fire(Player source, Actor target, AbstractCombatStyle style) {
		int weaponId = source.getEquipment().getWeaponId();
		int ammoId = source.getEquipment().getAmmoId();
		int speed = 46 + (source.getDistance(target) * 5);
		int speed2 = 55 + (source.getDistance(target) * 10);
		source.setNextGraphics(new Graphics(CombatAlgorithm.getArrowThrowGfxId(ammoId), 0, 100));
		
		for (int i = 1; i <= 2; i++) {
			style.sendHit(source, target, style.getCalculator().getMaximumHit(source,1), style.getRandomDamage(source, target, 1), ProjectileManager.getProjectileDelay(source, target));
			Projectile projectile = new Projectile(source, target, CombatAlgorithm.getArrowProjectileGfxId(weaponId, ammoId), 41, 35, 41, i == 1 ? speed : speed2, i == 1 ? 5 : 25, 0);
			ProjectileManager.sendProjectile(projectile);
			dropAmmo(source, target, 1);
		}
	}
}
