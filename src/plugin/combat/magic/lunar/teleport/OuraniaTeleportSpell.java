package plugin.combat.magic.lunar.teleport;

import com.rs.game.entity.WorldTile;
import com.rs.game.plugin.combat.spell.type.TeleportSpellPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/28/2017
 */
public class OuraniaTeleportSpell extends TeleportSpellPlugin {
	
	@Override
	public int levelRequired() {
		return 71;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(LAW_RUNE, 1, ASTRAL_RUNE, 2, EARTH_RUNE, 6);
	}
	
	@Override
	public WorldTile destination() {
		return new WorldTile(2469, 3247, 0);
	}
	
	@Override
	public int spellId() {
		return 54;
	}
	
	@Override
	public double exp() {
		return 69;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.LUNAR;
	}
}
