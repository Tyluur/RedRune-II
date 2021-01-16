package plugin.interaction.combat.special.melee;

import org.redrune.game.content.entity.actor.combat.player.AbstractCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.combat.SpecialAttackPlugin;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/5/2017
 */
public class Dragon2HSpecialAttackPlugin extends SpecialAttackPlugin {
	
	@Override
	public int[] getWeaponIds() {
		return arguments(7158);
	}
	
	@Override
	public void fire(Player source, Actor target, AbstractCombatStyle style) {
		source.setNextAnimation(new Animation(7048));
		source.setNextGraphics(new Graphics(1225));
		style.sendHit(source, target, style.getCalculator().getMaximumHit(source,1), style.getRandomDamage(source, target, 1), 0);
	}
}
