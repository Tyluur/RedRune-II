package plugin.combat.special.magic;

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
public class StaffOfLightSpecialAttackPlugin extends SpecialAttackPlugin {
	
	private static final Animation ANIMATION = new Animation(12804);
	
	private static final Graphics GRAPHICS = new Graphics(2319);
	
	private static final Graphics GRAPHICS1 = new Graphics(2321);
	
	@Override
	public int[] getWeaponIds() {
		return arguments(15486, 22207, 22209, 22211, 22213);
	}
	
	@Override
	public void fire(Player source, Actor target, AbstractCombatStyle style) {
		source.setNextAnimation(ANIMATION);
		source.setNextGraphics(GRAPHICS);
		source.setNextGraphics(GRAPHICS1);
		source.addPolDelay(60000);
	}
}
