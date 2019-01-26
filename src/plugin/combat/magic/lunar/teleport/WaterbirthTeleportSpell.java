package plugin.combat.magic.lunar.teleport;

import org.redrune.game.global.WorldTile;
import org.redrune.game.content.plugin.combat.spell.type.TeleportSpellPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/28/2017
 */
public class WaterbirthTeleportSpell extends TeleportSpellPlugin {
	
	@Override
	public int levelRequired() {
		return 72;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(ASTRAL_RUNE, 2, LAW_RUNE, 1, WATER_RUNE, 1);
	}
	
	@Override
	public WorldTile destination() {
		return new WorldTile(2527, 3739, 0);
	}
	
	@Override
	public int spellId() {
		return 47;
	}
	
	@Override
	public double exp() {
		return 72;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.LUNAR;
	}
}
