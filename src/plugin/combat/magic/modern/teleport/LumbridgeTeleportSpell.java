package plugin.combat.magic.modern.teleport;

import org.redrune.game.global.WorldTile;
import org.redrune.game.content.plugin.combat.spell.type.TeleportSpellPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class LumbridgeTeleportSpell extends TeleportSpellPlugin {
	
	@Override
	public int levelRequired() {
		return 31;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(EARTH_RUNE, 1, AIR_RUNE, 3, LAW_RUNE, 1);
	}
	
	@Override
	public WorldTile destination() {
		return new WorldTile(3222, 3218, 0);
	}
	
	@Override
	public int spellId() {
		return 43;
	}
	
	@Override
	public double exp() {
		return 41;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
}
