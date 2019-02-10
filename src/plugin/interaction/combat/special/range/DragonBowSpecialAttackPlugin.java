package plugin.interaction.combat.special.range;

import org.redrune.game.content.entity.actor.combat.CombatAlgorithm;
import org.redrune.game.content.entity.actor.combat.player.AbstractCombatStyle;
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
 * @since 9/6/2017
 */
public class DragonBowSpecialAttackPlugin extends SpecialAttackPlugin {
	
	@Override
	public int[] getWeaponIds() {
		return arguments(11235, 13405, 15701, 15702, 15703, 15704);
	}
	
	@Override
	public void fire(Player source, Actor target, AbstractCombatStyle style) {
		final int arrowId = source.getEquipment().getAmmoId();
		final int weaponId = source.getEquipment().getWeaponId();
		final int attackStyle = source.getCombatDefinitions().getAttackStyle();
		
		// animates
		source.setNextAnimation(new Animation(CombatAlgorithm.getWeaponAttackEmote(weaponId, attackStyle)));
		
		int delay = ProjectileManager.getProjectileDelay(source, target);
		int damage;
		int damage2;
		int maxHit;
		
		boolean dragon;
		
		if (arrowId == 11212) {
			maxHit = style.getCalculator().getMaximumHit(source, 1);
			damage = style.getRandomDamage(source, target, 1.5);
			damage2 = style.getRandomDamage(source, target, 1.5);
			if (damage < 80) {
				damage = 80;
			}
			if (damage2 < 80) {
				damage2 = 80;
			}
			visualize(source, target, dragon = true);
		} else {
			maxHit = style.getCalculator().getMaximumHit(source, 1);
			damage = style.getRandomDamage(source, target, 1.3);
			damage2 = style.getRandomDamage(source, target, 1.3);
			if (damage < 50) {
				damage = 50;
			}
			if (damage2 < 50) {
				damage2 = 50;
			}
			visualize(source, target, dragon = false);
		}
		final boolean dragonAmmo = dragon;
		
		style.sendHit(source, target, maxHit, damage, delay - 1).consume(detail -> detail.getHit().setLandTask(() -> {
			target.setNextGraphics(new Graphics(dragonAmmo ? 1100 : 1103, 0, 100));
		}));
		style.sendHit(source, target, maxHit, damage2, delay);
		
		// drops the ammo
		RangeWeaponPlugin.dropAmmo(source, target, 1);
		RangeWeaponPlugin.dropAmmo(source, target, 1);
	}
	
	/**
	 * Visualizes the dark bow projectiles
	 *
	 * @param player
	 * 		The player
	 * @param target
	 * 		The target
	 * @param dragon
	 * 		If we should use the dragon projectile
	 */
	public void visualize(Player player, Actor target, boolean dragon) {
		int projectileId = dragon ? 1099 : 1101;
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(player, target, projectileId, 40, 36, 46, 5, 0));
		int speed = ProjectileManager.getSpeedModifier(player, target);
		ProjectileManager.sendProjectile(new Projectile(player, target, projectileId, 40, 36, 51, speed + 10, 25, 0));
	}
}
