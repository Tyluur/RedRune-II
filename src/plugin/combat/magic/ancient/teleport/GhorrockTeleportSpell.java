package plugin.combat.magic.ancient.teleport;

import com.rs.game.entity.WorldTile;
import com.rs.game.plugin.combat.spell.type.TeleportSpellPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class GhorrockTeleportSpell extends TeleportSpellPlugin {
	
	@Override
	public int levelRequired() {
		return 96;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(LAW_RUNE, 2, WATER_RUNE, 8);
	}
	
	@Override
	public WorldTile destination() {
		return new WorldTile(2977, 3873, 0);
	}
	
	@Override
	public int spellId() {
		return 47;
	}
	
	@Override
	public double exp() {
		return 106;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
}
