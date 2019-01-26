package plugin.combat.range;

import org.redrune.game.content.combat.player.AbstractCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.mask.Hit;
import org.redrune.game.entity.actor.mask.HitSplat;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.combat.RangeWeaponPlugin;
import org.redrune.game.entity.projectile.ProjectileManager;
import org.redrune.utility.functions.Misc;

import static org.redrune.utility.constants.SkillConstants.FIREMAKING;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/6/2017
 */
public class HandCannonPlugin extends RangeWeaponPlugin {
	
	private static final Animation ANIMATION = new Animation(12175);
	
	private static final Graphics EXPLODE_GRAPHICS = new Graphics(2140);
	
	private static final Graphics FIRE_GRAPHICS = new Graphics(2138);
	
	@Override
	public String[] getWeaponNames() {
		return arguments("hand cannon");
	}
	
	@Override
	public void fire(Player source, Actor target, AbstractCombatStyle style) {
		if (Misc.getRandom(source.getSkills().getLevel(FIREMAKING) << 1) == 0) {
			source.setNextGraphics(EXPLODE_GRAPHICS);
			source.getEquipment().getItems().set(3, null);
			source.getEquipment().refresh(3);
			source.getAppearance().generateAppearanceData();
			source.applyHit(new Hit(source, Misc.getRandom(150) + 10, HitSplat.REGULAR_DAMAGE));
		} else {
			source.setNextGraphics(FIRE_GRAPHICS);
			ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(source, target, 2143, 18, 36, 41, 5, 0));
			style.sendHit(source, target, style.getCalculator().getMaximumHit(source,1), style.getRandomDamage(source, target, 1), ProjectileManager.getProjectileDelay(source, target));
			dropAmmo(source, target, -2);
		}
	}
}
