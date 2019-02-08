package plugin.interaction.combat.special.melee;

import org.redrune.game.content.combat.player.AbstractCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.combat.SpecialAttackPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/5/2017
 */
public class BarrelchestAnchorSpecialAttackPlugin extends SpecialAttackPlugin {
	
	private static final Graphics GRAPHICS = new Graphics(1027);
	
	private static final Animation ANIMATION = new Animation(5870);
	
	@Override
	public int[] getWeaponIds() {
		return arguments(10887);
	}
	
	@Override
	public void fire(Player source, Actor target, AbstractCombatStyle style) {
		source.setNextAnimation(ANIMATION);
		source.setNextGraphics(GRAPHICS);
		style.sendHit(source, target, style.getCalculator().getMaximumHit(source,1.1), style.getRandomDamage(source, target, 1.1), 0);
	}
}
