package plugin.combat.magic.lunar.teleport;

import org.redrune.game.global.WorldTile;
import org.redrune.game.plugin.combat.spell.type.TeleportSpellPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class MoonclanTeleportSpell extends TeleportSpellPlugin {
	
	@Override
	public WorldTile destination() {
		return new WorldTile(2100, 3915, 0);
	}
	
	@Override
	public int levelRequired() {
		return 69;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(ASTRAL_RUNE, 2, LAW_RUNE, 1, EARTH_RUNE, 2);
	}
	
	@Override
	public int spellId() {
		return 43;
	}
	
	@Override
	public double exp() {
		return 66;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.LUNAR;
	}
}
