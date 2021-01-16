package plugin.interaction.combat.magic.lunar.teleport;

import org.redrune.game.global.WorldTile;
import org.redrune.game.content.plugin.combat.spell.type.TeleportSpellPlugin;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 7/28/2017
 */
public class BarbarianTeleportSpell implements TeleportSpellPlugin {
	
	@Override
	public int levelRequired() {
		return 75;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(ASTRAL_RUNE, 2, LAW_RUNE, 2, FIRE_RUNE, 3);
	}
	
	@Override
	public WorldTile destination() {
		return new WorldTile(2544, 3572, 0);
	}
	
	@Override
	public int spellId() {
		return 22;
	}
	
	@Override
	public double exp() {
		return 77;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.LUNAR;
	}
}
