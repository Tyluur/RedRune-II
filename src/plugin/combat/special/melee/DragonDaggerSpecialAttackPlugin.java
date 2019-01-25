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
public class DragonDaggerSpecialAttackPlugin extends SpecialAttackPlugin {
	
	private static final Graphics GRAPHICS = new Graphics(252, 0, 100);
	
	private static final Animation ANIMATION = new Animation(1062);
	
	@Override
	public int[] getWeaponIds() {
		return arguments(1215, 5698);
	}
	
	@Override
	public void fire(Player source, Actor target, AbstractCombatStyle style) {
		source.setNextAnimation(ANIMATION);
		source.setNextGraphics(GRAPHICS);
		style.sendHit(source, target, style.getCalculator().getMaximumHit(source, 1.15), style.getRandomDamage(source, target, 1.15), 0);
		if (target.isPlayer()) {
			style.sendHit(source, target, style.getCalculator().getMaximumHit(source, 1.15), style.getRandomDamage(source, target, 1.15), 0);
		} else {
			style.sendHit(source, target, style.getCalculator().getMaximumHit(source, 1.15), style.getRandomDamage(source, target, 1.15), 1);
		}
		style.playSound(2537, source, target);
	}
}
