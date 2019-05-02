package plugin.interaction.combat.special.melee;

import org.redrune.game.content.entity.actor.combat.player.AbstractCombatStyle;
import org.redrune.game.content.plugin.combat.SpecialAttackPlugin;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.mask.HitSplat;
import org.redrune.game.entity.actor.player.Player;

/**
 * @author Tyluur
 * @since 2019-05-01
 */
public class KorasiSwordSpecialAttackPlugin extends SpecialAttackPlugin {
	
	private static final Animation ANIMATION = new Animation(14788);
	
	private static final Graphics GRAPHICS = new Graphics(1729);
	
	@Override
	public int[] getWeaponIds() {
		return arguments(19784);
	}
	
	@Override
	public void fire(Player source, Actor target, AbstractCombatStyle style) {
		source.setNextAnimation(ANIMATION);
		source.setNextGraphics(GRAPHICS);

		if (!source.isInMultiArea()) {
			int maxHit = style.getCalculator().getMaximumHit(source, 1);
			double multiplier = 0.5 + Math.random();
			maxHit *= multiplier;
			style.sendHit(source, target, maxHit, style.getRandomDamage(source, target, 1 + multiplier), 0).getHit().setSplat(HitSplat.MAGIC_DAMAGE);
		} else {
		
		}
	}
}
