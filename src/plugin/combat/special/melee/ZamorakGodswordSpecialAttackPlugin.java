package plugin.combat.special.melee;

import com.rs.game.content.combat.player.AbstractCombatStyle;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.combat.SpecialAttackPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/5/2017
 */
public class ZamorakGodswordSpecialAttackPlugin extends SpecialAttackPlugin {
	
	private static final Graphics GRAPHICS = new Graphics(1221);
	
	private static final Animation ANIMATION = new Animation(7070);
	
	@Override
	public int[] getWeaponIds() {
		return arguments(11700);
	}
	
	@Override
	public void fire(Player source, Actor target, AbstractCombatStyle style) {
		int damage = style.getRandomDamage(source, target, 1);
		source.setNextAnimation(ANIMATION);
		source.setNextGraphics(GRAPHICS);
		if (damage != 0 && target.getSize() <= 1) {
			target.setNextGraphics(new Graphics(2104));
			target.addFreezeDelay(18000, false, source); // 18seconds
		}
		style.sendHit(source, target, style.getCalculator().getMaximumHit(source,1), damage, 0);
	}
}
