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
public class GraniteMaulSpecialAttackPlugin extends SpecialAttackPlugin {
	
	private static final Animation ANIMATION = new Animation(1667);
	
	private static final Graphics GRAPHICS = new Graphics(340, 0, 96 << 16);
	
	@Override
	public int[] getWeaponIds() {
		return arguments(4153);
	}
	
	@Override
	public void fire(Player source, Actor target, AbstractCombatStyle style) {
		source.setNextAnimation(ANIMATION);
		source.setNextGraphics(GRAPHICS);
		style.sendHit(source, target, style.getCalculator().getMaximumHit(source, 1), style.getRandomDamage(source, target, 1), 0);
	}
	
	@Override
	public boolean isInstant() {
		return true;
	}
}
