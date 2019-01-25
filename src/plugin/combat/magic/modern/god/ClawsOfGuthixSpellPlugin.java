package plugin.combat.magic.modern.god;

import org.redrune.engine.thread.WorldThread;
import org.redrune.game.content.combat.player.style.MagicCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.plugin.combat.spell.type.CombatSpellPlugin;
import org.redrune.utility.constants.AttributeKey;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/7/2017
 */
public class ClawsOfGuthixSpellPlugin extends CombatSpellPlugin {
	
	@Override
	public int delay(Player player) {
		return 5;
	}
	
	@Override
	public int animationId() {
		return 811;
	}
	
	@Override
	public int hitGfx() {
		return 77;
	}
	
	@Override
	public int maxHit(Player player, Actor target) {
		if (player.getAttribute(AttributeKey.GOD_CHARGED, -1L) >= WorldThread.getTicksPassed()) {
			return 300;
		} else {
			return 200;
		}
	}
	
	@Override
	public void cast(Player source, Actor target, MagicCombatStyle style) {
		style.sendSpell(source, target, this);
	}
	
	@Override
	public int spellId() {
		return 67;
	}
	
	@Override
	public double exp() {
		return 34.5;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
}
