package plugin.interaction.combat.special.melee;

import org.redrune.game.content.entity.actor.combat.player.AbstractCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.combat.SpecialAttackPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/5/2017
 */
public class DragonHalberdSpecialPlugin extends SpecialAttackPlugin {
	
	private static final Graphics GRAPHICS = new Graphics(282);
	
	private static final Graphics GRAPHICS1 = new Graphics(254, 0, 100);
	
	private static final Graphics GRAPHICS2 = new Graphics(80);
	
	@Override
	public int[] getWeaponIds() {
		return arguments(3204);
	}
	
	@Override
	public void fire(Player source, Actor target, AbstractCombatStyle style) {
		source.setNextAnimation(new Animation(1665));
		source.setNextGraphics(GRAPHICS);
		if (target.getSize() < 3) {
			target.setNextGraphics(GRAPHICS1);
			target.setNextGraphics(GRAPHICS2);
		}
		style.sendHit(source, target, style.getCalculator().getMaximumHit(source, 1.1), style.getRandomDamage(source, target, 1.1), 0);
		if (target.getSize() > 1) {
			style.sendHit(source, target, style.getCalculator().getMaximumHit(source, 1.1), style.getRandomDamage(source, target, 1.1), 1);
		}
	}
}
