package plugin.combat.magic.ancient.teleport;

import com.rs.game.entity.WorldTile;
import com.rs.game.plugin.combat.spell.type.TeleportSpellPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class LassarTeleportSpell extends TeleportSpellPlugin {
	
	@Override
	public int levelRequired() {
		return 72;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(LAW_RUNE, 2, WATER_RUNE, 4);
	}
	
	@Override
	public WorldTile destination() {
		return new WorldTile(3006, 3471, 0);
	}
	
	@Override
	public int spellId() {
		return 43;
	}
	
	@Override
	public double exp() {
		return 82;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
}
