package plugin.combat.magic.lunar.teleport;

import com.rs.game.entity.WorldTile;
import com.rs.game.plugin.combat.spell.type.TeleportSpellPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/28/2017
 */
public class NorthArdougneTeleportSpell extends TeleportSpellPlugin {
	
	@Override
	public int levelRequired() {
		return 76;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(ASTRAL_RUNE, 2, LAW_RUNE, 1, WATER_RUNE, 5);
	}
	
	@Override
	public WorldTile destination() {
		return new WorldTile(2614, 3347, 0);
	}
	
	@Override
	public int spellId() {
		return 69;
	}
	
	@Override
	public double exp() {
		return 76;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.LUNAR;
	}
}
