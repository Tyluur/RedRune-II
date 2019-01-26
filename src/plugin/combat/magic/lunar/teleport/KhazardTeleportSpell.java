package plugin.combat.magic.lunar.teleport;

import org.redrune.game.global.WorldTile;
import org.redrune.game.content.plugin.combat.spell.type.TeleportSpellPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/28/2017
 */
public class KhazardTeleportSpell extends TeleportSpellPlugin {
	
	@Override
	public int levelRequired() {
		return 78;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(ASTRAL_RUNE, 2, LAW_RUNE, 2, WATER_RUNE, 4);
	}
	
	@Override
	public WorldTile destination() {
		return new WorldTile(2656, 3157, 0);
	}
	
	@Override
	public int spellId() {
		return 41;
	}
	
	@Override
	public double exp() {
		return 80;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.LUNAR;
	}
}
