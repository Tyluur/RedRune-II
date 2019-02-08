package plugin.interaction.combat.magic.modern.god;

import org.redrune.engine.cycle.GameCycleWorker;
import org.redrune.game.content.combat.player.style.MagicCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.combat.spell.type.CombatSpellPlugin;
import org.redrune.utility.constants.key.AttributeKey;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/7/2017
 */
public class FlamesOfZamorakSpellPlugin implements CombatSpellPlugin {
	
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
		if (player.getTemporaryAttribute(AttributeKey.GOD_CHARGED, -1L) >= GameCycleWorker.getTicksPassed()) {
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
