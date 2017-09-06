package plugin.combat.special.melee;

import com.rs.game.content.combat.player.AbstractCombatStyle;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.combat.SpecialAttackPlugin;
import com.rs.utility.tools.RandomFunction;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/5/2017
 */
public class DragonClawSpecialAttackPlugin extends SpecialAttackPlugin {
	
	private static final Animation ANIMATION = new Animation(10961);
	
	private static final Graphics GRAPHICS = new Graphics(1950);
	
	@Override
	public int[] getWeaponIds() {
		return arguments(14484, 23695);
	}
	
	@Override
	public void fire(Player source, Actor target, AbstractCombatStyle style) {
		source.setNextAnimation(ANIMATION);
		source.setNextGraphics(GRAPHICS);
		int[] hits;
		int hit = style.getRandomDamage(source, target, 1);
		if (hit > 0) {
			hits = new int[] { hit, hit / 2, (hit / 2) / 2, (hit / 2) - ((hit / 2) / 2) };
		} else {
			hit = style.getRandomDamage(source, target, 1);
			if (hit > 0) {
				hits = new int[] { 0, hit, hit / 2, hit - (hit / 2) };
			} else {
				hit = style.getRandomDamage(source, target, 1);
				if (hit > 0) {
					hits = new int[] { 0, 0, hit / 2, (hit / 2) + 10 };
				} else {
					hit = style.getRandomDamage(source, target, 1);
					if (hit > 0) {
						hits = new int[] { 0, 0, 0, (int) (hit * 1.5) };
					} else {
						hits = new int[] { 0, 0, 0, RandomFunction.getRandom(7) };
					}
				}
			}
		}
		for (int i = 0; i < hits.length; i++) {
			if (i > 1) {
				style.sendHit(source, target, style.getCalculator().getMaximumHit(source, 1), hits[i], 1);
			} else {
				style.sendHit(source, target, style.getCalculator().getMaximumHit(source, 1), hits[i], 0);
			}
		}
	}
}
