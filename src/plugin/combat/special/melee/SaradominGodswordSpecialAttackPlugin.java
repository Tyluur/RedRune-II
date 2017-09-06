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
public class SaradominGodswordSpecialAttackPlugin extends SpecialAttackPlugin {
	
	private static final Animation ANIMATION = new Animation(12019);
	
	private static final Graphics GRAPHICS = new Graphics(2109);
	
	@Override
	public int[] getWeaponIds() {
		return arguments(11698, 23681);
	}
	
	@Override
	public void fire(Player source, Actor target, AbstractCombatStyle style) {
		int damage = style.getRandomDamage(source, target, 1);
		source.setNextAnimation(ANIMATION);
		source.setNextGraphics(GRAPHICS);
		source.heal(damage / 2);
		source.getPrayer().restorePrayer((damage / 4) * 10);
		style.sendHit(source, target, style.getCalculator().getMaximumHit(source,1), damage, 0);
	}
}
