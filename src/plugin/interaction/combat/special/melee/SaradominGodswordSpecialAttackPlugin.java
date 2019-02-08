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
public class SaradominGodswordSpecialAttackPlugin extends SpecialAttackPlugin {
	
	private static final Animation ANIMATION = new Animation(12019);
	
	private static final Graphics GRAPHICS = new Graphics(2109);
	
	@Override
	public int[] getWeaponIds() {
		return arguments(11698, 23681);
	}
	
	@Override
	public void fire(Player source, Actor target, AbstractCombatStyle style) {
		int damage = style.getRandomDamage(source, target, 1.1);
		source.setNextAnimation(ANIMATION);
		source.setNextGraphics(GRAPHICS);
		source.heal(damage / 2);
		source.getPrayer().restorePrayer((damage / 4) * 10);
		style.sendHit(source, target, style.getCalculator().getMaximumHit(source, 1.1), damage, 0);
	}
}
