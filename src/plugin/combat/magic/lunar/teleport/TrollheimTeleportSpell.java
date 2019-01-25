package plugin.combat.magic.lunar.teleport;

import org.redrune.game.global.WorldTile;
import org.redrune.game.plugin.combat.spell.type.TeleportSpellPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/28/2017
 */
public class TrollheimTeleportSpell extends TeleportSpellPlugin {
	
	@Override
	public int levelRequired() {
		return 92;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(ASTRAL_RUNE, 3, LAW_RUNE, 3, WATER_RUNE, 10);
	}
	
	@Override
	public WorldTile destination() {
		return new WorldTile(2807, 3678, 0);
	}
	
	@Override
	public int spellId() {
		return 75;
	}
	
	@Override
	public double exp() {
		return 101;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.LUNAR;
	}
}
