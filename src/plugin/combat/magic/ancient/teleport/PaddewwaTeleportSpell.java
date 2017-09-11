package plugin.combat.magic.ancient.teleport;

import com.rs.game.entity.WorldTile;
import com.rs.game.plugin.combat.spell.type.TeleportSpellPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class PaddewwaTeleportSpell extends TeleportSpellPlugin {
	
	@Override
	public int levelRequired() {
		return 54;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(LAW_RUNE, 2, FIRE_RUNE, 1, AIR_RUNE, 1);
	}
	
	@Override
	public WorldTile destination() {
		return new WorldTile(3099, 9882, 0);
	}
	
	@Override
	public int spellId() {
		return 40;
	}
	
	@Override
	public double exp() {
		return 64;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
}
