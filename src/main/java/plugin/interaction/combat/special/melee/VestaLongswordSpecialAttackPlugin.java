package plugin.interaction.combat.special.melee;

import org.redrune.game.content.entity.actor.combat.player.AbstractCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.combat.SpecialAttackPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/5/2017
 */
public class VestaLongswordSpecialAttackPlugin extends SpecialAttackPlugin {
	
	private static final Animation ANIMATION = new Animation(10502);
	
	@Override
	public int[] getWeaponIds() {
		return arguments(13899, 13901);
	}
	
	@Override
	public void fire(Player source, Actor target, AbstractCombatStyle style) {
		source.setNextAnimation(ANIMATION);
		style.sendHit(source, target, style.getCalculator().getMaximumHit(source,1.20), style.getRandomDamage(source, target, 1.20), 0);
	}
}
