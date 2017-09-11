package plugin.combat.magic.modern.god;

import com.rs.cores.thread.WorldThread;
import com.rs.game.content.combat.player.style.MagicCombatStyle;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.combat.spell.type.CombatSpellPlugin;
import com.rs.utility.constants.AttributeKey;
import com.rs.utility.constants.MagicConstants.MagicBook;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/7/2017
 */
public class FlamesOfZamorakSpellPlugin extends CombatSpellPlugin {
	
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
		return 78;
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
	public int gfxHeight() {
		return 0;
	}
	
	@Override
	public int spellId() {
		return 68;
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
