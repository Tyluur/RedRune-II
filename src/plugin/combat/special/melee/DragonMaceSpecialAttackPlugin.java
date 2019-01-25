package plugin.combat.special.melee;

import org.redrune.game.content.combat.player.AbstractCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.plugin.combat.SpecialAttackPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/5/2017
 */
public class DragonMaceSpecialAttackPlugin extends SpecialAttackPlugin {
	
	private static final Graphics GRAPHICS = new Graphics(251, 0, 96);
	
	private static final Animation ANIMATION = new Animation(1060);
	
	@Override
	public int[] getWeaponIds() {
		return arguments(1434);
	}
	
	@Override
	public void fire(Player source, Actor target, AbstractCombatStyle style) {
		source.setNextAnimation(ANIMATION);
		source.setNextGraphics(GRAPHICS);
		style.sendHit(source, target, style.getCalculator().getMaximumHit(source, 1.5), style.getRandomDamage(source, target, 1.5), 0);
		style.playSound(2541, source, target);
	}
}
