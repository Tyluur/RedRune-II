package plugin.interaction.combat.special.melee;

import org.redrune.game.content.entity.actor.combat.player.AbstractCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.combat.SpecialAttackPlugin;
import org.redrune.utility.functions.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/5/2017
 */
public class DragonScimitarSpecialAttackPlugin extends SpecialAttackPlugin {
	
	private static final Graphics GRAPHICS = new Graphics(2118);
	
	private static final Animation ANIMATION = new Animation(12031);
	
	@Override
	public int[] getWeaponIds() {
		return arguments(4587);
	}
	
	@Override
	public void fire(Player source, Actor target, AbstractCombatStyle style) {
		source.setNextAnimation(ANIMATION);
		source.setNextGraphics(GRAPHICS);
		int damage = style.getRandomDamage(source, target, 1);
		if (target instanceof Player) {
			Player p2 = (Player) target;
			if (damage > 0) {
				p2.putAttribute("PrayerBlocked", 5000 + Misc.currentTimeMillis());
				int[] prayerIds;
				if (p2.getPrayer().isAncientCurses()) {
					prayerIds = new int[] { 6, 7, 8, 9, 17, 18 };
				} else {
					prayerIds = new int[] { 16, 17, 18, 19 };
				}
				p2.getPrayer().closePrayers(prayerIds);
				p2.getAppearance().generateAppearanceData();
				p2.getPrayer().recalculatePrayer();
			}
		}
		style.sendHit(source, target, style.getCalculator().getMaximumHit(source, 1), damage, 0);
		style.playSound(2540, source, target);
	}
}
