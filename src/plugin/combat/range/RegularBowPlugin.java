package plugin.combat.range;

import com.rs.game.content.combat.CombatAlgorithm;
import com.rs.game.content.combat.player.AbstractCombatStyle;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.combat.RangeWeaponPlugin;
import com.rs.game.world.projectile.ProjectileManager;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/6/2017
 */
public class RegularBowPlugin extends RangeWeaponPlugin {
	
	@Override
	public String[] getWeaponNames() {
		return arguments("shortbow", "* shortbow", "* longbow");
	}
	
	@Override
	public void fire(Player source, Actor target, AbstractCombatStyle style) {
		int weaponId = source.getEquipment().getWeaponId();
		int ammoId = source.getEquipment().getAmmoId();
		
		source.setNextGraphics(new Graphics(CombatAlgorithm.getArrowThrowGfxId(ammoId), 0, 100));
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(source, target, CombatAlgorithm.getArrowProjectileGfxId(weaponId, ammoId), 40, 30, 41, 15, 0));
		style.sendHit(source, target, style.getCalculator().getMaximumHit(source,1), style.getRandomDamage(source, target, 1), ProjectileManager.getProjectileDelay(source, target));
		dropAmmo(source, target, 1);
	}
	
}
