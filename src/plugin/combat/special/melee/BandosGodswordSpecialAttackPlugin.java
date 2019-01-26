package plugin.combat.special.melee;

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
public class BandosGodswordSpecialAttackPlugin extends SpecialAttackPlugin {
	
	private static final Graphics GRAPHICS = new Graphics(2114);
	
	private static final Animation ANIMATION = new Animation(11991);
	
	@Override
	public int[] getWeaponIds() {
		return arguments(11696, 23680);
	}
	
	@Override
	public void fire(Player source, Actor target, AbstractCombatStyle style) {
		source.setNextAnimation(ANIMATION);
		source.setNextGraphics(GRAPHICS);
		int damage = style.getRandomDamage(source, target, 1.21);
		style.sendHit(source, target, style.getCalculator().getMaximumHit(source,1.21), damage, 0);
		if (target.isPlayer()) {
			Player targetPlayer = target.toPlayer();
			int amountLeft;
			if ((amountLeft = targetPlayer.getSkills().drainLevel(DEFENCE, damage / 10)) > 0) {
				if ((amountLeft = targetPlayer.getSkills().drainLevel(STRENGTH, amountLeft)) > 0) {
					if ((amountLeft = targetPlayer.getSkills().drainLevel(PRAYER, amountLeft)) > 0) {
						if ((amountLeft = targetPlayer.getSkills().drainLevel(ATTACK, amountLeft)) > 0) {
							if ((amountLeft = targetPlayer.getSkills().drainLevel(MAGIC, amountLeft)) > 0) {
								if (targetPlayer.getSkills().drainLevel(RANGE, amountLeft) > 0) {
									return;
								}
							}
						}
					}
				}
			}
		}
	}
}
