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
public class AbyssalWhipSpecialAttackPlugin extends SpecialAttackPlugin {
	
	private static final Animation ANIMATION = new Animation(11971);
	
	private static final Graphics GRAPHICS = new Graphics(2108, 0, 96);
	
	@Override
	public int[] getWeaponIds() {
		return arguments(4151, 15442, 15443, 15444, 15441, 23691);
	}
	
	@Override
	public void fire(Player source, Actor target, AbstractCombatStyle style) {
		source.setNextAnimation(ANIMATION);
		target.setNextGraphics(GRAPHICS);
		if (target instanceof Player) {
			Player p2 = (Player) target;
			p2.setRunEnergy(p2.getRunEnergy() > 25 ? p2.getRunEnergy() - 25 : 0);
		}
		style.sendHit(source, target, style.getCalculator().getMaximumHit(source,1), style.getRandomDamage(source, target, 1), 0);
	}
}
